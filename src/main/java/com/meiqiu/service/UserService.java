package com.meiqiu.service;

import com.meiqiu.dto.LoginDTO;
import com.meiqiu.vo.LoginVo;

/**
 * @Description 用户接口
 * @Author sgh
 * @Date 2025/1/17
 * @Time 14:20
 */
public interface UserService {

    LoginVo login(LoginDTO loginDTO);
}
