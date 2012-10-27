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
public abstract class CacheByKey<T> implements Cache<T> {

    /**
     * instance cache.
     */
    private final ConcurrentMap<String, T> cache = new ConcurrentHashMap<String, T>();

    /**
     * Get instance.
     * 
     * @param objects cache target objects.
     * @return instance.
     */
    public T get(final boolean doCache, final Object... objects) {
        final String cachekey = generateCacheKey(objects);
        T instance = cache.get(cachekey);
        if (instance == null) {
            if (doCache) {
                synchronized (cachekey.intern()) {
                    instance = cache.get(cachekey);
                    if (instance == null) {
                        instance = initialValue(objects);
                        cache.put(cachekey, instance);
                    }
                }
            } else {
                instance = initialValue(objects);
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
            return "";
        } else if (objects.length == 1) {
            return objects[0] == null ? "null" : objects[0].toString();
        } else {
            final StringBuilder cachekeySb = new StringBuilder();
            for (final Object object : objects) {
                cachekeySb.append(object).append("_");
            }
            cachekeySb.deleteCharAt(cachekeySb.length() - 1);
            return cachekeySb.toString();
        }
    }

    /**
     * Create instance.
     * 
     * @param args cache target objects.
     */
    protected abstract T initialValue(final Object... args);

}
