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

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Support class for http session event listener.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author Created by Kazuki Shimizu
 */
public class HttpSessionEventListenerSupport {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(HttpSessionEventListenerSupport.class);

    /**
     * Do nothing of valueBound.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event http session binding event.
     */
    public void valueBound(final HttpSessionBindingEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call valueBound of {}. not exists implementation.", getClass().getName());
        }
    }

    /**
     * Do nothing of valueUnbound.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event http session binding event.
     */
    public void valueUnbound(final HttpSessionBindingEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call valueUnbound of {}. not exists implementation.", getClass().getName());
        }
    }

    /**
     * Do nothing of attributeAdded.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event http session binding event.
     */
    public void attributeAdded(final HttpSessionBindingEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call attributeAdded of {}. not exists implementation.", getClass().getName());
        }
    }

    /**
     * Do nothing of attributeRemoved.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event http session binding event.
     */
    public void attributeRemoved(final HttpSessionBindingEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call attributeRemoved of {}. not exists implementation.", getClass().getName());
        }
    }

    /**
     * Do nothing of attributeReplaced.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event http session binding event.
     */
    public void attributeReplaced(final HttpSessionBindingEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call attributeReplaced of {}. not exists implementation.", getClass().getName());
        }
    }

    /**
     * Do nothing of sessionWillPassivate.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event http session event.
     */
    public void sessionWillPassivate(final HttpSessionEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call sessionWillPassivate of {}. not exists implementation.", getClass().getName());
        }
    }

    /**
     * Do nothing of sessionDidActivate.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event http session event.
     */
    public void sessionDidActivate(final HttpSessionEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call sessionDidActivate of {}. not exists implementation.", getClass().getName());
        }
    }

    /**
     * Do nothing of sessionCreated.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event http session event.
     */
    public void sessionCreated(final HttpSessionEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call sessionCreated of {}. not exists implementation.", getClass().getName());
        }
    }

    /**
     * Do nothing of sessionDestroyed.
     * <p>
     * If does not override implements, logging warn level.
     * </p>
     * 
     * @param event http session event.
     */
    public void sessionDestroyed(final HttpSessionEvent event) {
        if (logger.isWarnEnabled()) {
            logger.warn("call sessionDestroyed of {}. not exists implementation.", getClass().getName());
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
