package org.jichigo.springframework.webmvc.exception.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExceptionModelResolver {

    Object resolveModel(HttpServletRequest request, HttpServletResponse response, Exception ex);

}
