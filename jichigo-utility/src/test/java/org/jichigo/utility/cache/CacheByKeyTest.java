package org.jichigo.utility.cache;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

public class CacheByKeyTest {

    @Test
    public void getOrCreate() {

        LazyCache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.getOrCreate("key");
        Object actualValue2 = cache.getOrCreate("key");

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void getOrCreate_multiple() {

        LazyCache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.getOrCreate("key");
        Object actualValue2 = cache.getOrCreate("key2");

        Assert.assertNotSame(actualValue1, actualValue2);

    }

    @Test
    public void get_key_multiple() {

        LazyCache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.getOrCreate("key", "key1");
        Object actualValue2 = cache.getOrCreate("key", "key1");

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void getOrCreate_key_null() {

        LazyCache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.getOrCreate((String) null);
        Object actualValue2 = cache.getOrCreate((String) null);

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void getOrCreate_key_objects_empty() {

        LazyCache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.getOrCreate();
        Object actualValue2 = cache.getOrCreate();

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void getOrCreate_key_objects_null() {

        LazyCache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.getOrCreate((Object[]) null);
        Object actualValue2 = cache.getOrCreate((Object[]) null);

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void getOrCreate_key_objects_null_and_empty() {

        LazyCache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object actualValue1 = cache.getOrCreate((Object[]) null);
        Object actualValue2 = cache.getOrCreate();

        Assert.assertSame(actualValue1, actualValue2);

    }

    @Test
    public void getOrCreate_multi_thread() throws InterruptedException {

        final AtomicInteger initValueCounter = new AtomicInteger(0);
        final LazyCache<Object> cache = new CacheByKey<Object>() {
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
                cache.getOrCreate(new String("key1"));
            }
        });
        Thread thread2 = new Thread(new Runnable() {

            public void run() {
                cache.getOrCreate(new String("key1"));
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
    public void clear() {

        final LazyCache<Object> cache = new CacheByKey<Object>() {
            @Override
            protected Object initialValue(Object... args) {
                return new Object();
            }
        };

        Object object1 = cache.getOrCreate("key");
        cache.clear();
        Object object2 = cache.getOrCreate("key");

        Assert.assertNotSame(object1, object2);

    }

}
