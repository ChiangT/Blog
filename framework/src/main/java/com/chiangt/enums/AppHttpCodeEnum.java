package com.chiangt.enums;

public enum AppHttpCodeEnum {

    SUCCESS(200, "操作成功"),
    NEED_LOGIN(401, "需要登录后操作"),
    NO_OPERATION_AUTH(403,"无操作权限"),
    SYSTEM_ERROR(500,"系统出错"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503,"邮箱已存在"),
    NICKNAME_EXIST(504,"昵称已存在"),
    REQUIRE_USERNAME(505,"需填写用户名"),
    LOGIN_ERROR(506,"用户名或密码错误"),
    CONTENT_NOT_NULL(507,"评论内容不能为空"),
    FILE_TYPE_ERROR(508,"文件类型错误，请上传png"),
    USERNAME_NOT_NULL(509,"用户名不能为空"),
    NICKNAME_NOT_NULL(510,"昵称不能为空"),
    PASSWORD_NOT_NULL(511,"密码不能为空"),
    EMAIL_NOT_NULL(512,"邮箱不能为空")
    ;

    int code;
    String msg;
    AppHttpCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
