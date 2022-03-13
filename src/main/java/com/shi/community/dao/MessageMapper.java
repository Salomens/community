package com.shi.community.dao;

import com.shi.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    //查询当前用户的会话列表 针对每个会话只返回最新的一条消息
    List<Message> selectConversations(int userId, int offset, int limit);
    //查询当前用户的会话数量
    int selectConversationCount(int userId);
    //查询某个会话包含的私信列表 通过会话id查找
    List<Message> selectLetters(String conversationId, int offset, int limit);
    //查询某个会话包含的私信数量
    int selectLetterCount(String conversationId);
    //查询未读私信数量
    int selectLetterUnreadCount(int userId,String conversationId);

}
