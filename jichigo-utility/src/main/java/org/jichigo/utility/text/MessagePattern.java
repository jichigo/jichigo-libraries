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

import java.text.MessageFormat;
import java.util.Locale;

import org.jichigo.utility.cache.Cache;
import org.jichigo.utility.cache.CacheByKey;
import org.jichigo.utility.cache.CacheByThread;

/**
 * Message Pattern class.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author created by Kazuki Shimizu
 */
public class MessagePattern {

    /**
     * instance cache.
     */
    private static final Cache<MessagePattern> messagePatternCache = new CacheByKey<MessagePattern>() {
        @Override
        protected MessagePattern create(final Object... args) {
            final String pattern = String.class.cast(args[0]);
            final Locale locale = Locale.class.cast(args[1]);
            return new MessagePattern(pattern, locale);
        }
    };

    /**
     * message format cache.
     */
    private final Cache<MessageFormat> messageFormatCache = new CacheByThread<MessageFormat>() {
        @Override
        protected MessageFormat create(final Object... args) {
            final MessageFormat instance = new MessageFormat(pattern, locale);
            return instance;
        }
    };

    /**
     * message pattern string.
     */
    private final String pattern;

    /**
     * locale.
     */
    private final Locale locale;

    /**
     * 
     * @param pattern message pattern string.
     * @param locale locale.
     */
    private MessagePattern(final String pattern, final Locale locale) {
        this.pattern = pattern;
        this.locale = locale;
    }

    /**
     * Get MessagePattern instance of Default Locale.
     * 
     * @param pattern message pattern.
     * @return MessagePattern instance of Default Locale.
     */
    public static MessagePattern getInstance(final String pattern) {
        return getInstance(pattern, Locale.getDefault());
    }

    /**
     * Get MessagePattern instance.
     * 
     * @param pattern message pattern.
     * @param locale locale
     * @return MessagePattern instance.
     */
    public static MessagePattern getInstance(final String pattern, final Locale locale) {
        return messagePatternCache.get(pattern, locale);

    }

    /**
     * format message.
     * 
     * @param messageArgs message args.
     * @return formatted string.
     */
    public String format(final Object... messageArgs) {
        return getFormat().format(messageArgs);
    }

    /**
     * Get message format.
     * 
     * @return message format.
     */
    private MessageFormat getFormat() {
        return messageFormatCache.get(pattern, locale);
    }

}
