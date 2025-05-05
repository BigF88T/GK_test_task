package ru.safonov.test_task.aop;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import static ru.safonov.test_task.util.ErrorUtil.returnAllErrors;

@Aspect
@Component
public class BindingResultAspect {
    @Around("@annotation(HandleBindingResult)")
    @SneakyThrows
    public Object handleBindingResult(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult bindingResult) {
                if (bindingResult.hasErrors()) {
                    return ResponseEntity
                            .badRequest()
                            .body(returnAllErrors(bindingResult));
                }
                break;
            }
        }
        return joinPoint.proceed();
    }
}
