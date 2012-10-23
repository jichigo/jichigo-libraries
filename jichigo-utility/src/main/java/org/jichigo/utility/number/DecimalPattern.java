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
package org.jichigo.utility.number;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jichigo.utility.cache.Cache;
import org.jichigo.utility.cache.ObjectKeyCache;
import org.jichigo.utility.cache.ObjectThreadCache;
import org.jichigo.utility.timer.StopWatch;

/**
 * Date Pattern class.
 */
public class DecimalPattern {

    /**
     * instance cache.
     */
    private static final Cache<DecimalPattern> decimalPatternCache = new ObjectKeyCache<DecimalPattern>() {
        @Override
        protected DecimalPattern createInstance(final Object... args) {
            return new DecimalPattern((String) args[0]);
        }
    };

    /**
     * date format for thread.
     */
    private final Cache<DecimalFormat> decimalFormatCache = new ObjectThreadCache<DecimalFormat>() {
        @Override
        protected DecimalFormat createInstance(final Object... args) {
            final DecimalFormat instance = new DecimalFormat((String) args[0]);
            return instance;
        }
    };

    /**
     * date pattern string.
     */
    private final String pattern;

    /**
     * 
     * @param pattern date pattern string.
     * @param locale locale.
     */
    private DecimalPattern(final String pattern) {
        this.pattern = pattern;
    }

    /**
     * Get DatePattern instance of Default Locale.
     * 
     * @param pattern date pattern.
     * @return DatePattern instance of Default Locale.
     */
    public static DecimalPattern getInstance(final String pattern) {
        return decimalPatternCache.getInstance(pattern);
    }

    /**
     * format date.
     * 
     * @param target target date.
     * @return formatted string.
     */
    public String format(final Number targetNumber) {
        return getFormat().format(targetNumber);
    }

    /**
     * Parse date string.
     * 
     * @param target target date string.
     * @return date.
     * @throws ParseException
     */
    public Number parse(final String targetString) throws ParseException {
        return getFormat().parse(targetString);
    }

    /**
     * Get date format.
     * 
     * @return date format.
     */
    private DecimalFormat getFormat() {
        return decimalFormatCache.getInstance(pattern);
    }

    public static void main(String[] args) throws ParseException {
        StopWatch sw = StopWatch.newInstance();

        new SimpleDateFormat();
        sw.start();

        DecimalPattern decimalPattern = DecimalPattern.getInstance(",###");
        for (int i = 0; i < 10; i++) {
            System.out.println(decimalPattern.format(10000000));
            System.out.println(decimalPattern.parse(decimalPattern.format(new BigDecimal(
                    "1000000000000000000000000000000000000000000000"))));
            System.out
                    .println(decimalPattern.parse(
                            decimalPattern.format(new BigDecimal("10000000000000000000000000000000000000000000000")))
                            .getClass());
            sw.split();
        }
        sw.stop();

        sw.print();
    }
}
