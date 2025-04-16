package com.example.matdongsan.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.example.matdongsan..*Controller.*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.info("[Request] {} args={}", methodName, toJson(args));

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            log.info("[Response] {} result={} ({}ms)", methodName, toJson(result), (end - start));
            return result;
        } catch (Throwable t) {
            long end = System.currentTimeMillis();
            log.error("[Exception] {} - {} ({}ms)", methodName, t.getClass(), (end - start));
            throw t;
        }
    }

    private String toJson(Object obj) {
        try {
            if (obj == null) return "null";
            if (obj.getClass().isArray()) {
                return objectMapper.writeValueAsString(Arrays.asList((Object[]) obj));
            }
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }
}
