package top.codecrab.vueblog.controller;


import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import top.codecrab.vueblog.base.BaseController;
import top.codecrab.vueblog.common.annotation.AccessLimit;
import top.codecrab.vueblog.common.response.Result;
import top.codecrab.vueblog.entity.Blog;

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
@RequestMapping("/blog")
@Api(tags = "博客管理接口")
public class BlogController extends BaseController {

    @GetMapping("/list")
    public Result pageList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                           @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return blogService.pageList(page, size);
    }

    @GetMapping("/{id}")
    public Result blogDetail(@PathVariable("id") Long id) {
        return blogService.blogDetail(id);
    }

    @AccessLimit(seconds = 60 * 60, maxCount = 50)
    @RequiresAuthentication
    @PostMapping("/edit")
    public Result editBlog(@Valid @RequestBody Blog blog) {
        return blogService.editBlog(blog);
    }

    @RequiresAuthentication
    @DeleteMapping("/delete/{id}")
    public Result deleteBlog(@PathVariable("id") Long id) {
        return blogService.deleteBlog(id);
    }

    /**
     * 判断token是否合法
     */
    @RequiresAuthentication
    @GetMapping("/option")
    public Result option() {
       return Result.success(null);
    }
}
