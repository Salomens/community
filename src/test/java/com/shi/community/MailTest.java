package com.shi.community;

import com.shi.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void testMail(){
        mailClient.sendMail("318761557@qq.com","test","111");
    }
    @Test
    public void testHTMLMail(){
        Context context = new Context();
        context.setVariable("username","shi");
        String s = templateEngine.process("/mail/demo", context);
        System.out.println(s);
        mailClient.sendMail("318761557@qq.com","test",s);
    }
}
