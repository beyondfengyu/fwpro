package com.cus.wob.constants;

/**
 * @author Andy  2016/1/8.
 * @description
 */
public class ErrorCode {

    /**
     * 客户端请求与语法错误
     */
    public static final int REQUEST_ERROR = 400;

    /**
     * 服务器收到请求，但是拒绝提供服务
     */
    public static final int REQUEST_FORBIDDEN = 403;

    /**
     * 未找到资源
     */
    public static final int NOT_FOUND = 404;

    /**
     * 服务器发生了不可预期的错误
     */
    public static final int SERVER_ERROR = 500;

    /**
     * 服务器当前不能处理客户端的请求
     */
    public static final int SERVER_UNAVAILABLE = 503;

    /**
     * 还未登录
     */
    public static final int NOT_LOGIN = 20000;

    /**
     * 缺少参数或参数值不正确
     */
    public static final int ERROR_NULL_ARGU = 20001;

    /**
     * 图片验证码校验错误
     */
    public static final int ERROR_IMG_CODE = 20002;


    public static final int EXIST_CHILDREN = 30001;

    public static final int EXIST_DOCUMENT = 30002;

}
