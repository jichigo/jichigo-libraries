package org.jichigo.sample;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jichigo.utility.exception.ExceptionLogger;

@Aspect
public class ExceptionHandlerLoggingInterceptor {

    private ExceptionLogger exceptionLogger;

    public void setExceptionLogger(final ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }

    @Around("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public Object loggingException(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object returnObj = null;
        try {
            returnObj = proceedingJoinPoint.proceed();
            for (Object arg : proceedingJoinPoint.getArgs()) {
                if (arg instanceof Exception) {
                    exceptionLogger.log((Exception) arg);
                }
            }
        } catch (final Exception e) {
            exceptionLogger.log(e);
            throw e;
        }
        return returnObj;
    }

}
