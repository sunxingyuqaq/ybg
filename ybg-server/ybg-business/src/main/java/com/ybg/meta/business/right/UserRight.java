package com.ybg.meta.business.right;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ybg.meta.api.security.entity.Menu;
import com.ybg.meta.api.security.entity.Role;
import com.ybg.meta.api.security.entity.UserRole;
import com.ybg.meta.mapper.mapper.security.MenuMapper;
import com.ybg.meta.mapper.mapper.security.RoleMapper;
import com.ybg.meta.mapper.mapper.security.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * describe:
 *
 * @author admin
 * @date 2021/12/29 14:30
 */
@Component
public class UserRight implements StpInterface {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<Menu> menus = menuMapper.findListByUserId(Integer.parseInt(loginId.toString()));
        return menus.stream().map(Menu::getPath).collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<UserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, loginId));
        if (CollUtil.isNotEmpty(userRoles)) {
            List<Integer> list = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            List<Role> roles = roleMapper.selectBatchIds(list);
            return roles.stream().map(Role::getRoleKey).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
