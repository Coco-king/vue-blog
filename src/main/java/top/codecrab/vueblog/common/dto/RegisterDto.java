package top.codecrab.vueblog.common.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class RegisterDto implements Serializable {

    @NotBlank(message = "用户名不能为空")
    @Length(min = 4, max = 12, message = "用户名必须在4-12位之间！")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 16, message = "密码必须在8-16位之间！")
    private String password;

    @Pattern(regexp = "0?(13|14|15|17|18)[0-9]{9}", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "请输入6位的验证码")
    private String verificationCode;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
