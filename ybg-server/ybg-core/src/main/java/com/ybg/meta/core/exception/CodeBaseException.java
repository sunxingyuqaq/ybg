package com.ybg.meta.core.exception;

import com.ybg.meta.core.enums.HttpResponseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * describe:
 *
 * @author admin
 * @date 2021/11/24 16:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeBaseException extends RuntimeException {
    private final HttpResponseEnum anEnum;
    private final Object[] args;
    private final String message;
    private final Throwable cause;

    public CodeBaseException(final HttpResponseEnum anEnum) {
        this(anEnum, null, anEnum.getMessage(), null);
    }

    public CodeBaseException(final HttpResponseEnum anEnum, final String message) {
        this(anEnum, null, message, null);
    }

    public CodeBaseException(final HttpResponseEnum anEnum, final Object[] args, final String message) {
        this(anEnum, args, message, null);
    }

    public CodeBaseException(final HttpResponseEnum anEnum, final Object[] args, final String message, final Throwable cause) {
        this.anEnum = anEnum;
        this.args = args;
        this.message = message;
        this.cause = cause;
    }
}
