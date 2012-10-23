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
package org.jichigo.utility.timer;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StopWatch {

    private static final StopWatchPrinter DEFAULT_PRINTER = new DefaultStopWatchPrinter(System.out);

    public static StopWatch newInstance() {
        return newInstance("default");
    }

    public static StopWatch newInstance(String name) {
        return new StopWatch(name);
    }

    private final String name;
    private boolean running;
    private long startTime;
    private long passedTime;
    private long lastSplitTime;
    private final List<Long> splitTimeList = new ArrayList<Long>();

    private StopWatch(String name) {
        super();
        this.name = name;
    }

    public synchronized void start() {
        if (!running) {
            running = true;
            startTime = System.nanoTime();
            lastSplitTime = startTime;
        }
    }

    public synchronized void stop() {
        if (running) {
            running = false;
            final long nowTime = System.nanoTime();
            passedTime = passedTime + (nowTime - startTime);
            if (!splitTimeList.isEmpty()) {
                setSplit(nowTime);
            }
        }
    }

    public synchronized void split() {
        if (running) {
            setSplit(System.nanoTime());
        }
    }

    private void setSplit(final long nowTime) {
        splitTimeList.add(nowTime - lastSplitTime);
        lastSplitTime = nowTime;
    }

    public synchronized void clear() {
        if (!running) {
            startTime = 0;
            passedTime = 0;
            lastSplitTime = 0;
            splitTimeList.clear();
        }
    }

    public synchronized long getPassedTime() {
        if (running) {
            return passedTime + (System.nanoTime() - startTime);
        } else {
            return passedTime;
        }
    }

    public String getFormatPassedTime() {
        return formatString(getPassedTime());
    }

    public List<String> getFormatSplitTimeList() {
        final List<String> list = new ArrayList<String>();
        for (final Long splitTime : splitTimeList) {
            list.add(formatString(splitTime.longValue()));
        }
        return list;
    }

    private String formatString(final long nanoTime) {
        final TimeUnit nanoTimeUnit = TimeUnit.NANOSECONDS;
        return String.format("%d.%09d", nanoTimeUnit.toSeconds(nanoTime), nanoTimeUnit.toNanos(nanoTime) % 1000000000);
    }

    public void print() {
        print(DEFAULT_PRINTER);
    }

    public void print(StopWatchPrinter printer) {
        printer.print(this);
    }

    public interface StopWatchPrinter {
        void print(StopWatch stopWatch);
    }

    public static class DefaultStopWatchPrinter implements StopWatchPrinter {

        private final PrintStream ps;

        public DefaultStopWatchPrinter(final PrintStream ps) {
            super();
            this.ps = ps;
        }

        public final void print(final StopWatch stopWatch) {
            final StringBuilder sb = new StringBuilder();
            appendHeader(sb, stopWatch.name);
            appendPassedTime(sb, stopWatch.getFormatPassedTime());
            List<String> formattedSplitTimeList = stopWatch.getFormatSplitTimeList();
            if (!formattedSplitTimeList.isEmpty()) {
                appendSplitTimeHeader(sb);
                int size = stopWatch.splitTimeList.size();
                int length = 1;
                while (0 < (size = size / 10)) {
                    length++;
                }
                final DecimalFormat decimalFmt = new DecimalFormat(String.format("%0" + length + "d", 0));
                long position = 1;
                for (final String formattedSplitTime : formattedSplitTimeList) {
                    appendSplitTime(sb, decimalFmt.format(position), formattedSplitTime);
                    position++;
                }
                appendSplitTimeFotter(sb);
            }
            appendFotter(sb, stopWatch.name);
            ps.println(sb.toString());
        }

        private void appendSplitTimeFotter(final StringBuilder sb) {
            sb.append("-----------------------");
            appendLine(sb);
        }

        protected void appendHeader(final StringBuilder sb, final String name) {
            sb.append("=================");
            appendLine(sb);
            sb.append("name : ").append(name);
            appendLine(sb);
            sb.append("=================");
            appendLine(sb);
        }

        protected void appendFotter(final StringBuilder sb, final String name) {
            // do nothing.
        }

        protected void appendPassedTime(final StringBuilder sb, final String passedTime) {
            sb.append(passedTime);
            appendLine(sb);
        }

        protected void appendSplitTimeHeader(final StringBuilder sb) {
            sb.append("---split passed time---");
            appendLine(sb);
        }

        protected void appendSplitTime(final StringBuilder sb, final String position, final String splitPassedTime) {
            sb.append(position);
            sb.append(" : ");
            sb.append(splitPassedTime);
            sb.append("\r\n");
        }

        private final StringBuilder appendLine(final StringBuilder sb) {
            sb.append("\r\n");
            return sb;
        }

    }

}
