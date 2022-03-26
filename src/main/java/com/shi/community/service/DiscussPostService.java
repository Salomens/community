package com.shi.community.service;

import com.shi.community.dao.DiscussPostMapper;
import com.shi.community.entity.DiscussPost;
import com.shi.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {
    //service层调用数据库层面查询字段
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> findDiscussPost(int id, int offset, int limit){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPost(id, offset, limit);
        return discussPosts;
    }

    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }
    //添加帖子
    public int addDiscussPost(DiscussPost post){
        if (post == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //转义html标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));
        return discussPostMapper.insertDiscussPost(post);
    }
    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostNById(id);
    }

    public int updateCommentCount(int id, int commentCount){
        return discussPostMapper.updateCommentCount(id,commentCount);
    }
    public int updateType(int id,int type){
        return discussPostMapper.updateType(id, type);
    }
    public int updateStatus(int id,int status){
        return discussPostMapper.updateStatus(id, status);
    }

}
