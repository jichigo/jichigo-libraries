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
package org.jichigo.utility.text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jichigo.utility.cache.Cache;
import org.jichigo.utility.cache.CacheByKey;

/**
 * Date Pattern class.
 * <p>
 * this class's instance is thread safe.
 * </p>
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author created by Kazuki Shimizu
 */
public class DatePattern {

    /**
     * instance cache.
     */
    private static final Cache<DatePattern> datePatternCache = new CacheByKey<DatePattern>() {
        /*
         * (”ñ Javadoc)
         * 
         * @see org.jichigo.utility.cache.CacheByKey#initialValue(java.lang.Object[])
         */
        @Override
        protected DatePattern initialValue(final Object... args) {
            final String pattern = String.class.cast(args[0]);
            final Locale locale = Locale.class.cast(args[1]);
            return new DatePattern(pattern, locale);
        }
    };

    /**
     * date format cache.
     */
    private final ThreadLocal<DateFormat> dateFormatCache = new ThreadLocal<DateFormat>() {
        /*
         * (”ñ Javadoc)
         * 
         * @see java.lang.ThreadLocal#initialValue()
         */
        @Override
        protected DateFormat initialValue() {
            final DateFormat instance = new SimpleDateFormat(pattern, locale);
            instance.setLenient(false);
            return instance;
        }
    };

    /**
     * date pattern string.
     */
    private final String pattern;

    /**
     * locale.
     */
    private final Locale locale;

    /**
     * Constructor.
     * 
     * @param pattern date pattern string.
     * @param locale locale.
     */
    private DatePattern(final String pattern, final Locale locale) {
        this.pattern = pattern;
        this.locale = locale;
    }

    /**
     * Get DatePattern instance of Default Locale.
     * 
     * @param pattern date pattern.
     * @return DatePattern instance of Default Locale.
     */
    public static DatePattern getPattern(final String pattern) {
        return getPattern(pattern, Locale.getDefault(), Cache.CACHE);
    }

    /**
     * Get DatePattern instance of Default Locale.
     * <p>
     * if not exists in cache, only create instance. (no cache)
     * </p>
     * 
     * @param pattern date pattern.
     * @return DatePattern instance of Default Locale.
     */
    public static DatePattern getPatternNoCache(final String pattern) {
        return getPattern(pattern, Locale.getDefault(), Cache.NO_CACHE);
    }

    /**
     * Get DatePattern instance.
     * 
     * @param pattern date pattern.
     * @param locale locale
     * @return DatePattern instance.
     */
    public static DatePattern getPattern(final String pattern, final Locale locale) {
        return getPattern(pattern, locale, Cache.CACHE);
    }

    /**
     * Get DatePattern instance.
     * <p>
     * if not exists in cache, only create instance. (no cache)
     * </p>
     * 
     * @param pattern date pattern.
     * @param locale locale
     * @return DatePattern instance.
     */
    public static DatePattern getPatternNoCache(final String pattern, final Locale locale) {
        return getPattern(pattern, locale, Cache.NO_CACHE);
    }

    /**
     * Get DatePattern instance.
     * 
     * @param pattern date pattern.
     * @param locale locale
     * @param doCache true is cache.
     * @return DatePattern instance.
     */
    private static DatePattern getPattern(final String pattern, final Locale locale, final boolean doCache) {
        return datePatternCache.get(doCache, pattern, locale);
    }

    /**
     * format date.
     * 
     * @param targetDate target date.
     * @return formatted string.
     */
    public String format(final Date targetDate) {
        return getFormat().format(targetDate);
    }

    /**
     * Parse date string.
     * 
     * @param targetDateString target date string.
     * @return date.
     * @throws ParseException
     */
    public Date parse(final String targetDateString) throws ParseException {
        return getFormat().parse(targetDateString);
    }

    /**
     * Get date format.
     * 
     * @return date format.
     */
    private DateFormat getFormat() {
        return dateFormatCache.get();
    }

}
