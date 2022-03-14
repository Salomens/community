package com.shi.community.controller;

import com.shi.community.entity.DiscussPost;
import com.shi.community.entity.Page;
import com.shi.community.entity.User;
import com.shi.community.service.DiscussPostService;
import com.shi.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class HomeController {
    //调用service
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){

        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.findDiscussPost(0, page.getOffset(), page.getLimit());
        List<Map<String ,Object>> discussPosts = new ArrayList<>();
        if (list != null){
            for (DiscussPost post : list) {
                HashMap<String , Object> map = new HashMap<>();
                map.put("post",post);
                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "index";
    }
    @GetMapping("/error")
    public String error(){
        return "/error/500";
    }

}
