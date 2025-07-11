package com.payment_service.payment_service.aspect;

import com.payment_service.payment_service.threadLocal.CorrelationIdHolder;
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
        String correlationId = CorrelationIdHolder.getCorrelationId();

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
