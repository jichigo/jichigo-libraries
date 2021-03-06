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
package org.jichigo.date;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for get date from AP layer(AP Server).
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author created by Kazuki Shimizu
 * @see {@link org.slf4j.Logger} Using logger.
 * @see {@link org.slf4j.LoggerFactory} Using logger.
 */
public class ApLayerDateProvider extends ModifiableDateProvider implements DateCreator {

    /**
     * Logger instance of slf4j.
     */
    private final Logger logger = LoggerFactory.getLogger(ApLayerDateProvider.class);

    /**
     * New date.
     * 
     * @return {@link java.uti.Date#Date(long)}. Constructor argument is {@link System#currentTimeMillis()}.
     */
    @Override
    public Date newDate() {
        final Date date = new Date(System.currentTimeMillis());
        if (logger.isDebugEnabled()) {
            logger.debug("date is {}", date);
        }
        return date;
    }

}
