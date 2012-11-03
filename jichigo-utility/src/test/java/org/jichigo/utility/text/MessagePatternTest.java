package org.jichigo.utility.text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MessagePatternTest {

    @Before
    public void setup() {
        MessagePattern.clearCache();
    }

    @Test
    public void getPattern_defaultLocale() {
        MessagePattern pattern1 = MessagePattern.getPattern("{0} {1,number} {1,date,yyyyMMdd(E)}");
        MessagePattern pattern2 = MessagePattern.getPattern("{0} {1,number} {1,date,yyyyMMdd(E)}");

        Assert.assertSame(pattern1, pattern2);
        Assert.assertEquals("string 1,234 19700101(–Ø)", pattern2.format("string", 1234, new Date(0)));

    }

    @Test
    public void getPattern_locale() {
        MessagePattern pattern1 = MessagePattern.getPattern("{0} {1,number} {1,date,yyyyMMdd(E)}", Locale.US);
        MessagePattern pattern2 = MessagePattern.getPattern("{0} {1,number} {1,date,yyyyMMdd(E)}", Locale.US);

        Assert.assertSame(pattern1, pattern2);
        Assert.assertEquals("string 1,234 19700101(Thu)", pattern2.format("string", 1234, new Date(0)));
    }

    @Test
    public void format_single_thread() {
        MessagePattern pattern = MessagePattern.getPattern("{0},{1,number,currency},{2,date,medium},{3,time,medium}");
        String actualFormattedValue = pattern.format("string", 1234, new Date(0), new Date(0));

        Assert.assertEquals("string,1,234,1970/01/01,9:00:00", actualFormattedValue);

    }

    @Test
    public void format_multi_thread() throws InterruptedException {

        final MessagePattern pattern = MessagePattern.getPattern("{0,date,yyyyMMddHHmmssSSS}");
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
    public void clearCache() {
        MessagePattern pattern1 = MessagePattern.getPattern("{0,date,yyyyMMdd}");
        MessagePattern.clearCache();
        MessagePattern pattern2 = MessagePattern.getPattern("{0,date,yyyyMMdd}");

        Assert.assertNotSame(pattern1, pattern2);
    }

}
