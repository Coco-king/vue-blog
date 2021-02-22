package top.codecrab.vueblog.service;

import top.codecrab.vueblog.common.dto.LoginDto;
import top.codecrab.vueblog.common.response.Result;
import top.codecrab.vueblog.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author codecrab
 * @since 2021-02-22
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     */
    Result login(LoginDto loginDto, HttpServletResponse response);
}
