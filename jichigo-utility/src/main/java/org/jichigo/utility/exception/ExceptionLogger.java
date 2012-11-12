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
     * Max capacity of LRU cache.
     */
    private static final int LRU_CACHE_MAX_CAPACITY = 1024;

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
     * logger name is 'org.jichigo.utility.exception.ExceptionLogger.Monitoring'.
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
            .synchronizedMap(new LRUCache<Class<? extends Exception>, String>(LRU_CACHE_MAX_CAPACITY));

    /**
     * LRU Cache of level mapping.
     * <p>
     * key : code.<br>
     * value : apply level.
     * </p>
     */
    private final Map<String, Level> levelMappingCache = Collections.synchronizedMap(new LRUCache<String, Level>(
            LRU_CACHE_MAX_CAPACITY));

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
        log(makeMessage(message, e), e, decideLevel(e));
    }

    /**
     * Output trace log.
     * 
     * @param e exception.
     */
    public void trace(final Exception e) {
        if (!Level.trace.isEnabled()) {
            return;
        }
        trace(e.getMessage(), e);
    }

    /**
     * Output trace log.
     * 
     * @param message message.
     * @param e exception.
     */
    public void trace(final String message, final Exception e) {
        log(makeMessage(message, e), e, Level.trace);
    }

    /**
     * Output debug log.
     * 
     * @param e exception.
     */
    public void debug(final Exception e) {
        if (!Level.debug.isEnabled()) {
            return;
        }
        debug(e.getMessage(), e);
    }

    /**
     * Output debug log.
     * 
     * @param message message.
     * @param e exception.
     */
    public void debug(final String message, final Exception e) {
        log(makeMessage(message, e), e, Level.debug);
    }

    /**
     * Output info log.
     * 
     * @param e exception.
     */
    public void info(final Exception e) {
        if (!Level.info.isEnabled()) {
            return;
        }
        info(e.getMessage(), e);
    }

    /**
     * Output info log.
     * 
     * @param message message.
     * @param e exception.
     */
    public void info(final String message, final Exception e) {
        log(makeMessage(message, e), e, Level.info);
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
        warn(e.getMessage(), e);
    }

    /**
     * Output warn log.
     * 
     * @param message message.
     * @param e exception.
     */
    public void warn(final String message, final Exception e) {
        log(makeMessage(message, e), e, Level.warn);
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
        error(e.getMessage(), e);
    }

    /**
     * Output error log.
     * 
     * @param message message.
     * @param e exception.
     */
    public void error(final String message, final Exception e) {
        log(makeMessage(message, e), e, Level.error);
    }

    /**
     * Output log.
     * 
     * @param message message.
     * @param e exception.
     * @param level level.
     */
    protected void log(final String message, final Exception e, final Level level) {
        if (level == Level.error) {
            monitoringLogger.error(message);
            applicationLogger.error(message, e);
        } else if (level == Level.warn) {
            monitoringLogger.warn(message);
            applicationLogger.warn(message, e);
        } else if (level == Level.info) {
            monitoringLogger.info(message);
            applicationLogger.info(message, e);
        } else if (level == Level.debug) {
            monitoringLogger.debug(message);
            applicationLogger.debug(message, e);
        } else if (level == Level.trace) {
            monitoringLogger.trace(message);
            applicationLogger.trace(message, e);
        }
    }

    /**
     * Make message.
     * 
     * @param e exception.
     * @return message.
     */
    protected String makeMessage(final Exception e) {
        return makeMessage(e.getMessage(), e);
    }

    /**
     * Make message.
     * 
     * @param message message.
     * @param e exception.
     * @return message.
     */
    protected String makeMessage(final String message, final Exception e) {
        final String code = decideCode(e);
        return formatMessage(code, message);
    }

    /**
     * Decide level.
     * 
     * @param e exception.
     * @return level.
     */
    protected Level decideLevel(final Exception e) {
        // check exists custom setting.
        if (customLevelMap.isEmpty()) {
            return defaultLevel;
        }
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
        // check exists custom setting.
        if (customCodeMap.isEmpty()) {
            return defaultCode;
        }
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
     * @param message message.
     * @return formatted message.
     */
    protected String formatMessage(final String code, final String message) {
        final StringBuffer sb = new StringBuffer();
        if (code != null && !code.isEmpty()) {
            sb.append("[").append(code).append("] ");
        }
        sb.append(message);
        return sb.toString();
    }

    /**
     * Level enum.
     * 
     * @since 1.0.0
     * @version 1.0.0
     * @author Kazuki Shimizu
     */
    public enum Level {
        /**
         * level of off.
         */
        off,
        /**
         * level of trace.
         */
        trace,
        /**
         * level of debug.
         */
        debug,
        /**
         * level of info.
         */
        info,
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
            if (this == Level.error) {
                return applicationLogger.isErrorEnabled() || monitoringLogger.isErrorEnabled();
            }
            if (this == Level.warn) {
                return applicationLogger.isWarnEnabled() || monitoringLogger.isWarnEnabled();
            }
            if (this == Level.info) {
                return applicationLogger.isInfoEnabled() || monitoringLogger.isInfoEnabled();
            }
            if (this == Level.debug) {
                return applicationLogger.isDebugEnabled() || monitoringLogger.isDebugEnabled();
            }
            if (this == Level.trace) {
                return applicationLogger.isTraceEnabled() || monitoringLogger.isTraceEnabled();
            }
            if (this == Level.off) {
                return false;
            }
            throw new IllegalStateException("unsupported level. level is [" + this + "]");
        }
    }

    /**
     * LRU cache class.
     * 
     * @since 1.0.0
     * @version 1.0.0
     * @author Kazuki Shimizu
     * 
     * @param <K> key type.
     * @param <V> value type.
     */
    private class LRUCache<K, V> extends LinkedHashMap<K, V> {

        /**
         * serialVersionUID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Initial capacity of LRU cache.
         */
        private static final int CACHE_INITIAL_CAPACITY = 16;

        /**
         * load factor of LRU cache.
         */
        private static final float CACHE_LOAD_FACTOR = 0.75f;

        /**
         * Max capacity.
         */
        private int maxCapacity;

        /**
         * Constructor.
         */
        private LRUCache(final int maxCapacity) {
            super(CACHE_INITIAL_CAPACITY, CACHE_LOAD_FACTOR, true);
            this.maxCapacity = maxCapacity;
        }

        /**
         * Is remove eldest entry ?
         * 
         * @param eldest The least recently inserted entry in the map, or if this is an access-ordered map, the least
         *        recently accessed entry.
         * @return if size is over maxCapacity, return true.
         */
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxCapacity;
        }

    }

}
