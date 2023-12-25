package com.sys.controller;




import com.sys.constans.ResponseResult;
import com.sys.constans.enums.AppHttpCodeEnum;
import com.sys.service.UserService;
import com.sys.service.impl.UserServiceImpl;
import com.sys.vo.UserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (User)表控制层
 *
 * @author zrx
 * @since 2023-12-17 23:29:57
 */
@RestController
@RequestMapping("/user")
public class UserController  {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserLoginVo userLoginVo){

        if(userLoginVo != null){
          ResponseResult result = userService.login(userLoginVo);
          return result;
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NULL);
    }


}

