package com.ybg.meta.api.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author nanjustar
 * @since 2021-12-04
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("ybg_login_log")
@ApiModel(value = "LoginLog对象", description = "")
public class LoginLog extends Model<LoginLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "logId", type = IdType.AUTO)
    private Integer logId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户ip")
    private String ipAddress;

    @ApiModelProperty(value = "用户地址")
    private String ipSource;

    @ApiModelProperty(value = "浏览器")
    private String brower;

    @ApiModelProperty(value = "操作系统")
    private String os;

    @ApiModelProperty(value = "登陆状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @Override
    public Serializable pkVal() {
        return this.logId;
    }

}
