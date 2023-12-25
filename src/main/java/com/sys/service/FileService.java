package com.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sys.constans.ResponseResult;
import com.sys.entity.Myfile;
import com.sys.vo.AddFileVo;
import com.sys.vo.FileDataVo;
import com.sys.vo.SearchFileRequestVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * (File)表服务接口
 *
 * @author zrx
 * @since 2023-12-17 23:29:24
 */
public interface FileService extends IService<Myfile> {

    //    添加文件
    ResponseResult addFile(MultipartFile file, AddFileVo fileVo);

    ResponseResult getFile();

    ResponseResult searchFile(SearchFileRequestVo searchFileRequestVo);


    ResponseResult editFile(FileDataVo file);

    ResponseResult deleteFiles(List<Myfile> files);
}

