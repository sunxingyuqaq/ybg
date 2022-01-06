package com.ybg.meta.api.system.vo;

import com.ybg.meta.core.page.PageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLogVo extends PageVo {
    @ApiModelProperty(value = "用户昵称", name = "nickname")
    private String nickname;

    @ApiModelProperty(value = "用户地址", name = "ipSource")
    private String ipSource;

    @ApiModelProperty(value = "状态", name = "status")
    private Integer status;

    @ApiModelProperty(value = "开始日期", name = "beginTime")
    private String beginTime;

    @ApiModelProperty(value = "结束日期", name = "endTime")
    private String endTime;
}
