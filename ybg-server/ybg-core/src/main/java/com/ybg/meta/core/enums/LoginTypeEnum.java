package com.ybg.meta.core.enums;

/**
 * @author admin
 */

public enum LoginTypeEnum {

    /**
     * 邮箱
     */
    EMAIL_LOGIN(1, "邮箱"),
    /**
     * 账号密码
     */
    USERNAME_AND_PASSWORD_LOGIN(2, "账号密码"),
    /**
     * QQ
     */
    QQ_LOGIN(3, "QQ");

    private final Integer loginTypeCode;

    private final String loginTypeName;

    LoginTypeEnum(Integer loginTypeCode, String loginTypeName) {
        this.loginTypeCode = loginTypeCode;
        this.loginTypeName = loginTypeName;
    }

    /**
     * 获取登陆方式信息
     *
     * @param loginTypeCode 方式码
     * @return 登陆方式名称
     */
    public static String getLoginTypeNameByCode(Integer loginTypeCode) {
        LoginTypeEnum[] loginTypeEnums = values();
        for (LoginTypeEnum loginTypeEnum : loginTypeEnums) {
            if (loginTypeEnum.getLoginTypeCode().equals(loginTypeCode)) {
                return loginTypeEnum.getLoginTypeName();
            }
        }
        return null;
    }

    public Integer getLoginTypeCode() {
        return loginTypeCode;
    }

    public String getLoginTypeName() {
        return loginTypeName;
    }
}
