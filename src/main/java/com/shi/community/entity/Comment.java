package com.shi.community.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

//评论
@Data
@ToString
public class Comment {
    private int id;
    private int userId;
    private int entityType;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;
}
