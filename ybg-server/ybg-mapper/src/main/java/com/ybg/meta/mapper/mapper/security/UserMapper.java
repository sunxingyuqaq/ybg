package com.ybg.meta.mapper.mapper.security;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ybg.meta.api.security.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * describe:
 *
 * @author admin
 * @date 2021/12/29 10:13
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
