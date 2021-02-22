package top.codecrab.vueblog.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.codecrab.vueblog.base.BaseController;
import top.codecrab.vueblog.common.response.Result;
import top.codecrab.vueblog.entity.User;

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

    @GetMapping("/index")
    @ApiOperation("测试程序")
    // @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    public Result index() {
        User user = userService.getById(1L);
        return Result.success(user);
    }
}
