package top.codecrab.vueblog.base;

import org.springframework.beans.factory.annotation.Autowired;
import top.codecrab.vueblog.service.UserService;

public class BaseController {
    @Autowired
    protected UserService userService;
}
