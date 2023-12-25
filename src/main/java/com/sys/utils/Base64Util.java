package com.sys.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;


//      Base64与文件 互相转换 , 以及对文件的创建及删除 的工具类
public class Base64Util {

    //文件 转 base64二进制流
    public static String file2Str(String filePath) {

        //定义输出流对象
        InputStream in = null;
        byte[] data = null;
        // 读取文件字节数组
        try {
            in = new FileInputStream(filePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert in != null;
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回 Base64 编码过的字节数组字符串
        assert data != null;
        return encoder.encode(data);
    }

    //base64二进制流 转 文件
    public static boolean str2File(String base64FileStr, String filePath,String fileName) {

        // 数据为空
        if (base64FileStr == null) {
            return false;
        }

        //判断文件夹是否存在
        File outputFile = new File(filePath,fileName);
        File fileP = outputFile.getParentFile();
        if (!fileP.exists() && !fileP.mkdirs() && !fileP.mkdir()) {
            throw new IllegalArgumentException("创建文件目录失败");
        }

        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {
            // Base64解码,对字节数组字符串进行Base64解码并生成文件
            byte[] byt = decoder.decodeBuffer(base64FileStr);
            for (int i = 0, len = byt.length; i < len; ++i) {
                // 调整异常数据
                if (byt[i] < 0) {
                    byt[i] += 256;
                }
            }
            InputStream input = new ByteArrayInputStream(byt);
            // 生成指定格式的文件
            out = new FileOutputStream(outputFile);
            byte[] buff = new byte[1024];
            int len;
            while ((len = input.read(buff)) != -1) {
                out.write(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert out != null;
                out.flush();
                out.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    //创建目录
    public static boolean createMkdirs(String filePath,String fileName){
        File file = new File(filePath,fileName);
        file.mkdirs();
        return true;
    }

    //删除文件(传入父路径，子路径)
    public static boolean deleteFile(String filePath,String fileName){
        File file = new File(filePath,fileName);
        if (!file.exists()){
            return false;
        }

        try {
            file.delete();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    //删除文件(传入整个路径)
    public static boolean deleteFile(String filePath){
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
