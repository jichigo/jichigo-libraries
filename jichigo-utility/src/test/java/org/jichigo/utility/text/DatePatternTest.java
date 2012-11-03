package org.jichigo.utility.text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatePatternTest {

    @Before
    public void setup() {
        DatePattern.clearCache();
    }

    @Test
    public void getPattern_defaultLocale() {
        DatePattern pattern1 = DatePattern.getPattern("yyyyMMdd(E)");
        DatePattern pattern2 = DatePattern.getPattern("yyyyMMdd(E)");

        Assert.assertSame(pattern1, pattern2);
        Assert.assertEquals("19700101(–Ø)", pattern2.format(new Date(0)));

    }

    @Test
    public void getPattern_locale() {
        DatePattern pattern1 = DatePattern.getPattern("yyyyMMdd(E)", Locale.US);
        DatePattern pattern2 = DatePattern.getPattern("yyyyMMdd(E)", Locale.US);

        Assert.assertSame(pattern1, pattern2);
        Assert.assertEquals("19700101(Thu)", pattern2.format(new Date(0)));
    }

    @Test
    public void format_single_thread() {
        DatePattern pattern = DatePattern.getPattern("yyyyMMdd");
        Date targetDate = new Date(0);
        String actualFormattedValue = pattern.format(targetDate);

        Assert.assertEquals("19700101", actualFormattedValue);

    }

    @Test
    public void format_multi_thread() throws InterruptedException {

        final DatePattern pattern = DatePattern.getPattern("yyyyMMddHHmmssSSS");
        final int count = 10000;

        final List<String> actualFormattedValueList1 = new ArrayList<String>(count);
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                int time = 0;
                for (int i = 0; i < count; i++) {
                    actualFormattedValueList1.add(pattern.format(new Date(time)));
                    time++;
                }
            }
        });

        final List<String> actualFormattedValueList2 = new ArrayList<String>(count);
        Thread thread2 = new Thread(new Runnable() {

            public void run() {
                int time = count;
                for (int i = 0; i < count; i++) {
                    actualFormattedValueList2.add(pattern.format(new Date(time)));
                    time++;
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        int expectedTime = 0;
        for (String actualFormattedValue : actualFormattedValueList1) {
            Assert.assertEquals(df.format(new Date(expectedTime)), actualFormattedValue);
            expectedTime++;
        }

        expectedTime = count;
        for (String actualFormattedValue : actualFormattedValueList2) {
            Assert.assertEquals(df.format(new Date(expectedTime)), actualFormattedValue);
            expectedTime++;
        }

    }

    @Test
    public void parse_single_thread() throws ParseException {
        DatePattern pattern = DatePattern.getPattern("yyyyMMdd");
        Date actualparsedValue = pattern.parse("20121027");

        Assert.assertEquals(java.sql.Date.valueOf("2012-10-27").getTime(), actualparsedValue.getTime());

    }

    @Test
    public void parse_multi_thread() throws InterruptedException {

        final DatePattern pattern = DatePattern.getPattern("yyyyMMddHHmmssSSS");
        final int count = 10000;

        final List<Date> actualParsedValueList1 = new ArrayList<Date>(count);
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                int time = 0;
                try {
                    for (int i = 0; i < count; i++) {
                        actualParsedValueList1.add(pattern.parse(df.format(new Date(time))));
                        time++;
                    }
                } catch (ParseException e) {
                    Assert.fail(e.getMessage());
                }
            }
        });

        final List<Date> actualParsedValueList2 = new ArrayList<Date>(count);
        Thread thread2 = new Thread(new Runnable() {

            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                int time = count;
                try {
                    for (int i = 0; i < count; i++) {
                        actualParsedValueList2.add(pattern.parse(df.format(new Date(time))));
                        time++;
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

        int expectedTime = 0;
        for (Date actualParsedValue : actualParsedValueList1) {
            Assert.assertEquals(new Date(expectedTime).getTime(), actualParsedValue.getTime());
            expectedTime++;
        }

        expectedTime = count;
        for (Date actualParsedValue : actualParsedValueList2) {
            Assert.assertEquals(new Date(expectedTime).getTime(), actualParsedValue.getTime());
            expectedTime++;
        }

    }

    @Test
    public void parse_invalid_dateString() {
        DatePattern pattern = DatePattern.getPattern("yyyyMMdd");
        try {
            pattern.parse("20120230");
            Assert.fail("not occur ParseException.");
        } catch (ParseException e) {
            Assert.assertEquals("Unparseable date: \"20120230\"", e.getMessage());
        }

    }

    @Test
    public void clearCache() {
        DatePattern pattern1 = DatePattern.getPattern("yyyyMMdd");
        DatePattern.clearCache();
        DatePattern pattern2 = DatePattern.getPattern("yyyyMMdd");

        Assert.assertNotSame(pattern1, pattern2);
    }

}
