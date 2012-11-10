package org.jichigo.utility.exception;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

import javax.xml.bind.JAXBException;

import org.jichigo.utility.exception.ExceptionLogger.Level;
import org.junit.Before;
import org.junit.Test;

public class ExceptionLoggerTest {

    @Before
    public void setup() {
    }

    @Test
    public void warn01() throws JAXBException {

        LinkedHashMap<String, String> customCodeMap = new LinkedHashMap<String, String>();
        customCodeMap.put("NullPointerException", "e.cm.888");
        customCodeMap.put("RuntimeException", "w.cm.777");

        LinkedHashMap<String, Level> customLevelMap = new LinkedHashMap<String, Level>();
        customLevelMap.put("e.", Level.error);
        customLevelMap.put("w.", Level.warn);

        ExceptionLogger logger = new ExceptionLogger();
        logger.setDefaultCode("e.cm.999");
        logger.setDefaultLevel(Level.error);
        logger.setCustomCodeMap(customCodeMap);
        logger.setCustomLevelMap(customLevelMap);

        logger.log(new NullPointerException("1"));
        logger.log(new IllegalArgumentException("2"));

        logger.log(new FileNotFoundException("3"));

    }

}
