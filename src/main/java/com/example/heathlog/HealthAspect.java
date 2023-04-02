package com.example.heathlog;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAccessor;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class HealthAspect {
    
    private static Logger log = LoggerFactory.getLogger(HealthAspect.class);

    @Around(value = "within(org.springframework.boot.actuate.health.HealthIndicator+) && execution(* getHealth(..))")
    public Health writeSuccessLog(ProceedingJoinPoint pjp) throws Throwable {

        Object[] args = pjp.getArgs();
        boolean includeDetails = (boolean) args[0];

        Object[] newArgs = { true };
        Health health = (Health) pjp.proceed(newArgs);

        Status status = health.getStatus();
        if(Status.DOWN.equals(status) || Status.OUT_OF_SERVICE.equals(status)) {
            log.error("{} {}", pjp.getTarget().getClass(), health);
        }

        return includeDetails ? health : new HealthAccessor(health).withoutDetails();
    }
    
}
