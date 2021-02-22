package top.codecrab.vueblog.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Getter
@Component
public class SmsProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private String signName;

    private String verifyCodeTemplate;

    public SmsProperties() {
        Properties prop = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = SmsProperties.class.getClassLoader().getResourceAsStream("sms.properties");
        try {
            Assert.notNull(in, "读取sms配置文件失败");
            prop.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            // 获取key对应的value值
            this.accessKeyId = prop.getProperty("sms.accessKeyId");
            this.accessKeySecret = prop.getProperty("sms.accessKeySecret");
            this.signName = prop.getProperty("sms.signName");
            this.verifyCodeTemplate = prop.getProperty("sms.verifyCodeTemplate");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
