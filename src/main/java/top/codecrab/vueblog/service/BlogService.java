package top.codecrab.vueblog.service;

import top.codecrab.vueblog.common.response.Result;
import top.codecrab.vueblog.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author codecrab
 * @since 2021-02-22
 */
public interface BlogService extends IService<Blog> {

    /**
     * 分页查询博客
     */
    Result pageList(Integer page, Integer size);

    /**
     * 博客详情
     */
    Result blogDetail(Long id);

    /**
     * 编辑或新增博客
     */
    Result editBlog(Blog blog);

    /**
     * 删除博客
     */
    Result deleteBlog(Long id);
}
