package com.example.j5ee.aspect;

import com.example.j5ee.common.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/***
 * @author Urmeas
 * @date 2022/10/2 11:53 上午
 */
@Aspect
@Component
public class ResultAspect {
   @Around("execution(public * com.example.j5ee.controller.*.*(..))")
   public Result doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

      Object object = proceedingJoinPoint.proceed();
      return Result.success(object);
   }
}
