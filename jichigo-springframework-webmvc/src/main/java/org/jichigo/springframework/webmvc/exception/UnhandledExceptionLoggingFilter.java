package org.jichigo.springframework.webmvc.exception;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jichigo.utility.exception.ExceptionLogger;
import org.springframework.web.filter.GenericFilterBean;

public class UnhandledExceptionLoggingFilter extends GenericFilterBean {
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
            chain.doFilter(request, response);
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
