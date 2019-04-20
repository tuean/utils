package aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class AspectTemplate {


    @Pointcut("execution(public * aop.*(..))")
    private void pointcut(){}


    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        String fullClassName = signature.getDeclaringTypeName();
        String[] classes = fullClassName.split("\\.");
        String name = classes[classes.length - 1].toLowerCase();
        
        Object object = null;
        try {
            object = point.proceed(point.getArgs());
            return object;
        } catch (Throwable var) {
            throw var;
        } finally {
            // todo 
        }
    }
}
