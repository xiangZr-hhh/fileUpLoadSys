package com.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sys.constans.ResponseResult;
import com.sys.entity.User;
import com.sys.vo.UserLoginVo;


/**
 * (User)表服务接口
 *
 * @author zrx
 * @since 2023-12-17 23:29:57
 */
public interface UserService extends IService<User> {

    ResponseResult login(UserLoginVo userLoginVo);
}

