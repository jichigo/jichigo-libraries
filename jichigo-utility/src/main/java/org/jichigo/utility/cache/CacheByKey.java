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
package org.jichigo.utility.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Object cache by key.
 * <p>
 * key is getInstance method's argument.<br>
 * this class's instance is thread safe.
 * </p>
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author created by Kazuki Shimizu
 */
public abstract class CacheByKey<T> implements LazyCache<T> {

    /**
     * Keys enum.
     * 
     * @since 1.0.0
     * @version 1.0.0
     * @author Kazuki Shimizu
     */
    private static enum Keys {
        /**
         * Default Key.
         */
        DEFAULT(""),
        /**
         * Null Key.
         */
        NULL("null");

        /**
         * Key's Value.
         */
        private String value;

        /**
         * Constructor.
         * 
         * @param value Key's value.
         */
        private Keys(String value) {
            this.value = value;
        }

        /**
         * Get value.
         * 
         * @return Key's Value.
         */
        public String value() {
            return value;
        }

    }

    /**
     * Key Separator.
     */
    private static final String KEY_SEPARATOR = "_";

    /**
     * instance cache.
     */
    private final ConcurrentMap<String, T> cache = new ConcurrentHashMap<String, T>();

    /**
     * Put instance.
     * 
     * @param objects cache target objects.
     * @return old cache instance.
     */
    public T create(final Object... objects) {
        final String cachekey = generateCacheKey(objects);
        return cache.put(cachekey, initialValue(objects));
    }

    /**
     * Get instance.
     * 
     * @param objects cache target objects.
     * @return instance.
     */
    public T get(final Object... objects) {
        final String cachekey = generateCacheKey(objects);
        T instance = cache.get(cachekey);
        if (instance == null) {
            instance = initialValue(objects);
        }
        return instance;
    }

    /**
     * Get instance.
     * 
     * @param objects cache target objects.
     * @return instance.
     */
    public T getOrCreate(final Object... objects) {
        final String cachekey = generateCacheKey(objects);
        T instance = cache.get(cachekey);
        if (instance == null) {
            synchronized (cachekey.intern()) {
                instance = cache.get(cachekey);
                if (instance == null) {
                    instance = initialValue(objects);
                    cache.put(cachekey, instance);
                }
            }
        }
        return instance;
    }

    /**
     * Clear cache.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Generate cache key.
     * 
     * @param objects cache target objects.
     * @return cache key.
     */
    protected String generateCacheKey(final Object... objects) {
        if (objects == null || objects.length == 0) {
            return Keys.DEFAULT.value();
        }
        if (objects.length == 1) {
            return objects[0] == null ? Keys.NULL.value() : objects[0].toString();
        }
        final StringBuilder cachekeyStrBuilder = new StringBuilder();
        for (int index = 0; index < objects.length; index++) {
            cachekeyStrBuilder.append(objects[index]);
            if (index < (objects.length - 1)) {
                cachekeyStrBuilder.append(KEY_SEPARATOR);
            }
        }
        return cachekeyStrBuilder.toString();
    }

    /**
     * Create instance.
     * 
     * @param args cache target objects.
     */
    protected abstract T initialValue(final Object... args);

}
