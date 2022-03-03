package com.shi.community;

import com.shi.community.dao.DiscussPostMapper;
import com.shi.community.dao.LoginTicketMapper;
import com.shi.community.dao.UserMapper;
import com.shi.community.entity.DiscussPost;
import com.shi.community.entity.LoginTicket;
import com.shi.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void selectTset(){
        User user = userMapper.selectById(101);
        System.out.println(user);
        User liubei = userMapper.selectByName("liubei");
        System.out.println(liubei);
    }
    @Test
    public void testInsert(){
        User user = new User();
        user.setUsername("shiyi");
        user.setPassword("111111");
        user.setCreateTime(new Date());
        int insertUser = userMapper.insertUser(user);
        System.out.println(insertUser);
        System.out.println(user.getId());
    }
    @Test
    public void testUpdate(){
        int i = userMapper.updateStatus(151, 1);
        System.out.println(i);
        int i1 = userMapper.updatePassword(151, "44444");
        System.out.println(i1);
    }
    @Test
    public void testDis(){
        for (DiscussPost discussPost : discussPostMapper.selectDiscussPost(149, 0, 10)) {
            System.out.println(discussPost);
        }
        int i = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(i);
    }


    @Test
    public void loginADD(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000*600));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }
//    @Test
//    public void selectloginTicket(){
//        LoginTicket abc = loginTicketMapper.selectByTicket("abc");
//        System.out.println(abc);
//        loginTicketMapper.updateStatus("abc",1);
//    }

}
