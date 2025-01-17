package com.meiqiu.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 结果状态码
 * @Author sgh
 * @Date 2025/1/17
 * @Time 14:16
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResultCode implements Serializable {

    SUCCESS("200", ""),
    FAILURE("400", "业务异常"),
    SYSTEM_ERROR("500", "系统出错"),

    USER_ERROR("00001", "用户名或密码错误"),
    ;

    private String code;

    private String msg;

    public static ResultCode getValue(String code) {
        for (ResultCode value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return SYSTEM_ERROR;
    }
}
