package com.meiqiu.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 返回结构封装
 * @Author sgh
 * @Date 2025/1/17
 * @Time 14:16
 */
@Data
@NoArgsConstructor
@ApiModel
public class BizResult<T> implements Serializable {

    @ApiModelProperty("返回状态码")
    private String code;

    @ApiModelProperty("描述信息")
    private String msg;

    @ApiModelProperty("成功标识")
    private Boolean success;

    private T data;

    private Object ref;

    public static <T> BizResult<T> result(String code, String msg) {
        BizResult<T> result = new BizResult<T>();
        result.setCode(code);
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }

    public static <T> BizResult<T> success() {
        return success(null);
    }

    public static <T> BizResult<T> success(T data) {
        ResultCode rce = ResultCode.SUCCESS;
        return result(rce, data, true);
    }

    public static <T> BizResult<T> judge(T data) {
        if (null == data) {
            return failed();
        }
        return success(data);
    }

    public static <T> BizResult<T> failed() {
        return result(ResultCode.SYSTEM_ERROR.getCode(), ResultCode.SYSTEM_ERROR.getMsg(), null, false);
    }

    public static <T> BizResult<T> failed(String msg) {
        return result(ResultCode.SYSTEM_ERROR.getCode(), msg, null, false);
    }

    public static <T> BizResult<T> judge(boolean status) {
        if (status) {
            return success();
        } else {
            return failed();
        }
    }

    public static <T> BizResult<T> failed(ResultCode resultCode) {
        return result(resultCode.getCode(), resultCode.getMsg(), null, false);
    }

    private static <T> BizResult<T> result(ResultCode resultCode, T data, Boolean success) {
        return result(resultCode.getCode(), resultCode.getMsg(), data, success);
    }

    private static <T> BizResult<T> result(String code, String msg, T data, Boolean success) {
        BizResult<T> result = new BizResult<T>();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        result.setSuccess(success);
        return result;
    }


}
