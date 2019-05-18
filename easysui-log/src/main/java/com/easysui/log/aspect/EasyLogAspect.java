package com.easysui.log.aspect;

import com.easysui.core.enums.ResultEnum;
import com.easysui.core.util.AspectUtil;
import com.easysui.core.util.JsonUtil;
import com.easysui.log.annotation.EasyLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * @author CHAO 2019/4/16
 */
@Slf4j
@Aspect
@Order(-2147483647)
public class EasyLogAspect {

    @Pointcut("@annotation(com.easysui.log.annotation.EasyLog)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object methodProcess(ProceedingJoinPoint joinPoint) {
        //注解方法
        Method sourceMethod = AspectUtil.getMethod(joinPoint);
        EasyLog easyLogAnnotation = sourceMethod.getAnnotation(EasyLog.class);
        //获取日志标识
        String title = easyLogAnnotation.title();
        //请求报文
        String text = StringUtils.left(JsonUtil.toJSON(joinPoint.getArgs()), 2000);
        String methodName = AspectUtil.getMethodName(joinPoint);
        try {
            log.info("[{}] method={}, 请求报文={}", title, methodName, text);
            Object result = joinPoint.proceed();
            log.info("[{}] method={}, 请求报文={}, 响应报文={}", title, methodName, text, JsonUtil.toJSON(result));
            return result;
        } catch (Throwable e) {
            log.error("[{}异常] method={}, 请求报文={}", title, methodName, text, e);
            //返回类型
            Class<?> returnType = AspectUtil.getReturnType(joinPoint);
            // 返回码属性域
            String codeField = easyLogAnnotation.codeField();
            // 返回信息属性域
            String msgField = easyLogAnnotation.msgField();
            return AspectUtil.buildResult(returnType, codeField, msgField, ResultEnum.INTERNAL_SERVER_ERROR.getCode(), ResultEnum.INTERNAL_SERVER_ERROR.getMsg());
        }
    }
}
