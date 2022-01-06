package com.ybg.meta.business.impl.security;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybg.meta.api.security.api.MenuRoleService;
import com.ybg.meta.api.security.entity.MenuRole;
import com.ybg.meta.mapper.mapper.security.MenuRoleMapper;
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
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements MenuRoleService {

}
