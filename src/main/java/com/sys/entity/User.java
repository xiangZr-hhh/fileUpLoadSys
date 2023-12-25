package com.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (User)表实体类
 *
 * @author zrx
 * @since 2023-12-22 22:17:48
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User  {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String username;
    
    private String password;
    //(0,用户;1，管理员)
    private Integer role;


}
