package com.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (GroupOrder)表实体类
 *
 * @author zrx
 * @since 2023-12-23 11:48:34
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("group_order")
public class GroupOrder  {
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private Integer projectId;
    
    private Integer productId;
    
    private Integer standardId;

    public GroupOrder(Integer projectId, Integer productId, Integer standardId) {
        this.projectId = projectId;
        this.productId = productId;
        this.standardId = standardId;
    }
}
