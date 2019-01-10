package com.doopp.gauss.server.entity;

import lombok.Data;

@Data
public class FilmSource {

    // 主键
    private Long source_id;

    // 电影信息的关联 ID
    private Long film_id;

    // 名称
    private String film_name;

    // from download site
    private String source_site;

    // source url
    private String source_url;

    // source data
    private String source_data;
}
