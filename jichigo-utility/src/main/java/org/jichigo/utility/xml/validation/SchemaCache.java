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
package org.jichigo.utility.xml.validation;

import java.io.File;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.jichigo.utility.cache.Cache;
import org.jichigo.utility.cache.CacheByKey;
import org.xml.sax.SAXException;

/**
 * Schema cache class.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author created by Kazuki Shimizu
 */
public class SchemaCache {

    /**
     * instance cache.
     */
    private static final Cache<Schema> cache = new CacheByKey<Schema>() {
        /*
         * (”ñ Javadoc)
         * 
         * @see org.jichigo.utility.cache.CacheByKey#initialValue(java.lang.Object[])
         */
        @Override
        protected Schema initialValue(final Object... args) {
            final String schemaLanguage = String.class.cast(args[0]);
            final Object source = args[1];
            return createSchema(schemaLanguage, source);
        }
    };

    /**
     * Create Schema instance.
     * 
     * @param schemaLanguage schema language.
     * @param source schema source.
     * @return Schema instance.
     * @throws NestedSAXException if schema source is invalid.
     */
    private static Schema createSchema(final String schemaLanguage, final Object source) throws NestedSAXException {
        final SchemaFactory factory = SchemaFactory.newInstance(schemaLanguage);
        try {
            if (File.class.isInstance(source)) {
                return factory.newSchema(File.class.cast(source));
            } else if (URL.class.isInstance(source)) {
                return factory.newSchema(URL.class.cast(source));
            } else if (Source.class.isInstance(source)) {
                return factory.newSchema(Source.class.cast(source));
            } else if (Source[].class.isInstance(source)) {
                return factory.newSchema(Source[].class.cast(source));
            } else {
                final String className = (source == null) ? "null" : source.getClass().getName();
                throw new IllegalArgumentException("source class is unsupported. class is " + className + ".");
            }
        } catch (final SAXException e) {
            throw new NestedSAXException(e);
        }
    }

    /**
     * Get Schema instance.
     * <p>
     * source's supported type is under. <br>
     * {@code java.net.URL}<br>
     * {@code java.net.File}<br>
     * {@code java.net.Source}<br>
     * {@code java.net.Source[]}<br>
     * </p>
     * <p>
     * schema language is {@code javax.xml.XMLConstants}.W3C_XML_SCHEMA_NS_URI
     * </p>
     * 
     * @param source schema source.
     * @return Schema instance.
     */
    public static Schema getSchema(final Object source) throws SAXException {
        return getSchema(XMLConstants.W3C_XML_SCHEMA_NS_URI, source);
    }

    /**
     * Get Schema instance.
     * <p>
     * if not exists in cache, only create instance. (no cache)
     * </p>
     * <p>
     * source's supported type is under. <br>
     * {@code java.net.URL}<br>
     * {@code java.net.File}<br>
     * {@code java.net.Source}<br>
     * {@code java.net.Source[]}<br>
     * </p>
     * <p>
     * schema language is {@code javax.xml.XMLConstants}.W3C_XML_SCHEMA_NS_URI
     * </p>
     * 
     * @param source schema source.
     * @return Schema instance.
     */
    public static Schema getSchemaNoCache(final Object source) throws SAXException {
        return getSchemaNoCache(XMLConstants.W3C_XML_SCHEMA_NS_URI, source);
    }

    /**
     * Get Schema instance.
     * 
     * <p>
     * source's supported type is under. <br>
     * {@code java.net.URL}<br>
     * {@code java.net.File}<br>
     * {@code java.net.Source}<br>
     * {@code java.net.Source[]}<br>
     * </p>
     * 
     * @param schemaLanguage schema language.
     * @param source schema source.
     * @return Schema instance.
     * @throws SAXException if schema source is invalid.
     */
    public static Schema getSchema(final String schemaLanguage, final Object source) throws SAXException {
        return getSchema(schemaLanguage, source, Cache.CACHE);
    }

    /**
     * Get Schema instance.
     * <p>
     * if not exists in cache, only create instance. (no cache)
     * </p>
     * <p>
     * source's supported type is under. <br>
     * {@code java.net.URL}<br>
     * {@code java.net.File}<br>
     * {@code java.net.Source}<br>
     * {@code java.net.Source[]}<br>
     * </p>
     * 
     * @param schemaLanguage schema language.
     * @param source schema source.
     * @return Schema instance.
     * @throws SAXException if schema source is invalid.
     */
    public static Schema getSchemaNoCache(final String schemaLanguage, final Object source) throws SAXException {
        return getSchema(schemaLanguage, source, Cache.NO_CACHE);
    }

    /**
     * Get Schema instance.
     * 
     * @param schemaLanguage schema language.
     * @param source schema source.
     * @param doCache true is cache.
     * @return Schema instance.
     * @throws SAXException if schema source is invalid.
     */
    private static Schema getSchema(final String schemaLanguage, final Object source, final boolean doCache)
            throws SAXException {
        try {
            return cache.get(doCache, schemaLanguage, source);
        } catch (final NestedSAXException e) {
            throw e.causeSaxException;
        }
    }

    /**
     * Clear cache.
     */
    public static void clearCache() {
        cache.clear();
    }

    /**
     * Nested SAXException class.
     * <p>
     * Holds cause SAXException.
     * </p>
     * 
     * @since 1.0.0
     * @version 1.0.0
     * @author created by Kazuki Shimizu
     */
    private static class NestedSAXException extends RuntimeException {
        /**
         * serialVersion UID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * SaxException.
         */
        private final SAXException causeSaxException;

        /**
         * Constructor.
         * 
         * @param causeSaxException SAXException.
         */
        private NestedSAXException(final SAXException causeSaxException) {
            super(causeSaxException);
            this.causeSaxException = causeSaxException;
        }
    }

}
