package com.meiqiu.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 登录返回实体
 * @Author sgh
 * @Date 2025/1/17
 * @Time 14:28
 */
@Data
@ApiOperation(value = "登录返回实体")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "用户信息")
    private UserVo user;
}
