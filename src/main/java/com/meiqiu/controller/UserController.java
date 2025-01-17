package com.meiqiu.controller;

import com.meiqiu.base.BizResult;
import com.meiqiu.dto.LoginDTO;
import com.meiqiu.service.UserService;
import com.meiqiu.vo.LoginVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Description 用户接口
 * @Author sgh
 * @Date 2025/1/17
 * @Time 14:16
 */
@RestController
@RequestMapping("/api/user")
@ApiOperation(value = "用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "登录")
    @ResponseBody
    public BizResult<LoginVo> login(@RequestBody @Valid LoginDTO loginDTO) {
        return BizResult.success(userService.login(loginDTO));
    }

}
