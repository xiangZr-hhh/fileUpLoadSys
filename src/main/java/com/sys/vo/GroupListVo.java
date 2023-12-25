package com.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/*
        张睿相   Java

        分组---项目号的vo类
*/
@Data
@AllArgsConstructor
public class GroupListVo {

    private int id;

    private String projectName;

    private List<ProductListVo> productList;

}
