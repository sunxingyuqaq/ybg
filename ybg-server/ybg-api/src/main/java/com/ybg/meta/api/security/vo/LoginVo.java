package com.ybg.meta.api.security.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author admin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {

    @NotBlank(message = "用户名不能为空！")
    @ApiModelProperty(value = "用户名", name = "username", required = true, dataType = "String")
    private String username;

    @NotBlank(message = "密码不能为空！")
    @ApiModelProperty(value = "密码", name = "password", required = true, dataType = "String")
    private String password;

    @NotBlank(message = "验证码不能为空！")
    @ApiModelProperty(value = "验证码", name = "captcha", required = true, dataType = "String")
    private String captcha;

    @ApiModelProperty(value = "是否需要记住我", name = "rememberMe", dataType = "Boolean")
    private Boolean rememberMe;
}
