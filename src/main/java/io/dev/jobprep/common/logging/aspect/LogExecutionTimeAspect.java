package io.dev.jobprep.common.logging.aspect;

import io.dev.jobprep.common.logging.aspect.annotation.LogExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogExecutionTimeAspect {

    @Around("@annotation(logExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogExecutionTime logExecutionTime) throws Throwable {
        long startTime = System.currentTimeMillis();

        // Executing...
        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            log.info("[{}] executing time = {}ms", joinPoint.getSignature().toShortString(), executionTime);
        }
        return result;
    }

}
