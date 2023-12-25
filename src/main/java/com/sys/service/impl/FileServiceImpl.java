package com.sys.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sys.constans.ResponseResult;
import com.sys.constans.enums.AppHttpCodeEnum;

import com.sys.entity.GroupOrder;
import com.sys.entity.Myfile;
import com.sys.mapper.*;
import com.sys.service.FileService;
import com.sys.utils.BeanCopyUtils;
import com.sys.utils.FileUtil;
import com.sys.vo.AddFileVo;
import com.sys.vo.FileDataVo;
import com.sys.vo.SearchFileRequestVo;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (File)表服务实现类
 *
 * @author zrx
 * @since 2023-12-17 23:29:24
 */
@Service("fileService")
public class FileServiceImpl extends ServiceImpl<FileMapper, Myfile> implements FileService {

    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private StandardMapper standardMapper;
    @Autowired
    private GroupOrderMapper groupOrderMapper;


    //    添加文件
    @Override
    public ResponseResult addFile(MultipartFile file, AddFileVo fileVo) {

        Myfile myFile = new Myfile();
        if (fileVo.getContent() != null && !fileVo.getContent().equals("{}") ) {
            myFile.setContent(fileVo.getContent());
        } else {
            myFile.setContent(" ");
        }
        if (fileVo.getVersion() != null) {
            myFile.setVersion(fileVo.getVersion());
        } else {
            myFile.setVersion(" ");
        }
//        设置文件原始名称与下载权限
        myFile.setOriginalName(file.getOriginalFilename());
        myFile.setRole(fileVo.getRole());

//        设置分组排序id
        LambdaQueryWrapper<GroupOrder> groupOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        groupOrderLambdaQueryWrapper.eq(GroupOrder::getProjectId, fileVo.getProjectId())
                .eq(GroupOrder::getProductId, fileVo.getProductId())
                .eq(GroupOrder::getStandardId, fileVo.getStandardId());
        GroupOrder groupOrder = groupOrderMapper.selectOne(groupOrderLambdaQueryWrapper);
        myFile.setGroupId(groupOrder.getId());

        String name = "";
        if (fileVo.getFileName().equals("")) {
            name = file.getOriginalFilename();
        } else {
            name = fileVo.getFileName() + getFileExtension(file.getOriginalFilename());
        }

        myFile.setFileName(name);

        //新建对象：储存文件后的返回链接,文件二进制流,返回的文件名称
        String returnUrl = "";
        String blob = "";
        String returnName = "";

        //将MultipartFile类型的文件转为blob二进制流发送出去
        try {

            byte[] fileBytes = file.getBytes();
            blob = Base64.getEncoder().encodeToString(fileBytes);

            //发送请求链接
            if (blob != "") {
                //目标接口地址
                String url = "http://124.220.42.243:8188/saveFile";
                //设置请求数据(文件名称，在云服务器的相对路径，文件二进制流)
                JSONObject postData = new JSONObject();
                postData.put("fileName", name);
                postData.put("path", "fileUpload/data/");
                postData.put("blob", blob);
                //发送请求的对象
                RestTemplate client = new RestTemplate();
                //接收请求后的返回参数
                JSONObject json = client.postForEntity(url, postData, JSONObject.class).getBody();
                //获取对应数据(文件访问/下载链接、文件名称)
                JSONObject jsonObject = json.getJSONObject("data");
                returnUrl = jsonObject.getString("url");
                returnName = jsonObject.getString("newName");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        //将获取的文件链接与名称封装为json数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", returnUrl);
        jsonObject.put("name", returnName);
        //返回json数据


        getFileAndReturn(returnUrl);


        myFile.setUrl(returnUrl);
        myFile.setCreateTime(new Date());
        myFile.setUploadNumber(0);


        fileMapper.insert(myFile);

        return ResponseResult.okResult();
    }


    //  获取文件类型
    public static String getFileType(MultipartFile file) {

        String name = "";
        Tika tika = new Tika();
        try {
            java.io.File checkFile = FileUtil.MultipartFileToFile(file);
            name = tika.detect(checkFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return name;
    }

    //截取文件后缀
    public static String getFileExtension(String filename) {
        String extension = "";
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            extension = filename.substring(dotIndex);
        }
        return extension;
    }


    //随机数工具方法
    public static Integer getNumber(int length) {
        StringBuilder buf = new StringBuilder();
        Random random = new Random();
        /*开头不为0,建议数据量较少时只放开部分，比如1至3开头的数，等业务达到一定数量时，再逐步放开剩余的号码段，由于是固定位数，总数量一定，生成的数越多，重复的几率越大**/
        int firstNumber = random.nextInt(9) + 1;
        buf.append(firstNumber);

        for (int i = 0; i < length - 1; ++i) {
            buf.append(random.nextInt(10));
        }

        return Integer.valueOf(buf.toString());
    }

    @Override
    public ResponseResult getFile() {

        List<Myfile> files;

        files = fileMapper.selectList(null);

        JSONArray jsonArray = new JSONArray();
        //按时间，由近到远排序
        List<Myfile> sortFile = files.stream().sorted(Comparator.comparing(Myfile::getCreateTime).reversed()) // 按照创建时间降序排序
                .collect(Collectors.toList());

        for (Myfile file : sortFile) {
            String type = getFileExtension(file.getOriginalName());
            FileDataVo fileDataVo = BeanCopyUtils.copyBean(file, FileDataVo.class);
            fileDataVo.setFileType(type);
            String group = "";
            GroupOrder order = groupOrderMapper.selectById(file.getGroupId());
            group += projectMapper.selectById(order.getProjectId()).getProjectName() + "/";
            group += productMapper.selectById(order.getProductId()).getProductName() + "/";
            group += standardMapper.selectById(order.getStandardId()).getStandardName();
            fileDataVo.setProjectId(order.getProjectId());
            fileDataVo.setProductId(order.getProductId());
            fileDataVo.setStandardId(order.getStandardId());
            fileDataVo.setGroup(group);
            fileDataVo.setRole(file.getRole());
            jsonArray.add(fileDataVo);
        }


        return ResponseResult.okResult(jsonArray);
    }

    @Override
    public ResponseResult searchFile(SearchFileRequestVo searchFileRequestVo) {
        List<Myfile> files = new ArrayList<>();

        files = fileMapper.selectList(null);
        String text = searchFileRequestVo.getFileName();
        int projectId = searchFileRequestVo.getProjectId();
        int productId = searchFileRequestVo.getProductId();
        int standardId = searchFileRequestVo.getStandardId();

        //        设置分组排序id
        LambdaQueryWrapper<GroupOrder> groupOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        groupOrderLambdaQueryWrapper.eq(GroupOrder::getProjectId, projectId)
                .eq(GroupOrder::getProductId, productId)
                .eq(GroupOrder::getStandardId, standardId);
        GroupOrder groupOrder = groupOrderMapper.selectOne(groupOrderLambdaQueryWrapper);

//        筛选后的结果
        List<Myfile> filteredFiles = new ArrayList<>();
//        按关键字排序筛选并排序
        if (!text.equals("")) {
            files.removeIf(file -> !file.getFileName().contains(text));
            files.sort(Comparator.comparing(Myfile::getCreateTime).reversed());
        }
//      分组筛选
//        如果项目号为全部
        if (files.size() != 0) {

//            如果是查找全部分组数据
            if (projectId == -2 && productId ==-2 && standardId == -2) {
                filteredFiles = files;
            }

//        如果产品号为全部
            else if (projectId != -2 && productId == -2 && standardId == -2) {

                List<GroupOrder> groupOrders = new ArrayList<>();
//        查询该项目号下的所有groupOrderId，即排序id
                LambdaQueryWrapper<GroupOrder> groupWrapper = new LambdaQueryWrapper<>();
                groupWrapper.eq(GroupOrder::getProjectId, projectId);
                groupOrders = groupOrderMapper.selectList(groupWrapper);
//          判断每个file是否包含groupId数组里的数据
                for (int i = 0;i < files.size();i++) {
                    Myfile file = files.get(i);
                    boolean judge = false;
                    for (GroupOrder group : groupOrders) {
                        if (file.getGroupId() == group.getId()) {
                            judge = true;
                        }
                    }
//                检测后如果不包含，则删除
                    if (!judge) {
                        files.remove(file);
                        i -=1 ;
                    }
                }
                filteredFiles = files;
//            如果产品规格为全部
            } else if (projectId != -2 && productId != -2 && standardId == -2) {

                List<GroupOrder> groupOrders = new ArrayList<>();
//        查询该项目号下的所有groupOrderId，即排序id
                LambdaQueryWrapper<GroupOrder> groupWrapper = new LambdaQueryWrapper<>();
                groupWrapper.eq(GroupOrder::getProjectId, projectId).eq(GroupOrder::getProductId, productId);
                groupOrders = groupOrderMapper.selectList(groupWrapper);
                //          判断每个file是否包含groupId数组里的数据
                if(files.size() != 0) {
                    for (int i = 0;i < files.size();i++) {
                        Myfile file = files.get(i);
                        boolean judge = false;
                        for (GroupOrder group : groupOrders) {
                            if (file.getGroupId() == group.getId()) {
                                judge = true;
                                break;
                            }
                        }
//                检测后如果不包含，则删除
                        if (!judge) {
                            files.remove(file);
                            i -=1 ;
                        }
                    }
                }
                filteredFiles = files;
//            如果为单独筛选
            } else if (projectId != -2 && productId != -2 && standardId != -2) {
                filteredFiles = files.stream().filter(file -> file.getGroupId() == groupOrder.getId()).collect(Collectors.toList());
            }
        }else {
            filteredFiles = files;
        }


        JSONArray jsonArray = new JSONArray();
        for (Myfile file : filteredFiles) {
            String type = getFileExtension(file.getOriginalName());
            FileDataVo fileDataVo = BeanCopyUtils.copyBean(file, FileDataVo.class);
            fileDataVo.setFileType(type);
            String group = "";
            GroupOrder order = groupOrderMapper.selectById(file.getGroupId());
            group += projectMapper.selectById(order.getProjectId()).getProjectName() + "/";
            group += productMapper.selectById(order.getProductId()).getProductName() + "/";
            group += standardMapper.selectById(order.getStandardId()).getStandardName();
            fileDataVo.setGroup(group);
            fileDataVo.setProjectId(order.getProjectId());
            fileDataVo.setProductId(order.getProductId());
            fileDataVo.setRole(file.getRole());
            fileDataVo.setStandardId(order.getStandardId());
            jsonArray.add(fileDataVo);
        }

        return ResponseResult.okResult(jsonArray);
    }


    @Override
    public ResponseResult editFile(FileDataVo file) {
        Integer id = file.getId();
        Myfile editFile = fileMapper.selectById(id);

        if (editFile == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NULL);
        }

        if (file.getFileName() != null) {
            editFile.setFileName(file.getFileName());
        }
        if (file.getContent() != null) {
            editFile.setContent(file.getContent());
        }
        if (file.getVersion() != null) {
            editFile.setVersion(file.getVersion());
        }

//        设置分组
        LambdaQueryWrapper<GroupOrder> groupWrapper = new LambdaQueryWrapper<>();
        groupWrapper.eq(GroupOrder::getProjectId,file.getProjectId())
                        .eq(GroupOrder::getProductId,file.getProductId())
                                .eq(GroupOrder::getStandardId,file.getStandardId());
        GroupOrder groupOrder = groupOrderMapper.selectOne(groupWrapper);
        editFile.setGroupId(groupOrder.getId());

        editFile.setRole(file.getRole());
        editFile.setUploadNumber(file.getUploadNumber());

        fileMapper.updateById(editFile);
        return ResponseResult.okResult();
    }

    // 获取文件并返回给前端的示例方法
    public ResponseEntity<Map<String, Object>> getFileAndReturn(String fileUrl) {
        RestTemplate restTemplate = new RestTemplate();

        // 发送 GET 请求获取文件内容
        ResponseEntity<byte[]> response = restTemplate.getForEntity(fileUrl, byte[].class);

        // 将文件内容转换为 Base64 编码
        String fileContentBase64 = Base64.getEncoder().encodeToString(response.getBody());

        // 构建包含文件内容和其他数据的 JSON 对象
        Map<String, Object> responseObject = new HashMap<>();
        responseObject.put("fileContent", fileContentBase64);
        responseObject.put("otherData", "Some other data");

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 返回带有 JSON 对象和响应头的 ResponseEntity
        return new ResponseEntity<>(responseObject, headers, response.getStatusCode());
    }

    @Override
    public ResponseResult deleteFiles(List<Myfile> files) {

        for (Myfile file : files) {
            fileMapper.deleteById(file.getId());
        }

        return ResponseResult.okResult();
    }
}
