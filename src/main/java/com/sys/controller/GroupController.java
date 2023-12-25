package com.sys.controller;

import com.sys.constans.ResponseResult;

import com.sys.constans.enums.AppHttpCodeEnum;
import com.sys.service.impl.GroupOrderServiceImpl;
import com.sys.vo.AddGroupRequestVo;
import com.sys.vo.GroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*
        张睿相   Java
*/
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupOrderServiceImpl groupOrderService;

    @GetMapping("/getAll")
    public ResponseResult getAllGroup(){
        ResponseResult result = groupOrderService.getAll();
        return result;
    }


    @PostMapping("/delete")
    public ResponseResult deleteGroup(@RequestBody GroupVo groupVo){

        if(groupVo == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NULL);
        }

        ResponseResult result = groupOrderService.deleteGroup(groupVo);
        return result;
    }

    @PostMapping("/add")
    public ResponseResult addGroup(@RequestBody AddGroupRequestVo addGroupResponseVo){

        ResponseResult result = groupOrderService.addGroup(addGroupResponseVo);

        return result;
    }


}
