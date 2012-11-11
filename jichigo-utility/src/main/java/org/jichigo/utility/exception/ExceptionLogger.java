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
package org.jichigo.utility.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exception logger class.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author Kazuki Shimizu
 */
public class ExceptionLogger {

    /**
     * Initial capacity of cache.
     */
    private static final int CACHE_INITIAL_CAPACITY = 16;

    /**
     * load factor of cache.
     */
    private static final float CACHE_LOAD_FACTOR = 0.75f;

    /**
     * Max capacity of cache.
     */
    private static final int CACHE_MAX_CAPACITY = 1024;

    /**
     * Level enum.
     * 
     * @since 1.0.0
     * @version 1.0.0
     * @author Kazuki Shimizu
     */
    public static enum Level {
        /**
         * level of warn.
         */
        warn,
        /**
         * level of error.
         */
        error;

        /**
         * Is enable level.
         * 
         * @return if enable, return true.
         */
        boolean isEnabled() {
            if (this == Level.warn) {
                return applicationLogger.isWarnEnabled() || monitoringLogger.isWarnEnabled();
            }
            if (this == Level.error) {
                return applicationLogger.isErrorEnabled() || monitoringLogger.isErrorEnabled();
            }
            throw new IllegalStateException("unsupported level. level is [" + this + "]");
        }
    }

    /**
     * Application logger.
     * <p>
     * output message & stack trace.<br>
     * logger name is 'org.jichigo.utility.exception.ExceptionLogger'.
     * </p>
     */
    private static final Logger applicationLogger = LoggerFactory.getLogger(ExceptionLogger.class);

    /**
     * Monitoring logger.
     * <p>
     * output only message.(not stack trace)<br>
     * logger name is 'org.jichigo.utility.exception.ExceptionLogger.Monitor'.
     * </p>
     */
    private static final Logger monitoringLogger = LoggerFactory.getLogger(applicationLogger.getName() + ".Monitoring");

    /**
     * LRU Cache of code mapping.
     * <p>
     * key : class of exception.<br>
     * value : apply code.
     * </p>
     */
    private final Map<Class<? extends Exception>, String> codeMappingCache = Collections
            .synchronizedMap(new LinkedHashMap<Class<? extends Exception>, String>(CACHE_INITIAL_CAPACITY,
                    CACHE_LOAD_FACTOR, true) {
                private static final long serialVersionUID = 1L;

                @Override
                protected boolean removeEldestEntry(Map.Entry<Class<? extends Exception>, String> eldest) {
                    return size() > CACHE_MAX_CAPACITY;
                }
            });

