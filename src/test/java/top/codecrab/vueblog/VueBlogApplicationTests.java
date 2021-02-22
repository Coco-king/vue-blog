package top.codecrab.vueblog;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VueBlogApplicationTests {

    @Test
    void testUUID() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        System.out.println(uuid);
        System.out.println(uuid.length());
    }

    @Test
    void testMD5() {
        String md5 = SecureUtil.md5("");
        System.out.println(md5);
    }
}
