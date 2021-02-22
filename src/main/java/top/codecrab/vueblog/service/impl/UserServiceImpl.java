package top.codecrab.vueblog.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import top.codecrab.vueblog.common.dto.LoginDto;
import top.codecrab.vueblog.common.response.Result;
import top.codecrab.vueblog.entity.User;
import top.codecrab.vueblog.mapper.UserMapper;
import top.codecrab.vueblog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.vueblog.utils.JwtUtils;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

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

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用户登录
     */
    public Result login(LoginDto loginDto, HttpServletResponse response) {
        User user = this.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        Assert.notNull(user, "用户不存在");
        //对用户输入的登录密码进行加盐加密
        String password = SecureUtil.md5(loginDto.getPassword() + user.getSalt());
        if (password.equals(user.getPassword())) {
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
