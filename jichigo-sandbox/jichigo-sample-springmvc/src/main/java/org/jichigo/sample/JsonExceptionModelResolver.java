package org.jichigo.sample;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jichigo.springframework.webmvc.exception.ExceptionModelResolver;

public class JsonExceptionModelResolver implements ExceptionModelResolver {

    @Override
    public Object resolveModel(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        GenericJsonBean bean = new GenericJsonBean();
        bean.setResultCode("99");
        bean.setMessage(ex.getMessage());
        return bean;
    }

}
