package com.sys.vo;

import lombok.Data;

import java.util.Date;

/*
        张睿相   Java
*/
@Data
public class FileDataVo {

    private Integer id;

    private String fileName;

    private String url;

    private Date createTime;

    private String version;

    private Integer uploadNumber;

    private String content;

    private String originalName;

    private String fileType;

    private String group;

    private int projectId;

    private int productId;

    private int standardId;

    private int role;
}
