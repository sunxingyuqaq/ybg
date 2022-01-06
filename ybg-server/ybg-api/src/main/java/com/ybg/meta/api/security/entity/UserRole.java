package com.ybg.meta.api.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * describe:
 *
 * @author admin
 * @date 2021/12/29 09:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ybg_user_role")
@ApiModel(value = "UserRole对象", description = "")
public class UserRole extends Model<UserRole> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关联表主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户主键")
    private Integer userId;

    @ApiModelProperty(value = "角色主键")
    private Integer roleId;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
