package com.example.demo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PrintExecutionTimeAspect {
    @Around("@annotation(PrintExecutionTime)")
    public Object printExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        var object = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        System.out.println("executed " + joinPoint.toShortString() + " with " + joinPoint.getArgs().length + " args in " + executionTime + "ms.");
        return object;
    }

    @Before("@annotation(PrintExecutionTime)")
    public void beforePrintExecutionTime(JoinPoint joinPoint) {
        System.out.println("before " + joinPoint.toShortString() + " with " + joinPoint.getArgs().length + " args.");
    }

    @After("@annotation(PrintExecutionTime)")
    public void afterPrintExecutionTime(JoinPoint joinPoint) {
        System.out.println("after " + joinPoint.toShortString() + " with " + joinPoint.getArgs().length + " args.");
    }

    @AfterReturning(pointcut = "@annotation(PrintExecutionTime)", returning="result")
    public void afterReturningPrintExecutionTime(JoinPoint joinPoint, Object result) {
        System.out.println("afterReturning " + joinPoint.toShortString() + " with " + joinPoint.getArgs().length + " args returning " + result.toString());
    }

    @AfterThrowing(pointcut = "@annotation(PrintExecutionTime)", throwing="ex")
    public void afterThrowingPrintExecutionTime(JoinPoint joinPoint, Exception ex) {
        System.out.println("afterThrowing " + joinPoint.toShortString() + " with " + joinPoint.getArgs().length + " args throwing " + ex.toString());
    }
}