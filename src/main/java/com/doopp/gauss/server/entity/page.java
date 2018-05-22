package com.doopp.gauss.server.entity;

import lombok.Data;

@Data
public class page {

    private Long id;

    private String type;

    private String from_url;

    private String name;

    private String cover;

    private String intro;

    private String resource;

    private String created_at;

}
