package top.codecrab.vueblog.shiro;

import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import top.codecrab.vueblog.common.response.Result;
import top.codecrab.vueblog.utils.JwtUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 这个方法在shiro执行登录时会调用获取token
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //从请求头获取token
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader(jwtUtils.getHeader());
        //没有token返回空
        if (StringUtils.isBlank(jwt)) return null;
        return new JwtToken(jwt);
    }

    /**
     * 访问是否被拒绝，通过判断token来确定
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //从请求头获取token
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader(jwtUtils.getHeader());
        //没有token表示未登录，则放行。权限问题会在AccountRealm判断解决
        if (StringUtils.isBlank(jwt)) {
            return true;
        } else {
            //不为空，取出token的内容，判断是否过期或不存在
            Claims claim = jwtUtils.getClaimByToken(jwt);
            if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                throw new ExpiredCredentialsException("登录状态已失效，请重新登录！");
            }
            //token不为空并且没有过期，执行登录方法进行账号密码正确性判断
            return executeLogin(servletRequest, servletResponse);
        }
    }

    /**
     * 上面方法如果账号密码正确性判断失败，由于本项目是前后端分离，所以要返回错误数据，原失败方法只能返回一个false，所以此处需要重写
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        //获取异常信息
        Throwable cause = e.getCause() == null ? e : e.getCause();
        //将异常信息封装为我们的统一返回对象，转换为json写入到前台页面
        String json = JSONUtil.toJsonStr(Result.fail(cause.getMessage()));
        try {
            response.getWriter().print(json);
        } catch (IOException ioException) {
            log.error("onLoginFailure: 登陆失败向页面写入json数据时出错！ =====> {}", ioException.getMessage());
        }
        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

}
