package com.shi.community.dao;

import com.shi.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    //分页查询帖子
    List<DiscussPost> selectDiscussPost(int userId, int offset, int limit, int orderMode);
    int selectDiscussPostRows(@Param("userId") int userId);//查询帖子的行数
    //插入一条帖子
    int insertDiscussPost(DiscussPost discussPost);

    //通过id找帖子
    DiscussPost selectDiscussPostNById(int id);
    //更新评论数量
    int updateCommentCount(int id, int commentCount);
    //修改帖子类型
    int updateType(int id,int type);

    int updateStatus(int id,int status);
    int updateScore(int id,double score);


}
