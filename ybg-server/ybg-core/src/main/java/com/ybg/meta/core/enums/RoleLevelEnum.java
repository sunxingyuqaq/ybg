package com.ybg.meta.core.enums;


import java.util.List;

/**
 * @author 12870
 */

public enum RoleLevelEnum {
    /**
     * superadmin
     */
    SUPER_ADMIN("super_admin", "超级管理员"),
    /**
     * superadmin
     */
    ADMIN("admin", "管理员"),
    /**
     * superadmin
     */
    USER("user", "用户"),
    /**
     * superadmin
     */
    TEST("test", "测试账号");

    private final String roleCode;

    private final String groleName;


    RoleLevelEnum(String roleCode, String groleName) {
        this.roleCode = roleCode;
        this.groleName = groleName;
    }

    /**
     * 获取性别信息
     *
     * @param roleList 角色列表
     * @return 性别名称
     */
    public static String getGenderNameByCode(List<String> roleList) {
        for (String role : roleList) {
            if ("super_admin".equals(role)) {
                return SUPER_ADMIN.groleName;
            }
            else if ("admin".equals(role)) {
                return ADMIN.groleName;
            }
            else if ("user".equals(role)) {
                return USER.groleName;
            }
            else if ("test".equals(role)) {
                return TEST.groleName;
            }
        }
        return null;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public String getGroleName() {
        return groleName;
    }
}
