package com.shi.community.dao;

import com.shi.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    //通过实体查询评论,eg:查询帖子的评论,查询评论的评论
    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);
    //返回一共有多少评论
    int selectCountByEntity(int entityType, int entityId);
}
