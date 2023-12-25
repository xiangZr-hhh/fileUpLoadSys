package com.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sys.constans.ResponseResult;
import com.sys.entity.GroupOrder;
import com.sys.vo.AddGroupRequestVo;
import com.sys.vo.GroupVo;


/**
 * (GroupOrder)表服务接口
 *
 * @author zrx
 * @since 2023-12-23 11:48:34
 */
public interface GroupOrderService extends IService<GroupOrder> {

    ResponseResult getAll();

    ResponseResult deleteGroup(GroupVo groupVo);

    ResponseResult addGroup(AddGroupRequestVo addGroupResponseVo);
}

