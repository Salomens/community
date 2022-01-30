package com.shi.community.service;

import com.shi.community.dao.UserMapper;
import com.shi.community.entity.User;
import com.shi.community.util.CommunityConstant;
import com.shi.community.util.CommunityUtil;
import com.shi.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.awt.geom.IllegalPathStateException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    /**
     * 注册
     * @param user
     * @return 用户集合
     */
    public Map<String ,Object> register(User user){
        Map<String, Object> map = new HashMap<>();

        //空值处理
        if (user == null){
            throw new IllegalPathStateException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emaiMsg","Email不能为空");
        }

        //验证账号是否存在
        User u = userMapper.selectByName(user.getUsername());
        if (u != null){
                map.put("usernameMsg","用户名已存在");
                return map;
        }
        //验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null){
                map.put("emailMsg","Email已存在");
                return map;
        }

        //开始注册用户
        //密码加密
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);//默认普通用户;
        user.setStatus(0);//没有被激活
        user.setActivationCode(CommunityUtil.generateUUID());//激活码
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);//调用dao层存储数据

        //发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("mail/activation", context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);
        return map;
    }
    public int activation(int userId,String code){
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1){//重复激活
            return ACTIVATION_REPEAT;
        }else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAILURE;//激活失败
        }
    }


}
