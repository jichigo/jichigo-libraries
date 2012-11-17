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

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support class for servlet context listener.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author Created by Kazuki Shimizu
 */
public class ServletContextEventListenerSupport {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ServletContextEventListenerSupport.class);

    /**
     * Do nothing on attributeAdded.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event servlet context attribute event.
     */
    public void attributeAdded(final ServletContextAttributeEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call attributeAdded of {}. not exists implementation.", getClass().getName());
        }
    }

    /**
     * Do nothing on attributeRemoved.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event servlet context attribute event.
     */
    public void attributeRemoved(final ServletContextAttributeEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call attributeRemoved of {}. not exists implementation.", getClass().getName());
        }
    }

    /**
     * Do nothing on attributeReplaced.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event servlet context attribute event.
     */
    public void attributeReplaced(final ServletContextAttributeEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call attributeReplaced of {}. not exists implementation.", getClass().getName());
        }
    }

    /**
     * Do nothing on contextInitialized.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event servlet context event.
     */
    public void contextInitialized(final ServletContextEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call contextInitialized of {}. not exists implementation.", getClass().getName());
        }
    }

    /**
     * Do nothing on contextDestroyed.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event servlet context event.
     */
    public void contextDestroyed(final ServletContextEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call contextDestroyed of {}. not exists implementation.", getClass().getName());
        }
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
