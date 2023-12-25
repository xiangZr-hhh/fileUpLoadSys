package com.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
        张睿相   Java
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddFileVo {

    private String fileName;

    private String version;

    private String content;

    private int projectId;

    private int productId;

    private int standardId;

    private int role;

}
