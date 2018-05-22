package com.doopp.gauss.server.entity;

import lombok.Data;

@Data
public class Movie {

    private Long id;

    private String type;

    private String from_url;

    private String name;

    private String cover;

    private String intro;

    private String download_links;

    private String created_at;

}
