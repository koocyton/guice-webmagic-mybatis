package com.doopp.gauss.server.entity;

import lombok.Data;

import java.util.Date;

@Data
public class FilmInfo {

    // 主键
    private Long id;

    // 电影名
    private String film_name;

    // 封面
    private String film_cover;

    // 导演
    private String directors;

    // 编剧
    private String screenwriters;

    // 主演
    private String actors;

    // 类型
    private String genres;

    // 制片国家/地区
    private String region;

    // 语言
    private String language;

    // 上映日期
    private String release_date;

    // 片长 分钟
    private int film_duration;

    // 又名
    private String alias;

    // 豆瓣ID
    private float douban_id;

    // 豆瓣评分
    private float douban_rating;

    // iMDb 链接
    private String imdb_link;

    // IMDB 评分
    private float imdb_rating;
}
