package org.jichigo.utility.exception;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

import javax.xml.bind.JAXBException;

import org.jichigo.utility.exception.ExceptionLogger.Level;
import org.junit.Before;
import org.junit.Test;

public class ExceptionLoggerTest {

    private ExceptionLogger testTarget;

    @Before
    public void setup() {
        LinkedHashMap<String, String> customCodeMap = new LinkedHashMap<String, String>();
        customCodeMap.put("NullPointerException", "e.cm.888");
        customCodeMap.put("ArrayIndexOutOfBoundsException", "i.cm.666");
        customCodeMap.put("RuntimeException", "w.cm.777");

        LinkedHashMap<String, Level> customLevelMap = new LinkedHashMap<String, Level>();
        customLevelMap.put("e.", Level.error);
        customLevelMap.put("w.", Level.warn);
        customLevelMap.put("i.", Level.info);

        testTarget = new ExceptionLogger();
        testTarget.setDefaultCode("e.cm.999");
        testTarget.setDefaultLevel(Level.error);
        testTarget.setCustomCodeMap(customCodeMap);
        testTarget.setCustomLevelMap(customLevelMap);
    }

    @Test
    public void warn01() throws JAXBException {

        testTarget.log(new NullPointerException("1"));
        testTarget.log(new IllegalArgumentException("2"));
        testTarget.log(new FileNotFoundException("3"));
        testTarget.log(new ArrayIndexOutOfBoundsException("4"));

        testTarget.warn(new ArrayIndexOutOfBoundsException("5"));
        testTarget.error(new IllegalArgumentException("6"));
        testTarget.info(new NullPointerException("7"));

    }

}
