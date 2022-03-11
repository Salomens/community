package com.shi.community.controller;

import com.shi.community.entity.DiscussPost;
import com.shi.community.entity.User;
import com.shi.community.service.DiscussPostService;
import com.shi.community.service.UserService;
import com.shi.community.util.CommunityUtil;
import com.shi.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscuss(String title,String content){
        User user = hostHolder.getUser();
        if (user == null){
            return CommunityUtil.getJSONString(403,"你还没有登录哦!");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);
        return CommunityUtil.getJSONString(0,"发布成功");
    }
    @GetMapping("/detail/{discussPostId}")
    public String  getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model) {
        //帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",post);
        //作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);
        return "/site/discuss-detail";
    }
}
