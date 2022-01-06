package com.ybg.meta.business.impl.security;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybg.meta.api.security.api.RoleService;
import com.ybg.meta.api.security.entity.Role;
import com.ybg.meta.mapper.mapper.security.RoleMapper;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
