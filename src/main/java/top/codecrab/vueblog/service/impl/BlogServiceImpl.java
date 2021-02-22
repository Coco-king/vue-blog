package top.codecrab.vueblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import sun.plugin.util.UserProfile;
import top.codecrab.vueblog.common.response.Result;
import top.codecrab.vueblog.entity.Blog;
import top.codecrab.vueblog.mapper.BlogMapper;
import top.codecrab.vueblog.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.codecrab.vueblog.shiro.AccountProfile;
import top.codecrab.vueblog.utils.ShiroUtils;

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
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    /**
     * 分页查询博客
     */
    public Result pageList(Integer page, Integer size) {
        if (page == null || page < 1) page = 1;
        if (size == null || size < 5) size = 5;
        //分页查询，按照创建时间倒序排列和状态查询博客，1：正常 0：删除 -1：封禁
        IPage<Blog> iPage = this.page(new Page<>(page, size),
                new QueryWrapper<Blog>().eq("status", 1).orderByDesc("created"));
        return Result.success(iPage);
    }

    /**
     * 博客详情
     */
    public Result blogDetail(Long id) {
        if (id == null || id < 1) Result.fail("请求参数有误！");
        //根据id和状态查询博客，1：正常 0：删除 -1：封禁
        Blog blog = this.getOne(new QueryWrapper<Blog>().eq("id", id).eq("status", 1));
        Assert.notNull(blog, "该文章已被删除或封禁");
        return Result.success(blog);
    }

    /**
     * 编辑或新增博客
     */
    public Result editBlog(Blog blog) {
        AccountProfile profile = ShiroUtils.getAccountProfile();
        Long blogId = blog.getId();
        Long userId = profile.getId();
        //文章的所属用户不等于当前用户
        if (!userId.equals(blog.getUserId())) return Result.fail("你没有权限修改不属于你的文章");
        if (blogId == null) {
            //新增博客
            blog.setStatus(1);
            blog.setUserId(userId);
            blog.setCreated(LocalDateTime.now());
            //保存
            boolean save = this.save(blog);
            return save ? new Result(201, "发布成功", null) : Result.fail("发布失败");
        } else if (blogId < 1) {
            return Result.fail("请求参数有误");
        } else {
            Blog one = this.getOne(new QueryWrapper<Blog>().eq("id", blogId).eq("status", 1));
            if (one == null) return Result.fail("未找到要编辑的文章");
            //编辑博客，把新传入的blog覆盖老的one
            one.setContent(blog.getContent());
            one.setDescription(blog.getDescription());
            one.setTitle(blog.getTitle());
            //执行更新
            boolean update = this.updateById(one);
            return update ? new Result(201, "编辑成功", null) : Result.fail("编辑失败");
        }
    }

    /**
     * 删除博客
     */
    public Result deleteBlog(Long id) {
        if (id == null || id < 1) Result.fail("请求参数有误！");
        //1：正常 0：删除 -1：封禁
        Blog blog = this.getOne(new QueryWrapper<Blog>().eq("id", id).eq("status", 1));
        Assert.notNull(blog, "文章不存在或已删除");
        Long userId = ShiroUtils.getAccountProfile().getId();
        //文章的所属用户不等于当前用户
        if (!userId.equals(blog.getUserId())) return Result.fail("你没有权限删除不属于你的文章");
        blog.setStatus(0);
        boolean update = this.updateById(blog);
        return update ? new Result(201, "删除成功", null) : Result.fail("文章不存在或已删除");
    }
}
