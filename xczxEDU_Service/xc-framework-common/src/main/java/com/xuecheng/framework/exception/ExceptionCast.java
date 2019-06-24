package com.xuecheng.framework.exception;


import com.xuecheng.framework.model.response.ResultCode;

public class ExceptionCast {
    /*封装 专门用来抛出自定义异常*/
    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }
}
