package com.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sys.entity.Project;
import com.sys.mapper.ProjectMapper;
import com.sys.service.ProjectService;
import org.springframework.stereotype.Service;

/**
 * (Project)表服务实现类
 *
 * @author zrx
 * @since 2023-12-22 22:41:15
 */
@Service("projectService")
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

}
