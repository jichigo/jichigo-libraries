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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jichigo.utility.exception.ExceptionLogger;

/**
 * Interceptor for exception logging class.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author Kazuki Shimizu
 */
@Aspect
public class ExceptionHandlerLoggingInterceptor {

    /**
     * Exception logger.
     */
    private ExceptionLogger exceptionLogger = new ExceptionLogger();

    /**
     * Inject any exception logger.
     * 
     * @param exceptionLogger any exception logger.
     */
    public void setExceptionLogger(final ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }

    /**
     * Log exception handling.
     * 
     * @param proceedingJoinPoint proceeding join point.
     * @return return object of exception handling.
     * @throws Throwable if occur error.
     */
    @Around("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public Object logException(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object returnObj = null;
        try {
            for (final Object arg : proceedingJoinPoint.getArgs()) {
                if (arg instanceof Exception) {
                    exceptionLogger.warn((Exception) arg);
                    break;
                }
            }
            returnObj = proceedingJoinPoint.proceed();
        } catch (final Exception e) {
            exceptionLogger.error(e);
            throw e;
        }
        return returnObj;
    }

}