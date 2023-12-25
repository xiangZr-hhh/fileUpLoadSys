package com.sys.controller;


import com.alibaba.fastjson.JSON;
import com.sys.constans.ResponseResult;
import com.sys.constans.enums.AppHttpCodeEnum;
import com.sys.entity.Myfile;
import com.sys.service.impl.FileServiceImpl;
import com.sys.vo.AddFileVo;
import com.sys.vo.FileDataVo;
import com.sys.vo.SearchFileRequestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (File)表控制层
 *
 * @author zrx
 * @since 2023-12-17 23:29:24
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileServiceImpl fileService;


    //    上传文件接口
    @PostMapping("/upload")
    public ResponseResult addFile(@RequestPart("fileVo") String addFileVoJson
            , @RequestPart MultipartFile file) {

        AddFileVo addFileVo = JSON.parseObject(addFileVoJson, AddFileVo.class);
        System.out.println(addFileVo.getFileName());

        if (file.isEmpty()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NULL);
        }

        if (addFileVo == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NULL);
        }

        ResponseResult result = fileService.addFile(file, addFileVo);

        return result;

    }

    //  获取所有文件信息
    @GetMapping("getAll")
    public ResponseResult getAllFile() {
        ResponseResult result = fileService.getFile();
        return result;
    }

    //    搜索文件信息
    @GetMapping("search")
    public ResponseResult search(int page,
                                 int limit,
                                 String fileName,
                                 int projectId,
                                 int productId,
                                 int standardId) {
        SearchFileRequestVo searchFileRequestVo = new SearchFileRequestVo(page,limit,fileName,projectId,productId,standardId);
        ResponseResult result = fileService.searchFile(searchFileRequestVo);
        result.setCode(0);
        return result;
    }

    //    编辑文件
    @PostMapping("edit")
    public ResponseResult editFile(@RequestBody FileDataVo fileDataVo) {
        ResponseResult result = fileService.editFile(fileDataVo);
        return result;
    }

    //    删除文件
    @PostMapping("delete")
    public ResponseResult deleteFile(@RequestBody List<Myfile> files) {
        ResponseResult result = fileService.deleteFiles(files);
        return result;
    }


}

