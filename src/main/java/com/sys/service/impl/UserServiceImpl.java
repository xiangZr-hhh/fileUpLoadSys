package com.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sys.constans.ResponseResult;
import com.sys.constans.enums.AppHttpCodeEnum;
import com.sys.entity.User;
import com.sys.mapper.UserMapper;
import com.sys.service.UserService;
import com.sys.vo.UserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (User)表服务实现类
 *
 * @author zrx
 * @since 2023-12-17 23:29:57
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseResult login(UserLoginVo userLoginVo){

        String username = userLoginVo.getUsername();
        String password = userLoginVo.getPassword();

        if(username != null&&password != null){
            LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(User::getUsername,username)
                    .eq(User::getPassword,password);
            User user = userMapper.selectOne(userWrapper);

            if(user != null){
                return ResponseResult.okResult(user);
            }

            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }

        return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
    }


}
