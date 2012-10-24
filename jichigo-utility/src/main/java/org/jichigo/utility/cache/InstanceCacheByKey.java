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
 * key is getInstance method's argument.
 * </p>
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author created by Kazuki Shimizu
 */
public abstract class InstanceCacheByKey<T> implements InstanceCache<T> {

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
    public T get(final Object... objects) {
        final String cachekey = generateCacheKey(objects);
        T instance = cache.get(cachekey);
        if (instance == null) {
            synchronized (cachekey.intern()) {
                instance = cache.get(cachekey);
                if (instance == null) {
                    instance = create(objects);
                    cache.put(cachekey, instance);
                }
            }
        }
        return instance;
    }

    /**
     * Generate cache key.
     * 
     * @param objects cache target objects.
     * @return cache key.
     */
    protected String generateCacheKey(final Object... objects) {
        final StringBuilder cachekeySb = new StringBuilder();
        for (final Object object : objects) {
            cachekeySb.append(object).append("_");
        }
        if (0 < cachekeySb.length()) {
            cachekeySb.deleteCharAt(cachekeySb.length() - 1);
        }
        return cachekeySb.toString();
    }

    /**
     * Create instance.
     * 
     * @param args
     */
    protected abstract T create(final Object... args);

}
