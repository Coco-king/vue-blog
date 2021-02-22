package top.codecrab.vueblog.service.impl;

import top.codecrab.vueblog.entity.Blog;
import top.codecrab.vueblog.mapper.BlogMapper;
import top.codecrab.vueblog.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author codecrab
 * @since 2021-02-22
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
