package top.codecrab.vueblog.base;

import org.springframework.beans.factory.annotation.Autowired;
import top.codecrab.vueblog.service.BlogService;
import top.codecrab.vueblog.service.UserService;
import top.codecrab.vueblog.utils.JwtUtils;

public class BaseController {
    @Autowired
    protected UserService userService;
    @Autowired
    protected BlogService blogService;
    @Autowired
    protected JwtUtils jwtUtils;
}
