package com.meiqiu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiqiu.dto.LoginDTO;
import com.meiqiu.dto.RegisterUserDTO;
import com.meiqiu.entity.User;
import com.meiqiu.vo.LoginVo;

/**
 * @Description 用户接口
 * @Author sgh
 * @Date 2025/1/17
 * @Time 14:20
 */
public interface UserService extends IService<User> {

    LoginVo login(LoginDTO loginDTO);

    LoginVo register(RegisterUserDTO request);

}
