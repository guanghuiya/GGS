package com.meiqiu.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 用户返回实体
 * @Author sgh
 * @Date 2025/1/17
 * @Time 14:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiOperation(value = "用户返回实体")
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String phone;

}
