package org.jichigo.springframework.webmvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

public class JichioDispatcherServlet extends DispatcherServlet {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        HandlerExecutionChain handler = super.getHandler(request);
        if (handler == null) {
            throw new NoSuchRequestHandlingMethodException(request);
        }
        return handler;
    }

}
