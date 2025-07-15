package com.oh.name_generator.global.common.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestUrlAspect {

//    @Around("execution(* *(..))")
//    //@Before("execution(* *(..))")
//    public void setRequestUrl(ProceedingJoinPoint joinPoint, Model model, HttpServletRequest request) throws Throwable {
//        controller2.home(model, request);
//        joinPoint.proceed();
//    }

//    @Before("execution(* com.project.nameMaker..*.*(..)) && args(model, request, ..)")
//    public void addCurrentUrlToModel(Model model, HttpServletRequest request) {
//        // 요청 URL을 currentUrl로 Model에 추가
//        model.addAttribute("currentUrl", request.getRequestURI());
//    }
}
