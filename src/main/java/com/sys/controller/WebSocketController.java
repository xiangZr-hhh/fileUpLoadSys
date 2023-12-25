package com.sys.controller;


import com.alibaba.fastjson.JSON;
import com.sys.config.WebSocket;
import com.sys.constans.ResponseResult;
import com.sys.entity.Myfile;
import com.sys.service.impl.FileServiceImpl;
import com.sys.vo.WescoketRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


/**
 * @author Liby
 * @date 2022-04-26 10:28
 * @description:建立WebSocket连接
 * @version:
 */
@RestController
@RequestMapping("/webSocket")
public class WebSocketController {

    @Autowired
    private WebSocket webSocket;
    @Autowired
    private FileServiceImpl fileService;

    @PostMapping("/sendMessage/{userId}")
    public ResponseResult sentMessage(@PathVariable("userId") int userId, @RequestBody WescoketRequestVo wescoketRequestVo) {

        String method = wescoketRequestVo.getMethod();
        ResponseResult data = new ResponseResult<>();
        if (method.equals("addFile")) {
            data = fileService.getFile();
        }
        List<Myfile> myfiles = (List<Myfile>) data.getData();

        System.out.println(JSON.toJSONString(myfiles));
        try {
            webSocket.sendInfo(JSON.toJSONString(myfiles));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseResult.okResult();
    }


}

