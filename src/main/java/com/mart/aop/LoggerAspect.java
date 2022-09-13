package com.mart.aop;

import com.mart.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.UUID;


@Component
@Aspect
@Slf4j
public class LoggerAspect {

    /**
     * This method intercepts all the incoming requests and create the unique correlation id if it is not provided by the client
     * @param jp
     * @return
     * @throws Throwable
     */
    @Around("(execution(* com.mart.controller..*.*(..)))")
    public Object trackingInfoAdvise(ProceedingJoinPoint jp) throws Throwable {
        try {
            HttpServletRequest request = getRequest();
            String correlationId = request.getHeader(CommonConstants.CORRELATION_ID);
            MDC.put(CommonConstants.CORRELATION_ID, StringUtils.isNotEmpty(correlationId) ? correlationId : UUID.randomUUID().toString());
            Object[] args = jp.getArgs();
            log.info(String.format("Request Received to %s for %s for %s", jp.getTarget().getClass().getCanonicalName(), jp.getSignature().getName(), Arrays.toString(args)));
            return jp.proceed();
        }catch (Throwable ex){
            log.error("Request Invalid: {}", ExceptionUtils.getStackTrace(ex));
            throw ex;
        }
    }

    /**
     * After the request is processed, the thread local variables(in MDC) are cleared
     */
    @After("(execution(* com.mart.controller..*.*(..)))")
    public void cleanupAdvise() {
        MDC.clear();
    }

    /**
     * This is used to log the time taken by each method
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.mart.annotation.ElapsedTime)")
    public Object performanceLogger(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        StopWatch stopWatch = new StopWatch(className+" -> "+methodName);
        stopWatch.start(methodName);
        Object result = joinPoint.proceed();
        stopWatch.stop();
        log.info("ElapsedTime : {} executed in {} seconds", stopWatch.getId(), stopWatch.getTotalTimeSeconds());
        return result;
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}
