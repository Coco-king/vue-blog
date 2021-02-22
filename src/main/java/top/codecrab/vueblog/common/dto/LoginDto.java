package top.codecrab.vueblog.common.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {

    @NotBlank(message = "用户名不能为空")
    @Length(min = 4, max = 12, message = "用户名必须在4-12位之间！")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 16, message = "密码必须在8-16位之间！")
    private String password;

    @NotBlank(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "请输入6位的验证码")
    private String verificationCode;
}
