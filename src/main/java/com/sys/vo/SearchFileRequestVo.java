package com.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
        张睿相   Java
*/
@Data
@AllArgsConstructor
public class SearchFileRequestVo {

    private int page;

    private int limit;

    private String fileName;

    private int projectId;

    private int productId;

    private int standardId;
}
