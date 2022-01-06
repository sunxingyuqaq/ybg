package com.ybg.meta.core.enums;

/**
 * @author 12870
 */

public enum GenderEnum {
    /**
     * 性别未知
     */
    GENDER_UNKNOWN(-1, "未知"),
    /**
     * 女生
     */
    GENDER_WOMEN(0, "女生"),
    /**
     * 男生
     */
    GENDER_MAN(1, "男生");

    private final Integer genderCode;

    private final String genderName;

    GenderEnum(Integer genderCode, String genderName) {
        this.genderCode = genderCode;
        this.genderName = genderName;
    }

    /**
     * 获取性别信息
     *
     * @param genderCode 性别码
     * @return 性别名称
     */
    public static String getGenderNameByCode(Integer genderCode) {
        GenderEnum[] genderEnums = values();
        for (GenderEnum genderEnum : genderEnums) {
            if (genderEnum.getGenderCode().equals(genderCode)) {
                return genderEnum.getGenderName();
            }
        }
        return null;
    }

    public Integer getGenderCode() {
        return genderCode;
    }

    public String getGenderName() {
        return genderName;
    }
}
