package top.codecrab.vueblog.utils;

import org.apache.shiro.SecurityUtils;
import top.codecrab.vueblog.shiro.AccountProfile;

public class ShiroUtils {

    public static AccountProfile getAccountProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
