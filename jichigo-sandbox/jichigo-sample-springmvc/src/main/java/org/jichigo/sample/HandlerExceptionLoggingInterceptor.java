package org.jichigo.sample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jichigo.utility.exception.ExceptionLogger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class HandlerExceptionLoggingInterceptor extends HandlerInterceptorAdapter {

    private ExceptionLogger exceptionLogger;

    public void setExceptionLogger(final ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
            final Object handler, final Exception ex) throws Exception {
        if (ex != null) {
            exceptionLogger.log(ex);
        }
    }

}