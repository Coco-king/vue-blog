package top.codecrab.vueblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.codecrab.vueblog.base.BaseController;
import top.codecrab.vueblog.common.response.Result;

/**
 * 短信接口
 */
@RestController
@RequestMapping("/sms")
public class SmsController extends BaseController {

    /**
     * 向指定手机号发送验证码，并保存在redis中
     */
    @PostMapping("/code")
    public Result sendVerifyCode(@RequestParam("phone") String phone) {
        return userService.sendVerifyCode(phone);
    }

}
