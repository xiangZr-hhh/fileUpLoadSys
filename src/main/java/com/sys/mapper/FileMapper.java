package com.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sys.entity.Myfile;
import org.apache.ibatis.annotations.Mapper;

/**
 * (File)表数据库访问层
 *
 * @author zrx
 * @since 2023-12-22 22:17:46
 */
@Mapper
public interface FileMapper extends BaseMapper<Myfile> {

}
