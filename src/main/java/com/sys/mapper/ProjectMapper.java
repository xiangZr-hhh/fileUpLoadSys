package com.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sys.entity.Project;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Project)表数据库访问层
 *
 * @author zrx
 * @since 2023-12-22 22:17:47
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

}
