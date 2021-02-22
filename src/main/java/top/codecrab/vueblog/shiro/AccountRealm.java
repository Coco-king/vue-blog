package top.codecrab.vueblog.shiro;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.codecrab.vueblog.entity.User;
import top.codecrab.vueblog.service.UserService;
import top.codecrab.vueblog.utils.JwtUtils;

/**
 * 验证token是否正确的类
 */
@Slf4j
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    /**
     * 判断传入的token是否是jwtToken，不是则不向下执行
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 获取授权信息，判断权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 得到验证信息(token)后，在此处做校验登录
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken token = (JwtToken) authenticationToken;
        log.info("jwt =======> {}", token.toString());

        //在用户注册时已经把userId放入了Subject中，现在直接根据传入的token取去
        String userId = jwtUtils.getClaimByToken((String) token.getPrincipal()).getSubject();
        //在数据库查询该用户信息
        User user = userService.getById(userId);

        //判断该用户是否存在
        if (user == null) throw new UnknownAccountException("该账户不存在！");
        //判断用户是否激活
        if (user.getStatus() == 0) throw new LockedAccountException("账户未激活，请到邮箱激活邮件激活！");
        //判断用户是否被封号
        if (user.getStatus() == -1) throw new LockedAccountException("账户已被封禁！");

        //把一些可以公开的数据拷贝到AccountProfile，返回给用户
        AccountProfile profile = new AccountProfile();
        BeanUtils.copyProperties(user, profile);
        log.info("AccountProfile ======> {}", profile.toString());

        //三个参数依次为 用户部分信息，用户登录的jwt信息，接口中的方法
        return new SimpleAuthenticationInfo(profile, token.getCredentials(), getName());
    }
}
