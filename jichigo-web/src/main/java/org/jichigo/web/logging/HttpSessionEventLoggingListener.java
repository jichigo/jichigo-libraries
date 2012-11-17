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

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jichigo.web.base.support.HttpSessionEventListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener class for logging http session event.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author Created by Kazuki Shimizu
 */
public class HttpSessionEventLoggingListener extends HttpSessionEventListenerSupport implements HttpSessionListener,
        HttpSessionActivationListener, HttpSessionAttributeListener, HttpSessionBindingListener {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(HttpSessionEventLoggingListener.class);

    /**
     * Log event of value bound.
     * 
     * @param event session binding event.
     */
    @Override
    public void valueBound(final HttpSessionBindingEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("session [{}] value bound. name [{}]. value [{}].",
                    array(event.getSession().getId(), event.getName(), event.getValue()));
        }
    }

    /**
     * Log event of value unbound.
     * 
     * @param event session binding event.
     */
    @Override
    public void valueUnbound(final HttpSessionBindingEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("session [{}] value unbound. name [{}]. value [{}].",
                    array(event.getSession().getId(), event.getName(), event.getValue()));
        }
    }

    /**
     * Log event of attribute added.
     * 
     * @param event session binding event.
     */
    @Override
    public void attributeAdded(final HttpSessionBindingEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("session [{}] attribute added. name [{}]. value [{}].",
                    array(event.getSession().getId(), event.getName(), event.getValue()));
        }
    }

    /**
     * Log event of attribute removed.
     * 
     * @param event session binding event.
     */
    @Override
    public void attributeRemoved(final HttpSessionBindingEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("session [{}] attribute removed. name [{}]. value [{}].",
                    array(event.getSession().getId(), event.getName(), event.getValue()));
        }
    }

    /**
     * Log event of attribute replaced.
     * 
     * @param event session binding event.
     */
    @Override
    public void attributeReplaced(final HttpSessionBindingEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("session [{}] attribute replaced. name [{}]. value [{}].",
                    array(event.getSession().getId(), event.getName(), event.getValue()));
        }
    }

    /**
     * Log event of session will passivate.
     * 
     * @param event session event.
     */
    @Override
    public void sessionWillPassivate(final HttpSessionEvent event) {
        if (logger.isInfoEnabled()) {
            logger.info("session [{}] will passivate.", array(event.getSession().getId()));
        }
    }

    /**
     * Log event of session did activate.
     * 
     * @param event session event.
     */
    @Override
    public void sessionDidActivate(final HttpSessionEvent event) {
        if (logger.isInfoEnabled()) {
            logger.info("session [{}] did activate.", array(event.getSession().getId()));
        }
    }

    /**
     * Log event of session created.
     * 
     * @param event session event.
     */
    @Override
    public void sessionCreated(final HttpSessionEvent event) {
        if (logger.isInfoEnabled()) {
            final HttpSession session = event.getSession();
            logger.info("session [{}] created. creationTime [{}].",
                    array(session.getId(), new Timestamp(session.getCreationTime())));
        }
    }

    /**
     * Log event of session destroyed.
     * 
     * @param event session event.
     */
    @Override
    public void sessionDestroyed(final HttpSessionEvent event) {
        if (logger.isInfoEnabled()) {
            final HttpSession session = event.getSession();
            logger.info("session [{}] destroyed. lastAccessedTime [{}].",
                    array(session.getId(), new Timestamp(session.getLastAccessedTime())));
        }
    }

}
