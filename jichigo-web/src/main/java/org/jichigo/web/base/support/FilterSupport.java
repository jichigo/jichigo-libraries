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
package org.jichigo.web.base.support;

import java.util.Enumeration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support class of Filter.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author Created by Kazuki Shimizu
 */
public abstract class FilterSupport {

    /**
     * Logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Filter config.
     */
    protected FilterConfig filterConfig;

    /**
     * Initialize filter.
     * 
     * @param filterConfig filter config.
     * @throws ServletException if
     */
    public final void init(final FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        if (logger.isInfoEnabled()) {
            @SuppressWarnings("unchecked")
            final Enumeration<String> parameterNames = filterConfig.getInitParameterNames();
            while (parameterNames.hasMoreElements()) {
                final String parameterName = parameterNames.nextElement();
                final String parameterValue = filterConfig.getInitParameter(parameterName);
                logger.info(
                        "filter [{}] initialize parameter. servlet context [{}]. name [{}]. value [{}].",
                        array(filterConfig.getFilterName(), filterConfig.getServletContext().getContextPath(),
                                parameterName, parameterValue));
            }
        }
        initBean();
        if (logger.isInfoEnabled()) {
            logger.info("filter [{}] initialized. servlet context [{}].",
                    array(filterConfig.getFilterName(), filterConfig.getServletContext().getContextPath()));
        }
    }

    /**
     * destroy filter.
     */
    public final void destroy() {
        destroyBean();
        if (logger.isInfoEnabled()) {
            logger.info("filter [{}] destroyed. servlet context [{}].", filterConfig.getServletContext()
                    .getContextPath(), filterConfig.getFilterName());
        }
    }

    /**
     * Initialize bean.
     */
    protected void initBean() {
    }

    /**
     * Destroy bean.
     */
    protected void destroyBean() {
    }

    /**
     * Convert to array from variable length variable.
     * 
     * @param objects variable length variable.
     * @return array.
     */
    protected Object[] array(final Object... objects) {
        return objects;
    }

}
