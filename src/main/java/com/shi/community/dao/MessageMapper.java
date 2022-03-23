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
    //新增消息
    int insertMessage(Message message);
    //更改消息状态
    int updateStatus(List<Integer> ids, int status);
    //查询某个主题下的最新通知
    Message selectLatestNotice(int userId,String topic);
    //查询某个主题包含的通知的数量
    int selectNoticeCount(int userId,String topic);
    //查询未读的通知的数量
    int selectNoticeUnread(int userId,String topic);
    //查询某个主题的通知列表
    List<Message> selectNotices(int userId, String topic,int offset,int limit);

}
