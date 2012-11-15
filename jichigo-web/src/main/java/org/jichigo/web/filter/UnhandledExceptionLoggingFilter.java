package org.jichigo.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jichigo.utility.exception.ExceptionLogger;

public class UnhandledExceptionLoggingFilter extends FilterAdapter {
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        try {
            super.doFilter(request, response, chain);
        } catch (IOException e) {
            exceptionLogger.log(e);
            throw e;
        } catch (ServletException e) {
            exceptionLogger.log(e);
            throw e;
        } catch (RuntimeException e) {
            exceptionLogger.log(e);
            throw e;
        }
    }

}
