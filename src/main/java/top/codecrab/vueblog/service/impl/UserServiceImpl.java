package top.codecrab.vueblog.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.HttpResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import top.codecrab.vueblog.common.dto.LoginDto;
import top.codecrab.vueblog.common.dto.RegisterDto;
import top.codecrab.vueblog.common.response.Result;
import top.codecrab.vueblog.config.SmsProperties;
import top.codecrab.vueblog.entity.User;
import top.codecrab.vueblog.mapper.UserMapper;
import top.codecrab.vueblog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.vueblog.utils.JwtUtils;
import top.codecrab.vueblog.utils.NumberUtils;
import top.codecrab.vueblog.utils.SmsUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author codecrab
 * @since 2021-02-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String KEY_PREFIX = "user:verify:";

    @Value("${vue-blog.user.default-avatar}")
    private String avatar;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SmsUtils smsUtils;

    @Autowired
    private SmsProperties smsProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 用户注册
     */
    @Override
    public Result register(RegisterDto registerDto, HttpServletResponse response) {
        //获取缓存中的验证码
        String redisCode = redisTemplate.opsForValue().get(KEY_PREFIX + registerDto.getPhone());
        //比对验证码是否正确
        if (!StringUtils.equals(registerDto.getVerificationCode(), redisCode)) return Result.fail("验证码错误");
        User user = this.getOne(new QueryWrapper<User>().eq("username", registerDto.getUsername()));
        //如果用户不为空，则抛出异常
        Assert.isNull(user, "用户名已被注册");
        user = this.getOne(new QueryWrapper<User>().eq("phone", registerDto.getPhone()));
        //如果用户不为空，则抛出异常
        Assert.isNull(user, "手机号已被注册");
        user = new User();
        user.setUsername(registerDto.getUsername());
        String[] split = avatar.split(",");
        int i = new Random().nextInt(split.length);
        user.setAvatar(split[i]);
        user.setEmail(registerDto.getEmail());
        user.setPhone(registerDto.getPhone());
        //设置状态 0：未激活 1：已激活 -1：已封禁
        user.setStatus(1);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        user.setSalt(uuid);
        user.setCreated(LocalDateTime.now());
        //设置加密的密码
        user.setPassword(SecureUtil.md5(registerDto.getPassword() + uuid));
        boolean save = this.save(user);
        if (save) {
            //删除缓存
            redisTemplate.delete(KEY_PREFIX + user.getPhone());
            return new Result(201, "注册成功", null);
        } else {
            return Result.fail("注册失败");
        }
    }

    /**
     * 向指定手机号发送验证码，并保存在redis中
     */
    @Override
    public Result sendVerifyCode(String phone) {
        if (StringUtils.isBlank(phone)) return Result.fail("手机号码为空");
        //生成验证码
        String code = NumberUtils.generateCode(6);
        //调用短信类发送短信
        try {
            CommonResponse response = smsUtils.sendSms(phone, code, smsProperties.getSignName(), smsProperties.getVerifyCodeTemplate());
            Map map = JSONUtil.toBean(response.getData(), Map.class);
            if ("OK".equals(map.get("Code"))) {
                //保存到redis中，五分钟的有效时间
                redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5L, TimeUnit.MINUTES);
                return Result.success(null);
            }
            return Result.fail("短信未发送成功");
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return Result.fail("验证码发送失败");
    }

    /**
     * 用户登录
     */
    @Override
    public Result login(LoginDto loginDto, HttpServletResponse response) {
        User user = this.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        Assert.notNull(user, "用户不存在");
        //对用户输入的登录密码进行加盐加密
        String password = SecureUtil.md5(loginDto.getPassword() + user.getSalt());
        if (password.equals(user.getPassword())) {
            if (user.getStatus() != 1) return Result.fail("用户未激活或已封禁");
            //登陆成功
            String jwt = jwtUtils.generateToken(user.getId());
            //把jwt设置到头信息
            response.setHeader(jwtUtils.getHeader(), jwt);
            //默认情况下，头信息显示6个简单的响应标头：
            //如果您希望客户端能够访问其他标题，则必须使用Access-Control-Expose-Headers标题列出它们。
            response.setHeader("Access-Control-Expose-Headers", jwtUtils.getHeader());
            //更新上次登陆时间
            user.setLastLogin(LocalDateTime.now());
            //更新
            this.updateById(user);
            //将前台需要的数据返回回去
            return Result.success(MapUtil.builder()
                    .put("id", user.getId())
                    .put("username", user.getUsername())
                    .put("avatar", user.getAvatar())
                    .put("email", user.getEmail())
                    .put("lastLogin", user.getLastLogin())
                    .map()
            );
        }
        //登陆失败，密码错误
        return Result.fail("用户名或密码错误");
    }

}
