package com.ybg.meta.business.aop;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.ybg.meta.api.system.entity.OptLog;
import com.ybg.meta.business.annotation.SysLog;
import com.ybg.meta.core.utils.IpUtils;
import com.ybg.meta.mapper.mapper.system.OptLogMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

/**
 * @author admin
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private OptLogMapper optLogMapper;

    /**
     * 日志切入点
     */
    @Pointcut("@annotation(com.ybg.meta.business.annotation.SysLog)")
    public void logPointCut() {
    }

    @Transactional(rollbackFor = Exception.class)
    @AfterReturning(value = "logPointCut()", returning = "response")
    public void saveOptLog(JoinPoint joinPoint, Object response) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        signature.getDeclaringType();
        // 获取操作
        Api api = (Api) signature.getDeclaringType().getAnnotation(Api.class);
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        SysLog sysLog = method.getAnnotation(SysLog.class);

        // 获取类名
        String className = joinPoint.getTarget().getClass().getName();
        String name = method.getName();
        String methodName = className + "." + name;

        /*------------------------  数据封装  -----------------------------*/
        OptLog optLog = OptLog.builder()
                .title(api.tags()[0])
                .businessType(sysLog.logType())
                .methodName(apiOperation.value())
                .methodUrl(methodName)
                .requestType(Objects.requireNonNull(request).getMethod())
                .optUrl(request.getRequestURI())
                .optName(StpUtil.getLoginIdAsString())
                .optIp(IpUtils.getIpAddress(request))
                .optAddress(IpUtils.getIpSource(IpUtils.getIpAddress(request)))
                .requestParam(JSON.toJSONString(joinPoint.getArgs()))
                .responseData(JSON.toJSONString(response))
                .optTime(new Date())
                .build();

        int insert = optLogMapper.insert(optLog);
        log.info(insert == 1 ? "记录日志成功！" : "记录日志失败！");
    }

}