    /**
     * LRU Cache of level mapping.
     * <p>
     * key : code.<br>
     * value : apply level.
     * </p>
     */
    private final Map<String, Level> levelMappingCache = Collections.synchronizedMap(new LinkedHashMap<String, Level>(
            CACHE_INITIAL_CAPACITY, CACHE_LOAD_FACTOR, true) {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Level> eldest) {
            return size() > CACHE_MAX_CAPACITY;
        }
    });

    /**
     * Custom code mapping.
     * <p>
     * key: exception class pattern. <br>
     * value : apply code.
     * </p>
     */
    private Map<String, String> customCodeMap = new LinkedHashMap<String, String>();

    /**
     * Custom level mapping.
     * <p>
     * key: code pattern. <br>
     * value : apply level.
     * </p>
     */
    private Map<String, Level> customLevelMap = new LinkedHashMap<String, Level>();

    /**
     * Default code.
     * <p>
     * default value is null.
     * </p>
     */
    private String defaultCode = null;

    /**
     * Default level.
     * <p>
     * default value is {@link Level#error}.
     * </p>
     */
    private Level defaultLevel = Level.error;

    /**
     * Default Constructor.
     */
    public ExceptionLogger() {
        super();
    }

    /**
     * Inject default code.
     * 
     * @param defaultCode default code.
     */
    public void setDefaultCode(final String defaultCode) {
        this.defaultCode = defaultCode;
    }

    /**
     * Inject default level.
     * 
     * @param defaultLevel default level.
     */
    public void setDefaultLevel(final Level defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    /**
     * Inject custom code map.
     * 
     * @param customCodeMap custom code mapping.
     */
    public void setCustomCodeMap(final LinkedHashMap<String, String> customCodeMap) {
        this.customCodeMap = customCodeMap;
    }

    /**
     * Inject custom level map.
     * 
     * @param customCodeMap custom level mapping.
     */
    public void setCustomLevelMap(final LinkedHashMap<String, Level> customLevelMap) {
        this.customLevelMap = customLevelMap;
    }

    /**
     * Output log.
     * 
     * @param e exception.
     */
    public void log(final Exception e) {
        final Level level = decideLevel(e);
        if (!level.isEnabled()) {
            return;
        }
        log(makeMessage(e), e, level);
    }

    /**
     * Output log.
     * 
     * @param message message.
     * @param e exception.
     */
    public void log(final String message, final Exception e) {
        log(message, e, decideLevel(e));
    }

    /**
     * Output warn log.
     * 
     * @param e exception.
     */
    public void warn(final Exception e) {
        if (!Level.warn.isEnabled()) {
            return;
        }
        warn(makeMessage(e), e);
    }

    /**
     * Output warn log.
     * 
     * @param message message.
     * @param e exception.
     */
    public void warn(final String message, final Exception e) {
        log(message, e, Level.warn);
    }

    /**
     * Output error log.
     * 
     * @param e exception.
     */
    public void error(final Exception e) {
        if (!Level.error.isEnabled()) {
            return;
        }
        error(makeMessage(e), e);
    }

    /**
     * Output error log.
     * 
     * @param message message.
     * @param e exception.
     */
    public void error(final String message, final Exception e) {
        log(message, e, Level.error);
    }

    /**
     * Output log.
     * 
     * @param message message.
     * @param e exception.
     * @param level level.
     */
    protected void log(final String message, final Exception e, final Level level) {
        if (level == Level.warn) {
            monitoringLogger.warn(message);
            applicationLogger.warn(message, e);
        } else if (level == Level.error) {
            monitoringLogger.error(message);
            applicationLogger.error(message, e);
        }
    }

    /**
     * Make message.
     * 
     * @param e exception.
     * @return message.
     */
    protected String makeMessage(final Exception e) {
        final String code = decideCode(e);
        if (code == null || code.isEmpty()) {
            return e.getMessage();
        } else {
            return formatMessage(code, e.getMessage());
        }
    }

    /**
     * Decide level.
     * 
     * @param e exception.
     * @return level.
     */
    protected Level decideLevel(final Exception e) {
        // decide code.
        final String code = decideCode(e);
        // find level in cache.
        Level level = levelMappingCache.get(code);
        if (level != null) {
            return level;
        }
        synchronized (code.intern()) {
            // retry find level in cache.
            level = levelMappingCache.get(code);
            if (level != null) {
                return level;
            }
            // find level.
            level = findLevel(code);
            // set level in cache.
            levelMappingCache.put(code, level);
            return level;
        }
    }

    /**
     * Find level.
     * 
     * @param code code.
     * @return level.
     */
    protected Level findLevel(final String code) {
        for (final Map.Entry<String, Level> customLevelEntry : customLevelMap.entrySet()) {
            if (code.contains(customLevelEntry.getKey())) {
                return customLevelEntry.getValue();
            }
        }
        return defaultLevel;
    }

    /**
     * Decide code.
     * 
     * @param e exception.
     * @return code.
     */
    protected String decideCode(final Exception e) {
        // get code.
        if (e instanceof ExceptionWithCode) {
            return ((ExceptionWithCode) e).getCode();
        }
        // find code in cache.
        final Class<? extends Exception> exceptionClass = e.getClass();
        String code = codeMappingCache.get(exceptionClass);
        if (code != null) {
            return code;
        }
        synchronized (exceptionClass) {
            // retry find code in cache.
            code = codeMappingCache.get(exceptionClass);
            if (code != null) {
                return code;
            }
            // find code.
            code = findCode(e);
            // set code in cache.
            codeMappingCache.put(exceptionClass, code);
            return code;
        }
    }

    /**
     * Find code.
     * 
     * @param e exception.
     * @return code.
     */
    protected String findCode(final Exception e) {
        final List<String> classNames = new ArrayList<String>();
        Class<?> cls = e.getClass();
        while (cls != Object.class) {
            classNames.add(cls.getName());
            cls = cls.getSuperclass();
        }
        for (final Map.Entry<String, String> customCodeEntry : customCodeMap.entrySet()) {
            for (final String className : classNames) {
                if (className.contains(customCodeEntry.getKey())) {
                    return customCodeEntry.getValue();
                }
            }
        }
        return defaultCode;
    }

    /**
     * Format message.
     * 
     * @param code code.
     * @param exceptionMessage exception message.
     * @return formatted message.
     */
    protected String formatMessage(final String code, final String exceptionMessage) {
        return new StringBuffer().append("[").append(code).append("] ").append(exceptionMessage).toString();
    }

}
