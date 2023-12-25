package com.sys.constans.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    DATA_NULL(401,"数据为空"),
    LOGIN_ERROR(402,"用户密码错误"),
    JSON_ERROR(403,"JSON数据格式错误"),
    GEOJSON_EMPTY(408,"geojson数据为空，检查mapId"),
    FILE_EMPTY(409,"文件为空");


    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}