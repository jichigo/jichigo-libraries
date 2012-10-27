package org.jichigo.utility.regex;

import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PatternCacheTest {

    @Before
    public void setup() {
        PatternCache.clearCache();
    }

    @Test
    public void getPattern() throws JAXBException {

        Pattern pattern1 = PatternCache.getPattern("[0-9]");
        Pattern pattern2 = PatternCache.getPattern("[0-9]");

        Assert.assertSame(pattern1, pattern2);
    }

    @Test
    public void getPatternNoCache_not_exists_cache() throws JAXBException {

        Pattern pattern1 = PatternCache.getPatternNoCache("[0-9]");
        Pattern pattern2 = PatternCache.getPatternNoCache("[0-9]");

        Assert.assertNotSame(pattern1, pattern2);
    }

    @Test
    public void getPatternNoCache_exists_cache() throws JAXBException {

        Pattern pattern1 = PatternCache.getPattern("[a-z]");
        Pattern pattern2 = PatternCache.getPatternNoCache("[a-z]");

        Assert.assertSame(pattern1, pattern2);
    }

    @Test
    public void clearCache() throws JAXBException {

        Pattern pattern1 = PatternCache.getPattern("[A-Z]");
        PatternCache.clearCache();
        Pattern pattern2 = PatternCache.getPattern("[A-Z]");

        Assert.assertNotSame(pattern1, pattern2);
    }

}
