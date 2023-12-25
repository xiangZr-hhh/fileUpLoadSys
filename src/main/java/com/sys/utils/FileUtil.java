package com.sys.utils;


import org.springframework.web.multipart.MultipartFile;

import java.io.*;

// MultipartFile转为File的工具类

public class FileUtil {
	
	//将MultipartFile转化为File类的方法
	public static File MultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File toFile = null;
        if (!multipartFile.equals("") && !(multipartFile.getSize() <= 0)) {
            InputStream ins = multipartFile.getInputStream();
            toFile = new File(multipartFile.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	//删除产生的临时文件的方法
    public static void deleteTempFile(File file){
        if(file!=null){
            File delFile = new File(file.toURI());
            delFile.delete();
        }
    }
}
