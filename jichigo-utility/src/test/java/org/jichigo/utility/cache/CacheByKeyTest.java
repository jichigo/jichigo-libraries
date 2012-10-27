package org.jichigo.utility.cache;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

public class CacheByKeyTest {

    @Test
    public void get_cache() {

        Cache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.get(Cache.CACHE, "key");
        Object actualValue2 = cache.get(Cache.CACHE, "key");

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void get_cache_multiple() {

        Cache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.get(Cache.CACHE, "key");
        Object actualValue2 = cache.get(Cache.CACHE, "key2");

        Assert.assertNotSame(actualValue1, actualValue2);

    }

    @Test
    public void get_nocache_not_exists_cache() {

        Cache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.get(Cache.NO_CACHE, "key1");
        Object actualValue2 = cache.get(Cache.NO_CACHE, "key1");

        Assert.assertNotSame(actualValue1, actualValue2);

    }

    @Test
    public void get_nocache_exists_cache() {

        Cache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.get(Cache.CACHE, "key1");
        Object actualValue2 = cache.get(Cache.NO_CACHE, "key1");

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void get_key_multiple() {

        Cache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.get(Cache.CACHE, "key", "key1");
        Object actualValue2 = cache.get(Cache.CACHE, "key", "key1");

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void get_key_null() {

        Cache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.get(Cache.CACHE, (String) null);
        Object actualValue2 = cache.get(Cache.CACHE, (String) null);

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void get_key_objects_empty() {

        Cache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.get(Cache.CACHE);
        Object actualValue2 = cache.get(Cache.CACHE);

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void get_key_objects_null() {

        Cache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.get(Cache.CACHE, (Object[]) null);
        Object actualValue2 = cache.get(Cache.CACHE, (Object[]) null);

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void get_key_objects_null_and_empty() {

        Cache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.get(Cache.CACHE, (Object[]) null);
        Object actualValue2 = cache.get(Cache.CACHE);

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void get_cache_multi_thread() throws InterruptedException {

        final AtomicInteger initValueCounter = new AtomicInteger(0);
        final Cache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                initValueCounter.incrementAndGet();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Assert.fail(e.getMessage());
                }
                return new Object();
            }
        };

        Thread thread1 = new Thread(new Runnable() {

            public void run() {
                cache.get(Cache.CACHE, new String("key1"));
            }
        });
        Thread thread2 = new Thread(new Runnable() {

            public void run() {
                cache.get(Cache.CACHE, new String("key1"));
            }
        });

        long startTime = System.currentTimeMillis();
        thread1.start();
        Thread.sleep(250);
        thread2.start();
        thread1.join();
        thread2.join();
        long endTime = System.currentTimeMillis();
        long passedTime = endTime - startTime;

        Assert.assertEquals(1, initValueCounter.get());
        Assert.assertTrue(passedTime < 550);

    }

    @Test
    public void get_no_cache_multi_thread() throws InterruptedException {

        final AtomicInteger initValueCounter = new AtomicInteger(0);
        final Cache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                initValueCounter.incrementAndGet();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Assert.fail(e.getMessage());
                }
                return new Object();
            }
        };

        Thread thread1 = new Thread(new Runnable() {

            public void run() {
                cache.get(Cache.NO_CACHE, new String("key1"));
            }
        });
        Thread thread2 = new Thread(new Runnable() {

            public void run() {
                cache.get(Cache.NO_CACHE, new String("key1"));
            }
        });

        long startTime = System.currentTimeMillis();
        thread1.start();
        Thread.sleep(250);
        thread2.start();
        thread1.join();
        thread2.join();
        long endTime = System.currentTimeMillis();
        long passedTime = endTime - startTime;

        Assert.assertEquals(2, initValueCounter.get());
        Assert.assertTrue(750 <= passedTime);

    }

    @Test
    public void clear() {

        final Cache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object object1 = cache.get(Cache.CACHE, "key");
        cache.clear();
        Object object2 = cache.get(Cache.CACHE, "key");

        Assert.assertNotSame(object1, object2);

    }

}
