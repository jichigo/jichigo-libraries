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

import java.text.DecimalFormat;
import java.text.ParseException;

import org.jichigo.utility.cache.CacheByKey;
import org.jichigo.utility.cache.LazyCache;

/**
 * Number Pattern class.
 * <p>
 * this class's instance is thread safe.
 * </p>
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author created by Kazuki Shimizu
 */
public class NumberPattern {

    /**
     * instance cache.
     */
    private static final LazyCache<NumberPattern> numberPatternCache = new CacheByKey<NumberPattern>() {
        /*
         * (�� Javadoc)
         * 
         * @see org.jichigo.utility.cache.CacheByKey#initialValue(java.lang.Object[])
         */
        @Override
        protected NumberPattern initialValue(final Object... args) {
            final String pattern = String.class.cast(args[0]);
            return new NumberPattern(pattern);
        }
    };

    /**
     * message format cache.
     */
    private final ThreadLocal<DecimalFormat> decimalFormatCache = new ThreadLocal<DecimalFormat>() {
        /*
         * (�� Javadoc)
         * 
         * @see java.lang.ThreadLocal#initialValue()
         */
        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat(pattern);
        }
    };

    /**
     * decimal pattern string.
     */
    private final String pattern;

    /**
     * 
     * @param pattern decimal pattern string.
     * @param locale locale.
     */
    private NumberPattern(final String pattern) {
        this.pattern = pattern;
    }

    /**
     * Get NumberPattern instance.
     * 
     * @param pattern decimal pattern.
     * @return DecimalPattern instance.
     */
    public static NumberPattern getPattern(final String pattern) {
        return numberPatternCache.getOrCreate(pattern);
    }

    /**
     * Clear cache.
     */
    public static void clearCache() {
        numberPatternCache.clear();
    }

    /**
     * format number.
     * 
     * @param targetNumber target number.
     * @return formatted string.
     */
    public String format(final Number targetNumber) {
        return getFormat().format(targetNumber);
    }

    /**
     * Parse number string.
     * 
     * @param targetNumberString target number string.
     * @return number.
     * @throws ParseException
     */
    public Number parse(final String targetNumberString) throws ParseException {
        return getFormat().parse(targetNumberString);
    }

    /**
     * Get decimal format.
     * 
     * @return decimal format.
     */
    private DecimalFormat getFormat() {
        return decimalFormatCache.get();
    }

}
