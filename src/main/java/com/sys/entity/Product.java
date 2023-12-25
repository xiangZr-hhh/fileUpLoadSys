package com.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Product)表实体类
 *
 * @author zrx
 * @since 2023-12-22 22:17:47
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("product")
public class Product  {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String productName;

    public Product(String productName) {
        this.productName = productName;
    }
}
