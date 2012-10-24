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
package org.jichigo.utility.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jichigo.utility.cache.Cache;
import org.jichigo.utility.cache.ObjectCacheByKey;
import org.jichigo.utility.cache.ObjectCacheByThread;

/**
 * Date Pattern class.
 */
public class DatePattern {

    /**
     * instance cache.
     */
    private static final Cache<DatePattern> datePatternCache = new ObjectCacheByKey<DatePattern>() {
        @Override
        protected DatePattern createInstance(final Object... args) {
            final String pattern = String.class.cast(args[0]);
            final Locale locale = Locale.class.cast(args[1]);
            return new DatePattern(pattern, locale);
        }
    };

    /**
     * date format cache.
     */
    private final Cache<DateFormat> dateFormatCache = new ObjectCacheByThread<DateFormat>() {
        @Override
        protected DateFormat createInstance() {
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
    public static DatePattern getInstance(final String pattern) {
        return getInstance(pattern, Locale.getDefault());
    }

    /**
     * Get DatePattern instance.
     * 
     * @param pattern date pattern.
     * @param locale locale
     * @return DatePattern instance.
     */
    public static DatePattern getInstance(final String pattern, final Locale locale) {
        return datePatternCache.getInstance(pattern, locale);

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
        return dateFormatCache.getInstance(pattern, locale);
    }

}
