package com.jiayun.erp.wms.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* com.jiayun.erp.wms.controller.*.*(..)) || @annotation(com.jiayun.erp.wms.util.Logging)")
    public void logging() {}

    @Before("logging()")
    public void doBefore(JoinPoint joinPoint) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        String paramStr = null;
        try {
            paramStr = mapper.writeValueAsString(joinPoint.getArgs());
        }catch (Exception e){
            paramStr = "request param JSON parse error -> " + e.getMessage();
        }
        if(joinPoint.getArgs() != null){
            log.info("Request method: {}, Request param: {}", joinPoint.getSignature(), paramStr);
        }
    }

    @AfterReturning(pointcut = "logging()", returning = "result")
    public void afterReturning(Object result) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        log.info("Response result: {}", mapper.writeValueAsString(result));
    }
}
