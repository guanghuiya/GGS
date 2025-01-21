package com.meiqiu.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description 用户注册实体
 * @Author sgh
 * @Date 2025/1/21
 * @Time 17:31
 */
@Data
@ApiOperation(value = "用户注册实体")
public class RegisterUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("昵称")
    @NotBlank(message = "昵称不能为空！")
    private String nickName;

    @ApiModelProperty("手机号（登录账号）")
    @NotBlank(message = "手机号不能为空！")
    private String phone;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空！")
    private String password;

    @ApiModelProperty("邮箱")
    private String email;


}
