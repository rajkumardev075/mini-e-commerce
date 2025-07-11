package com.order_service.order_service.aspect;

import com.order_service.order_service.threadLocalStore.CorrelationIdHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Service *)")
    public void loggableMethods() {}

    @Around("loggableMethods()")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String correlationId = CorrelationIdHolder.get();

        log.info("START [{}] - Correlation ID: {}", methodName, correlationId);

        try {
            Object result = joinPoint.proceed();
            log.info("END [{}] - Correlation ID: {}", methodName, correlationId);
            return result;
        } catch (Throwable ex) {
            log.error("ERROR [{}] - Correlation ID: {} - Exception: {}", methodName, correlationId, ex.getMessage());
            throw ex;
        }
    }
}
