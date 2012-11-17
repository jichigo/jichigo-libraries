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
package org.jichigo.web.logging;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jichigo.web.base.support.ServletContextEventListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener class for logging servlet context event.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author Created by Kazuki Shimizu
 */
public class ServletContextEventLoggingListener extends ServletContextEventListenerSupport implements
        ServletContextListener, ServletContextAttributeListener {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ServletContextEventLoggingListener.class);

    /**
     * Log event of attribute added.
     * 
     * @param event servlet context attribute event.
     */
    @Override
    public void attributeAdded(final ServletContextAttributeEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("servlet context [{}] attribute added. name [{}]. value [{}].",
                    array(event.getServletContext().getContextPath(), event.getName(), event.getValue()));
        }
    }

    /**
     * Log event of attribute removed.
     * 
     * @param event servlet context attribute event.
     */
    @Override
    public void attributeRemoved(final ServletContextAttributeEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("servlet context [{}] attribute removed. name [{}]. value [{}].",
                    array(event.getServletContext().getContextPath(), event.getName(), event.getValue()));
        }
    }

    /**
     * Log event of attribute replaced.
     * 
     * @param event servlet context attribute event.
     */
    @Override
    public void attributeReplaced(final ServletContextAttributeEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("servlet context [{}] attribute replaced. name [{}]. value [{}].",
                    array(event.getServletContext().getContextPath(), event.getName(), event.getValue()));
        }
    }

    /**
     * Log event of initialized.
     * 
     * @param event servlet context event.
     */
    @Override
    public void contextInitialized(final ServletContextEvent event) {
        if (logger.isInfoEnabled()) {
            logger.info("servlet context [{}] initialized.", array(event.getServletContext().getContextPath()));
        }
    }

    /**
     * Log event of destroyed.
     * 
     * @param event servlet context event.
     */
    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        if (logger.isInfoEnabled()) {
            logger.info("servlet context [{}] destroyed.", array(event.getServletContext().getContextPath()));
        }
    }

}
