package com.sys.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Myfile)表实体类
 *
 * @author zrx
 * @since 2023-12-23 14:07:34
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("myFile")
public class Myfile  {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String fileName;
    
    private String url;
    
    private Date createTime;
    
    private String version;
    
    private Integer uploadNumber;
    
    private String content;
    
    private String originalName;
    
    private Integer groupId;
    
    private Integer role;


}
