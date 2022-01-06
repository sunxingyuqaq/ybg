package com.ybg.meta.business.impl.security;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybg.meta.api.security.api.UserRoleService;
import com.ybg.meta.api.security.entity.UserRole;
import com.ybg.meta.mapper.mapper.security.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nanjustar
 * @since 2021-11-13
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
