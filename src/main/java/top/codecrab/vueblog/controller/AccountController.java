package top.codecrab.vueblog.controller;

import io.swagger.annotations.Api;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.codecrab.vueblog.base.BaseController;
import top.codecrab.vueblog.common.dto.LoginDto;
import top.codecrab.vueblog.common.dto.RegisterDto;
import top.codecrab.vueblog.common.response.Result;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@Api(tags = "账户登录、登出接口")
public class AccountController extends BaseController {

    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterDto registerDto, HttpServletResponse response) {
        return userService.register(registerDto, response);
    }

    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        return userService.login(loginDto, response);
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.success(null);
    }

}
