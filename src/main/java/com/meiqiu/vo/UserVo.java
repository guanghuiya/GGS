package com.meiqiu.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    @ApiModelProperty("主键 id")
    private Integer id;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("手机号（登录账号）")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private Integer createUser;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("更新人")
    private Integer updateUser;

    @ApiModelProperty("是否删除 0：否 1：是")
    private Integer isDelete;

}
