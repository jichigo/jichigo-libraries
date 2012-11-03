package org.jichigo.utility.text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NumberPatternTest {

    @Before
    public void setup() {
        NumberPattern.clearCache();
    }

    @Test
    public void getPattern() {
        NumberPattern pattern1 = NumberPattern.getPattern(",###");
        NumberPattern pattern2 = NumberPattern.getPattern(",###");

        Assert.assertSame(pattern1, pattern2);
        Assert.assertEquals("1,234,567", pattern2.format(1234567));

    }

    @Test
    public void format_single_thread() {
        NumberPattern pattern = NumberPattern.getPattern("###%");
        String actualFormattedValue = pattern.format(0.99D);

        Assert.assertEquals("99%", actualFormattedValue);

    }

    @Test
    public void format_multi_thread() throws InterruptedException {

        final NumberPattern pattern = NumberPattern.getPattern("000000");
        final int count = 10000;

        final List<String> actualFormattedValueList1 = new ArrayList<String>(count);
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                int number = 0;
                for (int i = 0; i < count; i++) {
                    actualFormattedValueList1.add(pattern.format(number));
                    number++;
                }
            }
        });

        final List<String> actualFormattedValueList2 = new ArrayList<String>(count);
        Thread thread2 = new Thread(new Runnable() {

            public void run() {
                int number = count;
                for (int i = 0; i < count; i++) {
                    actualFormattedValueList2.add(pattern.format(number));
                    number++;
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        DecimalFormat df = new DecimalFormat("000000");
        int expectedNumber = 0;
        for (String actualFormattedValue : actualFormattedValueList1) {
            Assert.assertEquals(df.format(expectedNumber), actualFormattedValue);
            expectedNumber++;
        }

        expectedNumber = count;
        for (String actualFormattedValue : actualFormattedValueList2) {
            Assert.assertEquals(df.format(expectedNumber), actualFormattedValue);
            expectedNumber++;
        }

    }

    @Test
    public void parse_single_thread() throws ParseException {
        NumberPattern pattern = NumberPattern.getPattern("###%");
        Number actualParsedValue = pattern.parse("94%");
        Assert.assertEquals(0.94D, actualParsedValue.doubleValue(), 0);
    }

    @Test
    public void parse_multi_thread() throws InterruptedException {

        final NumberPattern pattern = NumberPattern.getPattern("000000");
        final int count = 10000;

        final List<Number> actualParsedValueList1 = new ArrayList<Number>(count);
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                int number = 0;
                DecimalFormat df = new DecimalFormat("000000");
                try {
                    for (int i = 0; i < count; i++) {
                        actualParsedValueList1.add(pattern.parse(df.format(number)));
                        number++;
                    }
                } catch (ParseException e) {
                    Assert.fail(e.getMessage());
                }
            }
        });

        final List<Number> actualParsedValueList2 = new ArrayList<Number>(count);
        Thread thread2 = new Thread(new Runnable() {

            public void run() {
                int number = count;
                DecimalFormat df = new DecimalFormat("000000");
                try {
                    for (int i = 0; i < count; i++) {
                        actualParsedValueList2.add(pattern.parse(df.format(number)));
                        number++;
                    }
                } catch (ParseException e) {
                    Assert.fail(e.getMessage());
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        int expectedNumber = 0;
        for (Number actualParsedValue : actualParsedValueList1) {
            Assert.assertEquals(expectedNumber, actualParsedValue.intValue());
            expectedNumber++;
        }

        expectedNumber = count;
        for (Number actualParsedValue : actualParsedValueList2) {
            Assert.assertEquals(expectedNumber, actualParsedValue.intValue());
            expectedNumber++;
        }

    }

    @Test
    public void clearCache() {
        NumberPattern pattern1 = NumberPattern.getPattern("###,###");
        NumberPattern.clearCache();
        NumberPattern pattern2 = NumberPattern.getPattern("###,###");

        Assert.assertNotSame(pattern1, pattern2);
    }

}
