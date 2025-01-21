package com.meiqiu.mapper;

import com.meiqiu.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author sgh
 * @since 2025-01-21 04:39:10
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> queryAll();

    User queryByPhone(@Param("phone") String phone);

    void save(@Param("user") User user);
}
