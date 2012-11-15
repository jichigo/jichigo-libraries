/*
 * Copyright (c) 2012 jichigo's developers team.
 *
 * jichigo's source code and binaries are distributed the MIT License.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial 
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE 
 * AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.jichigo.springframework.webmvc.exception;

import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jichigo.utility.exception.ExceptionLogger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Interceptor class for logging handled excetion by {@link ExceptionHandler} annotation.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author Kazuki Shimizu
 */
@Aspect
public class HandlerExceptionResolverLoggingInterceptor {

    /**
     * Exception logger.
     */
    private ExceptionLogger exceptionLogger = new ExceptionLogger();

    private Set<Class<? extends HandlerExceptionResolver>> resolversForWarn = new HashSet<Class<? extends HandlerExceptionResolver>>();

    /**
     * Inject any exception logger.
     * 
     * @param exceptionLogger any exception logger.
     */
    public void setExceptionLogger(final ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }

    public void setResolversForWarn(HashSet<Class<? extends HandlerExceptionResolver>> resolversForWarn) {
        this.resolversForWarn = resolversForWarn;
    }

    /**
     * Log exception handling.
     * 
     * @param proceedingJoinPoint proceeding join point.
     * @return return object of exception handling.
     * @throws Throwable if occur error.
     */
    @Around("execution(* org.springframework.web.servlet.HandlerExceptionResolver.resolveException(..))")
    public Object logException(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object returnObj = proceedingJoinPoint.proceed();
        if (returnObj != null) {
            if (resolversForWarn.contains(proceedingJoinPoint.getTarget().getClass())) {
                exceptionLogger.warn((Exception) proceedingJoinPoint.getArgs()[3]);
            } else {
                exceptionLogger.log((Exception) proceedingJoinPoint.getArgs()[3]);
            }
        }
        return returnObj;
    }

}