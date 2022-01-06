package com.ybg.meta.business.impl.security;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ybg.meta.api.security.api.MenuService;
import com.ybg.meta.api.security.dto.MenuBackDto;
import com.ybg.meta.api.security.dto.MenuRouterDto;
import com.ybg.meta.api.security.entity.Menu;
import com.ybg.meta.api.security.entity.MenuRole;
import com.ybg.meta.api.security.entity.User;
import com.ybg.meta.api.security.entity.UserRole;
import com.ybg.meta.api.security.vo.MenuConditionVo;
import com.ybg.meta.api.security.vo.MenuVo;
import com.ybg.meta.business.holder.CurrentUserHolder;
import com.ybg.meta.core.constant.CommonConst;
import com.ybg.meta.core.constant.RedisConst;
import com.ybg.meta.core.constant.ServiceErrorConst;
import com.ybg.meta.core.utils.BeanCopyUtil;
import com.ybg.meta.core.utils.RedisCache;
import com.ybg.meta.mapper.mapper.security.MenuMapper;
import com.ybg.meta.mapper.mapper.security.MenuRoleMapper;
import com.ybg.meta.mapper.mapper.security.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nanjustar
 * @since 2021-11-13
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private CurrentUserHolder currentUserHolder;
    @Autowired
    private MenuRoleMapper menuRoleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<MenuRouterDto> listMenuRouter() {
        /*------------------------  查询当前用户菜单信息开始  -----------------------------*/
        List<Integer> roleIds = listConsumerRoleIds();
        List<Integer> menuIds = listRoleMenuIds(roleIds);
        /*------------------------  查询当前用户菜单信息完成  -----------------------------*/
        /*------------------------  查询父级菜单路由信息开始  -----------------------------*/
        List<Menu> menus = baseMapper.selectList(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, CommonConst.MENU_LAYOUT_ID)
                .eq(Menu::getIsHidden, CommonConst.NOT_HIDDEN)
                .eq(Menu::getIsDisable, CommonConst.NOT_DISABLE)
                .eq(Menu::getDelFlag, CommonConst.NOT_DELETE)
                .in(Menu::getMenuId, menuIds)
                .orderByAsc(Menu::getOrderNum));
        /*------------------------  查询父级菜单路由信息完成  -----------------------------*/
        /*------------------------  查询子级菜单路由信息完成  -----------------------------*/
        List<MenuRouterDto> menuRouterDtos = new ArrayList<>();
        menus.forEach(menu -> {
            MenuRouterDto menuRouterDto = BeanCopyUtil.copyObject(menu, MenuRouterDto.class);
            List<Menu> childrenMenu = baseMapper.selectList(new LambdaQueryWrapper<Menu>()
                    .eq(Menu::getParentId, menu.getMenuId())
                    .eq(Menu::getIsHidden, CommonConst.NOT_HIDDEN)
                    .eq(Menu::getIsDisable, CommonConst.NOT_DISABLE)
                    .eq(Menu::getDelFlag, CommonConst.NOT_DELETE)
                    .in(Menu::getMenuId, menuIds)
                    .orderByAsc(Menu::getOrderNum));
            menuRouterDto.setChildren(BeanCopyUtil.copyList(childrenMenu, MenuRouterDto.class));
            menuRouterDtos.add(menuRouterDto);
        });
        /*------------------------  查询子级菜单路由信息完成  -----------------------------*/
        return menuRouterDtos;
    }

    @Override
    public List<MenuBackDto> listChildrenMenuById(Integer id) {
        /*------------------------  查询子菜单信息开始  -----------------------------*/
        List<Menu> childrenMenu = baseMapper.selectList(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, id)
                .eq(Menu::getDelFlag, CommonConst.NOT_DELETE));
        List<MenuBackDto> list = new ArrayList<>();
        childrenMenu.forEach(children -> {
            MenuBackDto menuBackDto = BeanCopyUtil.copyObject(children, MenuBackDto.class);
            menuBackDto.setHasChildren(false);
            list.add(menuBackDto);
        });
        /*------------------------  查询子菜单信息完成  -----------------------------*/
        return list;
    }


    @Override
    public List<MenuBackDto> listParentMenu() {
        /*------------------------  开始查询父级菜单信息  -----------------------------*/
        List<Menu> menus = baseMapper.selectList(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, CommonConst.MENU_LAYOUT_ID)
                .eq(Menu::getDelFlag, CommonConst.NOT_DELETE)
                .orderByAsc(Menu::getOrderNum));
        /*------------------------  查询父级菜单信息完成  -----------------------------*/
        /*------------------------  检测父级菜单是否含有子菜单开始  -----------------------------*/
        List<MenuBackDto> parentMenu = BeanCopyUtil.copyList(menus, MenuBackDto.class);
        parentMenu.forEach(parent -> parent.setHasChildren(checkMenuIsHasChildren(parent.getMenuId())));
        /*------------------------  检测父级菜单是否含有子菜单完成  -----------------------------*/
        return parentMenu;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMenu(MenuVo menuVo) {
        /*------------------------  开始查重菜单信息是否存在  -----------------------------*/
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getMenuName, menuVo.getMenuName())
                .eq(Menu::getPath, menuVo.getPath())
                .eq(Menu::getParentId, menuVo.getParentId()));
        /*------------------------  查重菜单信息是否存在完成  -----------------------------*/
        /*------------------------  开始新增菜单信息  -----------------------------*/
        int insert = baseMapper.insert(BeanCopyUtil.copyObject(menuVo, Menu.class));
        /*------------------------  新增菜单信息完成  -----------------------------*/
        /*------------------------  更新菜单角色关联表信息  -----------------------------*/
        Menu menu = baseMapper.selectOne(new LambdaQueryWrapper<Menu>().eq(Menu::getMenuName, menuVo.getMenuName()));
        int menuRoleInsert = menuRoleMapper.insert(new MenuRole(menu.getMenuId(), 1));
        /*------------------------  更新菜单角色关联表信息  -----------------------------*/
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMenu(MenuVo menuVo) {
        /*------------------------  开始查重菜单信息是否存在  -----------------------------*/
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getMenuName, menuVo.getMenuName())
                .eq(Menu::getPath, menuVo.getPath())
                .eq(Menu::getParentId, menuVo.getParentId()));
        /*------------------------  查重菜单信息是否存在完成  -----------------------------*/
        /*------------------------  修改新增菜单信息  -----------------------------*/
        int update = baseMapper.updateById(BeanCopyUtil.copyObject(menuVo, Menu.class));
        /*------------------------  修改菜单信息完成  -----------------------------*/
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteMenu(Integer id) {
        /*------------------------  检测子级菜单信息开始  -----------------------------*/
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, id)
                .eq(Menu::getDelFlag, CommonConst.NOT_DELETE));
        if (count > 0) {
            throw new RuntimeException(ServiceErrorConst.EXISTS_CHILD_ELEMENT);
        }
        /*------------------------  检测子级菜单信息完成  -----------------------------*/
        /*------------------------  开始删除菜单信息  -----------------------------*/
        int delete = baseMapper.deleteById(id);
        /*------------------------  删除菜单信息完成  -----------------------------*/
        /*------------------------  删除菜单角色对应信息  -----------------------------*/
        int menuRoleDelete = menuRoleMapper.delete(new LambdaQueryWrapper<MenuRole>()
                .eq(MenuRole::getMenuId, id));
        /*------------------------  删除菜单角色对应信息  -----------------------------*/
    }

    @Override
    public List<MenuBackDto> listMenuByCondition(MenuConditionVo menuConditionVo) {
        /*------------------------  开始查询菜单信息  -----------------------------*/
        List<Menu> menus = baseMapper.selectList(new LambdaQueryWrapper<Menu>()
                .like(StrUtil.isNotEmpty(menuConditionVo.getMenuName()), Menu::getMenuName, menuConditionVo.getMenuName())
                .eq(ObjectUtil.isNotNull(menuConditionVo.getIsDisable()), Menu::getIsDisable, menuConditionVo.getIsDisable()));
        /*------------------------  查询菜单信息完成  -----------------------------*/
        /*------------------------  数据转化开始  -----------------------------*/
        List<MenuBackDto> list = new ArrayList<>();
        menus.forEach(menu -> {
            MenuBackDto menuBackDto = BeanCopyUtil.copyObject(menu, MenuBackDto.class);
            menuBackDto.setHasChildren(false);
            list.add(menuBackDto);
        });
        /*------------------------  数据转化完成  -----------------------------*/
        return list;
    }

    /**
     * 获取当前登陆用户的角色id集合
     *
     * @return 角色id集合
     */
    private List<Integer> listConsumerRoleIds() {
        /*------------------------  获取当前用户角色信息开始  -----------------------------*/
        User consumer = currentUserHolder.getCurrentUser();
        return userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>()
                        .eq(UserRole::getUserId, consumer.getUserId()))
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        /*------------------------  获取当前用户角色信息完成  -----------------------------*/
    }

    /**
     * 根据角色ids获取菜单ids
     *
     * @param roleIds 角色ids
     * @return 菜单ids
     */
    private List<Integer> listRoleMenuIds(List<Integer> roleIds) {
        return menuRoleMapper.selectList(new LambdaQueryWrapper<MenuRole>()
                        .in(MenuRole::getRoleId, roleIds))
                .stream()
                .map(MenuRole::getMenuId)
                .collect(Collectors.toList());
    }

    /**
     * 检查菜单是否含有子菜单
     *
     * @param id 菜单id
     * @return true 含有 false 不含有
     */
    private Boolean checkMenuIsHasChildren(Integer id) {
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, id)
                .eq(Menu::getIsHidden, CommonConst.NOT_HIDDEN)
                .eq(Menu::getIsDisable, CommonConst.NOT_DISABLE)
                .eq(Menu::getDelFlag, CommonConst.NOT_DELETE));
        return count != null && count > 0;
    }


    /**
     * 查询redis中是否含有菜单路由
     *
     * @return true 含有    false 不含有
     */
    public List<MenuRouterDto> checkMenuRouterFromRedis() {
        List<MenuRouterDto> cacheList = redisCache.getCacheList(RedisConst.MENU_ROUTER_LIST);
        System.out.println(cacheList);
        if (CollUtil.isNotEmpty(cacheList)) {
            return cacheList;
        }
        return null;
    }
}
