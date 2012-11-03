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
package org.jichigo.utility.xml.bind;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.jichigo.utility.cache.CacheByKey;
import org.jichigo.utility.cache.LazyCache;

/**
 * JAXBContext cache class.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author created by Kazuki Shimizu
 */
public class JAXBContextCache {

    /**
     * instance cache.
     */
    private static final LazyCache<JAXBContext> cache = new CacheByKey<JAXBContext>() {
        /*
         * (”ñ Javadoc)
         * 
         * @see org.jichigo.utility.cache.CacheByKey#initialValue(java.lang.Object[])
         */
        @Override
        protected JAXBContext initialValue(final Object... args) {
            final Class<?> classeToBeBound = Class.class.cast(args[0]);
            return createJAXBContext(classeToBeBound);
        }
    };

    /**
     * Constructor.
     */
    private JAXBContextCache() {
        super();
    }

    /**
     * Create JAXBContext instance.
     * 
     * @param classToBeBound class to be bound.
     * @return JAXBContext instance.
     * @throws NestedJAXBException if class is invalid.
     */
    private static JAXBContext createJAXBContext(final Class<?> classeToBeBound) throws NestedJAXBException {
        if (classeToBeBound == null) {
            throw new IllegalArgumentException("classeToBeBound is null.");
        }
        try {
            return JAXBContext.newInstance(classeToBeBound);
        } catch (final JAXBException e) {
            throw new NestedJAXBException(e);
        }
    }

    /**
     * Get JAXBContext instance.
     * 
     * @param classToBeBound class to be bound.
     * @return JAXBContext instance.
     * @throws JAXBException if class is invalid.
     */
    public static JAXBContext getJAXBContext(final Class<?> classToBeBound) throws JAXBException {
        try {
            return cache.getOrCreate(classToBeBound);
        } catch (final NestedJAXBException e) {
            throw e.causeJAXBException;
        }
    }

    /**
     * Clear cache.
     */
    public static void clearCache() {
        cache.clear();
    }

    /**
     * Nested JAXBException class.
     * <p>
     * Holds cause JAXBException.
     * </p>
     * 
     * @since 1.0.0
     * @version 1.0.0
     * @author created by Kazuki Shimizu
     */
    private static class NestedJAXBException extends RuntimeException {
        /**
         * serialVersion UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * JAXBException.
         */
        private final JAXBException causeJAXBException;

        /**
         * Constructor.
         * 
         * @param causeJAXBException JAXBException.
         */
        private NestedJAXBException(final JAXBException causeJAXBException) {
            super(causeJAXBException);
            this.causeJAXBException = causeJAXBException;
        }
    }

}
