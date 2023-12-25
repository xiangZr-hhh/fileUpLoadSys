package com.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sys.entity.GroupOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * (GroupOrder)表数据库访问层
 *
 * @author zrx
 * @since 2023-12-23 11:48:33
 */
@Mapper
public interface GroupOrderMapper extends BaseMapper<GroupOrder> {

}
