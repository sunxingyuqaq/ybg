package com.ybg.meta.api.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author nanjustar
 * @since 2021-11-13
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("ybg_menu_role")
@ApiModel(value = "MenuRole对象", description = "")
public class MenuRole extends Model<MenuRole> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "菜单id")
    private Integer menuId;

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    public MenuRole(Integer menuId, Integer roleId) {
        this.menuId = menuId;
        this.roleId = roleId;
    }

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
