package com.meiqiu.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiqiu.base.ResultCode;
import com.meiqiu.base.ServiceException;
import com.meiqiu.dto.LoginDTO;
import com.meiqiu.dto.RegisterUserDTO;
import com.meiqiu.entity.User;
import com.meiqiu.mapper.UserMapper;
import com.meiqiu.service.UserService;
import com.meiqiu.vo.LoginVo;
import com.meiqiu.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


/**
 * @Description 用户接口实现类
 * @Author sgh
 * @Date 2025/1/17
 * @Time 14:20
 */

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public static final String TOKEN_PREFIX = "token:";

    /**
     * 登录
     */
    @Override
    public LoginVo login(LoginDTO loginDTO) {
        try {
            //用户数据
            User user = userMapper.queryByPhone(loginDTO.getPhone());
            if (user == null) {
                throw new ServiceException(ResultCode.USER_ERROR);
            }
            //将密码进行md5加密,比对
            String md5Str = DigestUtil.md5Hex(loginDTO.getPassword());
            if (!md5Str.equals(user.getPassword())) {
                throw new ServiceException(ResultCode.USER_ERROR);
            }
            //生成 token
            String token = initToken(user.getPhone());
            //封装返回数据
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            return LoginVo.builder()
                    .token(token)
                    .user(userVo)
                    .build();
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("登录失败", e);
            throw new ServiceException("登录失败：" + e.getMessage());
        }
    }

    /**
     * 生成 token
     */
    private String initToken(String phone) {
        String token = String.valueOf(System.currentTimeMillis());
        redisTemplate.opsForValue().set(TOKEN_PREFIX + phone, token, 10L);
        return token;
    }

    /**
     * 注册
     */
    @Override
    public LoginVo register(RegisterUserDTO request) {
        try {
            //校验手机号
            User user = userMapper.queryByPhone(request.getPhone());
            if (user != null) {
                throw new ServiceException(ResultCode.USER_EXIST);
            }
            //保存用户信息
            user = new User();
            BeanUtils.copyProperties(request, user);
            user.setPassword(DigestUtil.md5Hex(request.getPassword()));
            user.setCreateUser(1);
            user.setUpdateUser(1);
            userMapper.save(user);

            //生成 token
            String token = initToken(user.getPhone());
            //封装 vo
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            return LoginVo.builder()
                    .token(token)
                    .user(userVo)
                    .build();
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("注册失败", e);
        }
        return null;
    }
}
