package com.zettamine.mpa.lpm.aspects;



import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Aspect
@Slf4j
public class LoggingAspect {
	
	  @Before("execution(* com.zettamine.mpa.lpm.controller.*.*(..))") public void
	  logBeforeMethodCall(JoinPoint joinPoint) {
	  
	  log.info("Starting Execution of  - \"" + joinPoint.getSignature().getName() +
	  "()\" - with " + joinPoint.getArgs().length + " parameters"); }
	  
	  @After("execution(* com.zettamine.mpa.lpm.controller.*.*(..))") public void
	  logAfterMethodCall(JoinPoint joinPoint) { log.info("Method - \"" +
	  joinPoint.getSignature().getName() + "()\" has executed"); }
	  
	  @AfterReturning(pointcut =
	  "execution(* com.zettamine.mpa.lpm.controller.*.*(..))", returning =
	  "returnValue") public void logAfterSuccessfulMethodCall(JoinPoint joinPoint,
	  Object returnValue) { log.info("Execution Completed for \"" +
	  joinPoint.getSignature().getName() + "()\" with " +
	  joinPoint.getArgs().length + " arguments and returns - " + returnValue); }
	  
	  @AfterThrowing(pointcut =
	  "execution(* com.zettamine.mpa.lpm.controller.*.*(..))s", throwing = "ex")
	  public void logMethodCallAfterException(JoinPoint joinPoint, Exception ex) {
	  log.info(joinPoint.getSignature().getName() + "() fired an exception -> " +
	  ex.toString()); }
	 
}

