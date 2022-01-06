package com.ybg.meta.mapper.mapper.security;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ybg.meta.api.security.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2021-11-13
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取菜单权限列表
     *
     * @param id
     * @return
     */
    List<Menu> findListByUserId(@Param("id") Integer id);

}
