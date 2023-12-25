package com.sys.vo;

import lombok.Data;

/*
        张睿相   Java
*/
@Data
public class AddGroupRequestVo {

    private int projectId;

    private String projectName;

    private int productId;

    private String productName;

    private String standardName;

    private int isCreateProject;

    private  int isCreateProduct;

}
