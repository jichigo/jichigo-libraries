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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.annotation.Aspect;
import org.jichigo.utility.exception.ExceptionMapping;
import org.jichigo.web.base.util.AcceptMimeTypeMatcher;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 * Interceptor class for logging handled excetion by {@link ExceptionHandler} annotation.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author Created By Kazuki Shimizu
 */
@Aspect
public class JichigoSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

    private final ExceptionMapping<Integer> responseCodeEceptionMapping;

    private final AcceptMimeTypeMatcher acceptMimeTypeMatcher;

    private Pattern targetHandlerPatter;

    private ViewResolver viewResolver;

    private ExceptionModelResolver exceptionModelResolver;

    private String defaultErrorView;

    public JichigoSimpleMappingExceptionResolver() {
        this.responseCodeEceptionMapping = new ExceptionMapping<Integer>();
        LinkedHashMap<String, Integer> responseCodeMapping = new LinkedHashMap<String, Integer>();
        responseCodeMapping.put(NoSuchRequestHandlingMethodException.class.getName(), HttpServletResponse.SC_NOT_FOUND);
        responseCodeMapping.put(HttpRequestMethodNotSupportedException.class.getName(),
                HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        responseCodeMapping.put(HttpMediaTypeNotSupportedException.class.getName(),
                HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        responseCodeMapping.put(HttpMediaTypeNotAcceptableException.class.getName(),
                HttpServletResponse.SC_NOT_ACCEPTABLE);
        responseCodeMapping.put(MissingServletRequestParameterException.class.getName(),
                HttpServletResponse.SC_BAD_REQUEST);
        responseCodeMapping.put(ServletRequestBindingException.class.getName(), HttpServletResponse.SC_BAD_REQUEST);
        responseCodeMapping.put(TypeMismatchException.class.getName(), HttpServletResponse.SC_BAD_REQUEST);
        responseCodeMapping.put(HttpMessageNotReadableException.class.getName(), HttpServletResponse.SC_BAD_REQUEST);
        responseCodeMapping.put(MethodArgumentNotValidException.class.getName(), HttpServletResponse.SC_BAD_REQUEST);
        responseCodeMapping.put(MissingServletRequestPartException.class.getName(), HttpServletResponse.SC_BAD_REQUEST);
        responseCodeEceptionMapping.setMapping(responseCodeMapping);

        this.acceptMimeTypeMatcher = new AcceptMimeTypeMatcher();
    }

    public void setTargetHandlerRegex(String targetHandlerRegex) {
        targetHandlerPatter = Pattern.compile(targetHandlerRegex);
    }

    public void setViewResolver(ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

    public void setExceptionModelResolver(ExceptionModelResolver exceptionModelResolver) {
        this.exceptionModelResolver = exceptionModelResolver;
    }

    public void setDefaultErrorView(String defaultErrorView) {
        super.setDefaultErrorView(defaultErrorView);
        this.defaultErrorView = defaultErrorView;
    }

    public void setTargetAcceptMimeType(String targetAcceptMimeType) {
        setTargetAcceptMimeTypes(Collections.singletonList(targetAcceptMimeType));
    }

    public void setTargetAcceptMimeTypes(List<String> targetAcceptMimeTypes) {
        acceptMimeTypeMatcher.setTargetAcceptMimeTypes(targetAcceptMimeTypes);
    }

    @Override
    protected boolean shouldApplyTo(HttpServletRequest request, Object handler) {

        if (targetHandlerPatter == null) {
            return super.shouldApplyTo(request, handler);
        }

        Class<?> handlerClass = null;
        if (handler instanceof HandlerMethod) {
            handlerClass = ((HandlerMethod) handler).getBean().getClass();
        } else if (handler != null) {
            handlerClass = handler.getClass();
        }

        boolean match = false;
        if (handlerClass != null) {
            match = targetHandlerPatter.matcher(handlerClass.getName()).matches();
        }
        if (!match) {
            String accept = request.getHeader("Accept");
            if (accept != null) {
                match = acceptMimeTypeMatcher.matches(accept);
            }
        }

        return match;
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {

        ModelAndView modelAndView = super.doResolveException(request, response, handler, ex);

        if (modelAndView == null) {
            return null;
        }

        if (modelAndView.getViewName() == defaultErrorView) {
            Integer statusCode = responseCodeEceptionMapping.getContainsValue(ex.getClass());
            if (statusCode != null) {
                applyStatusCodeIfPossible(request, response, statusCode);
            }
        }

        if (viewResolver != null) {
            LocaleResolver localeResolver = (LocaleResolver) request
                    .getAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE);
            Locale locale = localeResolver != null ? localeResolver.resolveLocale(request) : Locale.getDefault();
            try {
                View view = viewResolver.resolveViewName(modelAndView.getViewName(), locale);
                if (view != null) {
                    modelAndView.setView(view);
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        if (exceptionModelResolver != null) {
            Object model = exceptionModelResolver.resolveModel(request, response, ex);
            modelAndView.addObject(model);
        }

        return modelAndView;
    }
}