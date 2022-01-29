package com.shi.community.dao;

import com.shi.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User selectById(int id);
    User selectByName(String username);
    User selectByEmail(String email);

    int insertUser(User user);
    int updateStatus(int id, int status);
    int updateHeader(int id, String url);
    int updatePassword(int id, String password);
}
