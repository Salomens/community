package com.shi.community;

import com.shi.community.entity.DiscussPost;
import com.shi.community.service.DiscussPostService;
import com.shi.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void testSens(){
        String text = "=赌*博666";
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);

    }


}
