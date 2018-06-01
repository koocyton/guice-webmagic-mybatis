package com.doopp.gauss.server.entity;

import lombok.Data;

@Data
public class Movie {

    // 主键
    private Long id;

    // 名称
    private String name;

    // 视频 图片 音频 文字
    private String type;

    // 第一分类
    private String first_category;

    // 第二分类
    private String second_category;

    // 第三分类
    private String third_category;

    // 封面
    private String cover;

    // 发布时间
    private String publish_date;

    // 译名
    private String translate_name;

    // 电影原名
    private String film_name;

    // 发布年代
    private String release_age;

    // 地区
    private String origin_place;

    // 分类描述
    private String category;

    // 语言
    private String language;

    // 字母
    private String subtitle;

    // 发行时间
    private String release_date;

    // 视频格式
    private String video_format;

    // 视频大小 ， 清晰度
    private String video_size;

    // 文件大小
    private String file_size;

    // 视频时长
    private String time_length;

    // 导演
    private String director;

    // 主演
    private String actors;

    // 下载链接
    private String download_links;

    // 简介
    private String intro;

    // 下载来源
    private String from_url;

    // 评分
    private int score;

    // 点赞
    private int like;

    // 下载次数
    private int download_sum;

    // 浏览次数
    private int browse_sum;
}
