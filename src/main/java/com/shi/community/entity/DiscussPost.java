package com.shi.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
@Data
@ToString
@Mapper
@NoArgsConstructor
@AllArgsConstructor
public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;//帖子类型 0 普通帖子 1 置顶帖子
    private int status;//帖子状态 0:正常 1:精华 2:拉黑
    private Date createTime;
    private int commentCount;
    private double score;


}
