package com.sky.aspect;
//自定义切面类,自动填充助理
import com.sky.anotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.anotation.AutoFill)")
    public void autoFill(){
    }
    //前置通知
    @Before("autoFill()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行数据填充");
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object args0 = args[0];

        LocalDateTime now = LocalDateTime.now();
        Long userId = BaseContext.getCurrentId();


        if(operationType == OperationType.INSERT)
        {
            try {
                Method setCreateTime = args0.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = args0.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = args0.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = args0.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setCreateTime.invoke(args0,now);
                setUpdateTime.invoke(args0,now);
                setCreateUser.invoke(args0,userId);
                setUpdateUser.invoke(args0,userId);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if(operationType == OperationType.UPDATE)
        {

            try {
                Method setUpdateTime = args0.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = args0.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(args0,now);
                setUpdateUser.invoke(args0,userId);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
