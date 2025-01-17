package com.meiqiu.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.meiqiu.base.ResultCode;
import com.meiqiu.base.ServiceException;
import com.meiqiu.dto.LoginDTO;
import com.meiqiu.entity.User;
import com.meiqiu.service.UserService;
import com.meiqiu.vo.LoginVo;
import com.meiqiu.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description 用户接口实现类
 * @Author sgh
 * @Date 2025/1/17
 * @Time 14:20
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    /**
     * 登录
     */
    @Override
    public LoginVo login(LoginDTO loginDTO) {
        //模拟数据库用户数据
        List<User> userList = new ArrayList<>();
        String initPass = "123456";
        //将密码进行md5加密
        String md5Str = DigestUtil.md5Hex(initPass);
        for (int i = 1; i <= 10; i++) {
            User user = User.builder()
                    .id(Long.parseLong(String.valueOf(i)))
                    .nickName("nickName-" + i)
                    .phone("phone-" + i)
                    .password(md5Str)
                    .build();
            userList.add(user);
        }

        //模拟用户token数据,key为token，value为手机号
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token-phone-1-000000000001", "phone-1");
        tokenMap.put("token-phone-2-000000000002", "phone-2");
        tokenMap.put("token-phone-3-000000000003", "phone-3");

        //业务逻辑开始
        try {
            //根据用户名和加密密码查询用户信息
            String md5Pass = DigestUtil.md5Hex(loginDTO.getPassword());
            User user = userList.stream().filter(u -> u.getPhone().equals(loginDTO.getPhone())
                    && u.getPassword().equals(md5Pass)).findFirst().orElse(null);
            if (user == null) {
                throw new ServiceException(ResultCode.USER_ERROR);
            }

            //生成token，放进 token池
            String token = "token-" + System.currentTimeMillis();
            tokenMap.put(token, loginDTO.getPhone());
            //封装返回数据
            return LoginVo.builder()
                    .token(token)
                    .user(UserVo.builder()
                            .id(user.getId())
                            .nickName(user.getNickName())
                            .phone(user.getPhone())
                            .build())
                    .build();
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("登录失败", e);
            throw new ServiceException("登录失败：" + e.getMessage());
        }
    }
}
