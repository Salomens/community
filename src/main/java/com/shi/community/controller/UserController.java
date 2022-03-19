package com.shi.community.controller;

import com.shi.community.annotation.LoginRequired;
import com.shi.community.entity.User;
import com.shi.community.service.LikeService;
import com.shi.community.service.UserService;
import com.shi.community.util.CommunityUtil;
import com.shi.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    //头像上传路径
    @Value("${community.path.upload}")
    private String uploadPath;

    //上传域名
    @Value("${community.path.domain}")
    private String domain;

    //访问路径
    @Value("${server.servlet.context-path}")
    private String conextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;

    @LoginRequired
    @GetMapping("/setting")
    public String getStingPage(){
        return "/site/setting";
    }

    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage , Model model){
        if (headerImage == null){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }
        String filename = headerImage.getOriginalFilename();
        //截取文件后缀名
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }
        //生成随机文件名
        filename = CommunityUtil.generateUUID() + suffix;
        //确定文件存放路径
        File dest = new File(uploadPath+"/"+filename);
        //将图片文件写入存放路径
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败" + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常",e);
        }
        //更新用户头像放访问路径(web上的路径)
        User user = hostHolder.getUser();
        String headerUrl = domain + conextPath + "/user/header/" + filename;
        userService.updateHeader(user.getId(),headerUrl);
        //更新头像成功后重定向到首页
        return "redirect:/index";
    }

    @GetMapping("/header/{filename}")
     public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
        //服务器存放的路径
        filename = uploadPath + "/" + filename;
        //文件的后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        //响应图片
        response.setContentType("image/" + suffix);

        try {
            //获取字节流
            ServletOutputStream os = response.getOutputStream();
            //创建输入流
            FileInputStream fis = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1){
                os.write(buffer,0,b);
            }
            os.close();
            fis.close();

        } catch (IOException e) {
            logger.error("读取头像失败" + e.getMessage());
        }

    }
    //个人主页
    @GetMapping("/profile/{userId}")
    public String getProfilePage(@PathVariable("userId") int userId, Model model){
        User user = userService.findUserById(userId);
        if (user == null){
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user",user);
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);

        return "/site/profile";
    }

}
