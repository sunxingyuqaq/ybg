package com.ybg.meta.core.result.advice;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ybg.meta.core.enums.HttpResponseEnum;
import com.ybg.meta.core.result.ResponseResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * describe:
 *
 * @author admin
 * @date 2021/11/25 14:25
 */
@RestControllerAdvice
public class ResultResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@Nullable MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, @Nullable MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String path = getUri(request);
        if (body instanceof ResponseResult) {
            ResponseResult<?> newBody = (ResponseResult<?>) body;
            newBody.setPath(path);
            return newBody;
        }
        if (body instanceof String) {
            body = JSON.toJSONStringWithDateFormat(ResponseResult.success(HttpResponseEnum.SUCCESS.getMessage(), body, path),
                    "yyyy-MM-dd HH:ss:mm", SerializerFeature.WriteNullStringAsEmpty);
        }
        return body;
    }

    /**
     * 去除项目名 contextPath
     *
     * @param request r
     * @return s
     */
    public String getUri(ServerHttpRequest request) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String requestUri = servletRequest.getRequestURI();
        if (StrUtil.isNotBlank(requestUri)) {
            String contextPath = servletRequest.getContextPath();
            if (StrUtil.isNotBlank(contextPath)) {
                requestUri = requestUri.replace(contextPath, "");
            }
        }
        return requestUri;
    }
}
