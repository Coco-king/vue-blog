package top.codecrab.vueblog.shiro;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 存放一些可以被公开的用户数据
 */
@Data
@ToString
public class AccountProfile {

    private Long id;

    private String username;

    private String avatar;

    private String email;

    private LocalDateTime lastLogin;

}
