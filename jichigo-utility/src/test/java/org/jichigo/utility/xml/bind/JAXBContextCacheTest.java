package org.jichigo.utility.xml.bind;

import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.jichigo.utility.xml.bind.jAXBContextTest.bean.Model;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JAXBContextCacheTest {

    @Before
    public void setup() {
        JAXBContextCache.clearCache();
    }

    @Test
    public void getJAXBContext() throws JAXBException {

        JAXBContext context1 = JAXBContextCache.getJAXBContext(Model.class);
        JAXBContext context2 = JAXBContextCache.getJAXBContext(Model.class);

        Assert.assertSame(context1, context2);
    }

    @Test
    public void getJAXBContext_multiple() throws JAXBException {

        JAXBContext context1 = JAXBContextCache.getJAXBContext(Model.class);
        JAXBContext context2 = JAXBContextCache.getJAXBContext(Object.class);

        Assert.assertNotSame(context1, context2);
    }

    @Test
    public void getJAXBContext_null() throws JAXBException {
        try {
            JAXBContextCache.getJAXBContext(null);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("classeToBeBound is null.", e.getMessage());
            Assert.assertNull(e.getCause());
        }
    }

    @Test
    public void getJAXBContext_JAXBContextException() {
        try {
            JAXBContextCache.getJAXBContext(Map.class);
            Assert.fail("not occur JAXBException");
        } catch (JAXBException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void getJAXBContextNoCache_not_exists_cache() throws JAXBException {

        JAXBContext context1 = JAXBContextCache.getJAXBContextNoCache(Model.class);
        JAXBContext context2 = JAXBContextCache.getJAXBContextNoCache(Model.class);

        Assert.assertNotSame(context1, context2);
    }

    @Test
    public void getJAXBContextNoCache_exists_cache() throws JAXBException {

        JAXBContext context1 = JAXBContextCache.getJAXBContext(Model.class);
        JAXBContext context2 = JAXBContextCache.getJAXBContextNoCache(Model.class);

        Assert.assertSame(context1, context2);
    }

    @Test
    public void getJAXBContextNoCache_JAXBContextException() {
        try {
            JAXBContextCache.getJAXBContextNoCache(Map.class);
            Assert.fail("not occur JAXBException");
        } catch (JAXBException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void getJAXBContextNoCache_null() throws JAXBException {
        try {
            JAXBContextCache.getJAXBContextNoCache(null);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("classeToBeBound is null.", e.getMessage());
            Assert.assertNull(e.getCause());
        }
    }

    @Test
    public void clearCache() throws JAXBException {

        JAXBContext context1 = JAXBContextCache.getJAXBContext(Model.class);
        JAXBContextCache.clearCache();
        JAXBContext context2 = JAXBContextCache.getJAXBContext(Model.class);

        Assert.assertNotSame(context1, context2);
    }
}
