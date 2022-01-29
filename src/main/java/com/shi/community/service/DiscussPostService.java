package com.shi.community.service;

import com.shi.community.dao.DiscussPostMapper;
import com.shi.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    //service层调用数据库层面查询字段
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPost(int id, int offset, int limit){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPost(id, offset, limit);
        return discussPosts;
    }

    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

}
