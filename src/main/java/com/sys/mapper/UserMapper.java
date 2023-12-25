package com.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sys.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * (User)表数据库访问层
 *
 * @author zrx
 * @since 2023-12-22 22:17:48
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
