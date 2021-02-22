package top.codecrab.vueblog.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;
import top.codecrab.vueblog.base.BaseController;
import top.codecrab.vueblog.common.response.Result;
import top.codecrab.vueblog.entity.User;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author codecrab
 * @since 2021-02-22
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController extends BaseController {

    @RequiresAuthentication //必须登录才能访问本接口
    @GetMapping("/index")
    @ApiOperation("测试程序")
    public Result index() {
        User user = userService.getById(1L);
        return Result.success(user);
    }

    @PostMapping("/save")
    @ApiOperation("保存用户")
    @ApiImplicitParam(name = "user", value = "用户信息", required = true, paramType = "body", dataType = "User")
    public Result save(@Valid @RequestBody User user) {
        return Result.success(user);
    }
}
