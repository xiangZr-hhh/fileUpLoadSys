package com.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sys.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Product)表数据库访问层
 *
 * @author zrx
 * @since 2023-12-22 22:17:47
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}
