package com.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sys.entity.Standard;
import com.sys.mapper.StandardMapper;
import com.sys.service.StandardService;
import org.springframework.stereotype.Service;

/**
 * (Standard)表服务实现类
 *
 * @author zrx
 * @since 2023-12-22 22:41:15
 */
@Service("standardService")
public class StandardServiceImpl extends ServiceImpl<StandardMapper, Standard> implements StandardService {

}
