package com.doopp.gauss.server.entity;

import lombok.Data;

@Data
public class FilmComment {

    // 主键
    private Long comment_id;

    // 电影信息的关联 ID
    private Long film_id;

    // 名称
    private String film_name;

    // 评论得票数
    private int vote;

    // 评论人
    private String user_name;

    // 评论时间
    private String date_time;

    // 评论内容
    private String content;
}
