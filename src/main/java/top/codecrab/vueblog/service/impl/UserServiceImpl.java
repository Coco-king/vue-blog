package top.codecrab.vueblog.service.impl;

import top.codecrab.vueblog.entity.User;
import top.codecrab.vueblog.mapper.UserMapper;
import top.codecrab.vueblog.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
