package com.ybg.meta.core.enums;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author admin
 * @see HttpStatus
 * 可以包含上面的状态值
 * 用户相关操作20000-29999
 * 日志操作相关30000-39999
 * 菜单操作相关40000-49999
 * 系统相关异常50000-59999
 * token相关异常60000-69999
 * 接口请求相关异常70000-79999
 * 字典请求相关异常80000-89999
 */

public enum HttpResponseEnum implements ResponseEnum {
    /**
     * 操作成功
     */
    SUCCESS(20000, "操作成功！"),
    /**
     * 没有登录
     */
    NOT_LOGIN(40001, "您还没有登录！"),
    /**
     * 没有操作权限
     */
    AUTHORIZED(40003, "没有操作权限！"),
    /**
     * 操作失败，
     */
    FAIL(50000, "操作失败！"),
    /**
     * 用户名已经存在
     */
    USER_USERNAME_EXIST(20001, "用户名已经存在！"),
    /**
     * 用户不存在
     */
    USER_USERNAME_NOT_EXIST(20002, "用户不存在！"),
    /**
     * 用户名或者密码错误
     */
    USER_USERNAME_AND_PASSWORD_NOT_CORRECT(20003, "用户名或者密码错误！"),
    /**
     * 用户已经被禁用！
     */
    USER_IS_NOT_ENABLED(20004, "用户已经被禁用！"),
    /**
     * 用户已经被删除！
     */
    USER_IS_DELETED(20005, "用户已经被删除！"),
    /**
     * 账号在其他地方登陆，您将被迫下线！
     */
    USER_USER_LOGIN_IN_OTHER_PLACE(20006, "用户在其他地方登陆，您将被迫下线！"),
    /**
     * 用户权限不足！
     */
    USER_OPT_NOT_PERMIT(20007, "用户权限不足,无法操作！"),
    /**
     * 请求超时异常！
     */
    REQUEST_TIMEOUT(50001, "请求超时异常！"),
    /**
     * json格式异常！
     */
    JSON_ERROR_EXCEPTION(50002, "json格式化异常！"),
    /**
     * 系统异常！
     */
    SYSTEM_ERROR(50005, "系统异常！"),

    TOKEN_NOT_EXISTS(60000, "未提供token"),
    TOKEN_INVALID(60001, "token无效"),
    TOKEN_TIMEOUT(60002, "token已过期"),
    TOKEN_BE_REPLACED(60003, "token已被顶下线"),
    TOKEN_KICK_OUT(60004, "token已被踢下线"),
    /**
     * 传入参数不合法
     */
    VALID_ERROR(70000, "传入参数不合法！");


    private final Integer code;
    private final String message;

    private MessageSource messageSource;

    HttpResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getLocaleMessage(Object[] args) {
        return messageSource.getMessage("response.error." + code, args, message, LocaleContextHolder.getLocale());
    }

    public void setMessageSource(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Component
    public static class ReportTypeServiceInjector {
        private final MessageSource messageSource;

        public ReportTypeServiceInjector(final MessageSource messageSource) {
            this.messageSource = messageSource;
        }

        @PostConstruct
        public void postConstruct() {
            for (final HttpResponseEnum anEnum : HttpResponseEnum.values()) {
                anEnum.setMessageSource(messageSource);
            }
        }
    }
}

