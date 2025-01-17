package com.meiqiu.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description 登录实体
 * @Author sgh
 * @Date 2025/1/17
 * @Time 14:05
 */
@Data
@ApiOperation(value = "登录实体")
public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空！")
    private String phone;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空！")
    private String password;

}

