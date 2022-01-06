package com.ybg.meta.core.result.advice;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.alibaba.fastjson.JSONException;
import com.ybg.meta.core.enums.HttpResponseEnum;
import com.ybg.meta.core.enums.ResponseEnum;
import com.ybg.meta.core.exception.CodeBaseException;
import com.ybg.meta.core.result.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * describe:
 *
 * @author admin
 * @date 2021/11/24 16:05
 */
@Slf4j
@RestControllerAdvice
public class UnifiedExceptionHandler {
    private static final String ENV_PROD = "prod";
    private final MessageSource messageSource;
    private final Boolean isProd;

    public UnifiedExceptionHandler(@Value("${spring.profiles.active:dev}") final String activeProfile, final MessageSource messageSource) {
        this.messageSource = messageSource;
        this.isProd = new HashSet<>(Arrays.asList(activeProfile.split(","))).contains(ENV_PROD);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestPartException.class
    })
    public ResponseResult<Object> badRequestException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler({
            NoHandlerFoundException.class
    })
    public ResponseResult<Object> noHandlerFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.fail(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class
    })
    public ResponseResult<Object> httpRequestMethodNotSupportedException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
    }

    @ExceptionHandler({
            HttpMediaTypeNotAcceptableException.class
    })
    public ResponseResult<Object> httpMediaTypeNotAcceptableException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.fail(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage());
    }

    @ExceptionHandler({
            HttpMediaTypeNotSupportedException.class
    })
    public ResponseResult<Object> httpMediaTypeNotSupportedException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.fail(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), e.getMessage());
    }

    @ExceptionHandler({
            AsyncRequestTimeoutException.class
    })
    public ResponseResult<Object> asyncRequestTimeoutException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.fail(HttpResponseEnum.REQUEST_TIMEOUT);
    }

    @ExceptionHandler({
            MissingPathVariableException.class,
            HttpMessageNotWritableException.class,
            ConversionNotSupportedException.class,
    })
    public ResponseResult<Object> handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.fail(HttpResponseEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler({
            BindException.class
    })
    public ResponseResult<Object> handleBindException(BindException e) {
        log.error("参数绑定异常", e);
        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 参数校验异常，将校验失败的所有异常组合成一条错误信息
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseResult<Object> handleValidException(MethodArgumentNotValidException e) {
        log.error("参数校验异常", e);
        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 包装绑定异常结果
     */
    private ResponseResult<Object> wrapperBindingResult(BindingResult bindingResult) {
        final List<String> errorDesc = new ArrayList<>();
        for (ObjectError error : bindingResult.getAllErrors()) {
            final StringBuilder msg = new StringBuilder();
            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
            errorDesc.add(msg.toString());
        }
        final String desc = isProd ? getLocaleMessage(HttpStatus.BAD_REQUEST.value(), "") : String.join(", ", errorDesc);
        return ResponseResult.fail(HttpStatus.BAD_REQUEST.value(), desc);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(value = CodeBaseException.class)
    public ResponseResult<Object> handleBusinessException(CodeBaseException e) {
        log.error("业务异常：" + e.getMessage(), e);
        final ResponseEnum anEnum = e.getAnEnum();
        return ResponseResult.fail(anEnum.getCode(), anEnum.getMessage());
    }

    /**
     * 未定义异常
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseResult<Object> handleExceptionInternal(Exception e) {
        log.error("未捕捉异常：" + e.getMessage(), e);
        final Integer code = HttpResponseEnum.SYSTEM_ERROR.getCode();
        return ResponseResult.fail(code, getLocaleMessage(code, e.getMessage()));
    }

    /**
     * json异常
     */
    @ExceptionHandler(value = JSONException.class)
    public ResponseResult<Object> handleJsonException(JSONException e) {
        log.error("json异常：" + e.getMessage(), e);
        return ResponseResult.fail(HttpResponseEnum.JSON_ERROR_EXCEPTION);
    }

    /**
     * token异常
     */
    @ExceptionHandler(value = NotLoginException.class)
    public ResponseResult<Object> handleNotLoginException(NotLoginException nle) {
        log.error("未登录异常：" + nle.getMessage(), nle);
        String message;
        Integer code = null;
        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未提供token";
            code = HttpResponseEnum.TOKEN_NOT_EXISTS.getCode();
        }
        else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "token无效";
            code = HttpResponseEnum.TOKEN_INVALID.getCode();
        }
        else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "token已过期";
            code = HttpResponseEnum.TOKEN_TIMEOUT.getCode();
        }
        else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "token已被顶下线";
            code = HttpResponseEnum.TOKEN_BE_REPLACED.getCode();
        }
        else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
            message = "token已被踢下线";
            code = HttpResponseEnum.TOKEN_KICK_OUT.getCode();
        }
        else {
            message = "当前会话未登录";
            code = HttpResponseEnum.NOT_LOGIN.getCode();
        }
        return ResponseResult.fail(code, message);
    }

    @ExceptionHandler(value = {NotRoleException.class, NotPermissionException.class})
    public ResponseResult<Object> handleNotLoginException() {
        return ResponseResult.fail(HttpResponseEnum.USER_OPT_NOT_PERMIT);
    }

    private String getLocaleMessage(Integer code, String defaultMsg) {
        try {
            return messageSource.getMessage("" + code, null, defaultMsg, LocaleContextHolder.getLocale());
        }
        catch (Throwable t) {
            log.warn("本地化异常消息发生异常: {}", code);
            return defaultMsg;
        }
    }
}
