package com.ybg.meta.api.system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginLogDto {

    /**
     * 主键
     */
    private Integer logId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户ip
     */
    private String ipAddress;

    /**
     * 用户地址
     */
    private String ipSource;

    /**
     * 浏览器
     */
    private String brower;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登陆状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date gmtCreate;

    /**
     * 备注信息
     */
    private String remark;
}
