package com.sys.vo;

/*
        张睿相   Java


        分组---产品号的封装类
*/

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductListVo {

    private int id;

    private String name;

    private List<StandardListVo> standardList;
}
