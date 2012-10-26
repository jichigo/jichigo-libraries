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

/**
 * Stop Watch class.
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author created by Kazuki Shimizu
 */
public class StopWatch {

    /**
     * Default printer instance.
     */
    private static final StopWatchPrinter DEFAULT_PRINTER = new DefaultStopWatchPrinter(System.out);

    /**
     * New instance.
     * <p>
     * stop watch name is "default".
     * </p>
     * 
     * @return stop watch instance.
     */
    public static StopWatch newInstance() {
        return newInstance("default");
    }

    /**
     * New instance.
     * 
     * @param name stop watch name.
     * @return stop watch instance.
     */
    public static StopWatch newInstance(final String name) {
        return new StopWatch(name);
    }

    /**
     * name.
     */
    private final String name;

    /**
     * running flag.
     * <p>
     * true is running.
     * </p>
     */
    private boolean running;

    /**
     * start time.
     */
    private long startTime;

    /**
     * passed time.
     */
    private long passedTime;

    /**
     * last split time.
     */
    private long lastSplitTime;

    /**
     * split time list.
     */
    private final List<Long> splitTimeList = new ArrayList<Long>();

    /**
     * Constructor.
     * 
     * @param name stop watch name.
     */
    private StopWatch(final String name) {
        super();
        this.name = name;
    }

    /**
     * Start.
     */
    public synchronized void start() {
        if (!running) {
            running = true;
            startTime = System.nanoTime();
            lastSplitTime = startTime;
        }
    }

    /**
     * Stop.
     */
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

    /**
     * Split.
     * <p>
     * Save split time.
     * </p>
     */
    public synchronized void split() {
        if (running) {
            setSplit(System.nanoTime());
        }
    }

    /**
     * Set split time.
     * 
     * @param nowTime now time.
     */
    private void setSplit(final long nowTime) {
        splitTimeList.add(nowTime - lastSplitTime);
        lastSplitTime = nowTime;
    }

    /**
     * Clear.
     */
    public synchronized void clear() {
        if (!running) {
            startTime = 0;
            passedTime = 0;
            lastSplitTime = 0;
            splitTimeList.clear();
        }
    }

    /**
     * Get the passed time.
     * 
     * @return passed time.
     */
    public synchronized long getPassedTime() {
        if (running) {
            return passedTime + (System.nanoTime() - startTime);
        } else {
            return passedTime;
        }
    }

    /**
     * Get the formatted passed time.
     * 
     * @return formatted passed time.
     */
    public String getFormatPassedTime() {
        return formatString(getPassedTime());
    }

    /**
     * Get the formatted split time list.
     * 
     * @return formatted split time list.
     */
    public List<String> getFormatSplitTimeList() {
        final List<String> list = new ArrayList<String>();
        for (final Long splitTime : splitTimeList) {
            list.add(formatString(splitTime.longValue()));
        }
        return list;
    }

    /**
     * Format string.
     * <p>
     * format seconds.<br>
     * format) n.nnnnnnnnn<br>
     * e.g) nano's time is '1123456789' -> 1.123456789
     * </p>
     * 
     * @param nanoTime nano's time.
     * @return formatted time string.
     */
    private String formatString(final long nanoTime) {
        final TimeUnit nanoTimeUnit = TimeUnit.NANOSECONDS;
        return String.format("%d.%09d", nanoTimeUnit.toSeconds(nanoTime), nanoTimeUnit.toNanos(nanoTime) % 1000000000);
    }

    /**
     * Print.
     */
    public void print() {
        print(DEFAULT_PRINTER);
    }

    /**
     * Print to printer.
     * 
     * @param printer target printer.
     */
    public void print(final StopWatchPrinter printer) {
        printer.print(this);
    }

    /**
     * Printer interface.
     * 
     * @since 1.0.0
     * @version 1.0.0
     * @author created by Kazuki Shimizu
     */
    public interface StopWatchPrinter {
        void print(StopWatch stopWatch);
    }

    /**
     * Default Printer.
     * 
     * @since 1.0.0
     * @version 1.0.0
     * @author created by Kazuki Shimizu
     */
    public static class DefaultStopWatchPrinter implements StopWatchPrinter {

        /**
         * Print Stream.
         */
        private final PrintStream ps;

        /**
         * Constructor.
         * 
         * @param ps Print Stream.
         */
        public DefaultStopWatchPrinter(final PrintStream ps) {
            super();
            this.ps = ps;
        }

        /**
         * Print.
         * 
         * @param stopWatch stop watch.
         */
        public final void print(final StopWatch stopWatch) {
            final StringBuilder sb = new StringBuilder();
            appendHeader(sb, stopWatch.name);
            appendPassedTime(sb, stopWatch.getFormatPassedTime());
            final List<String> formattedSplitTimeList = stopWatch.getFormatSplitTimeList();
            if (!formattedSplitTimeList.isEmpty()) {
                appendSplitTimeHeader(sb);
                int size = stopWatch.splitTimeList.size();
                int length = 1;
                while (0 < (size = size / 10)) {
                    length++;
                }
                final DecimalFormat decimalFormat = new DecimalFormat(String.format("%0" + length + "d", 0));
                long position = 1;
                for (final String formattedSplitTime : formattedSplitTimeList) {
                    appendSplitTime(sb, decimalFormat.format(position), formattedSplitTime);
                    position++;
                }
                appendSplitTimeFooter(sb);
            }
            appendFooter(sb, stopWatch.name);
            ps.println(sb.toString());
        }

        /**
         * Append header.
         * 
         * @param sb target string builder.
         * @param name stop watch name.
         */
        protected void appendHeader(final StringBuilder sb, final String name) {
            sb.append("=================");
            appendLine(sb);
            sb.append("name : ").append(name);
            appendLine(sb);
            sb.append("=================");
            appendLine(sb);
        }

        /**
         * Append footer.
         * 
         * @param sb target string builder.
         * @param name stop watch name.
         */
        protected void appendFooter(final StringBuilder sb, final String name) {
            // do nothing.
        }

        /**
         * Append passed time.
         * 
         * @param sb target string builder.
         * @param passedTime passed time.
         */
        protected void appendPassedTime(final StringBuilder sb, final String passedTime) {
            sb.append(passedTime);
            appendLine(sb);
        }

        /**
         * Append split time header.
         * 
         * @param sb target string builder.
         */
        protected void appendSplitTimeHeader(final StringBuilder sb) {
            sb.append("---split passed time---");
            appendLine(sb);
        }

        /**
         * Append split time.
         * 
         * @param sb target string builder.
         * @param position split position.
         * @param splitPassedTime split passed time.
         */
        protected void appendSplitTime(final StringBuilder sb, final String position, final String splitPassedTime) {
            sb.append(position);
            sb.append(" : ");
            sb.append(splitPassedTime);
            sb.append("\r\n");
        }

        /**
         * Append split time footer.
         * 
         * @param sb target string builder.
         */
        private void appendSplitTimeFooter(final StringBuilder sb) {
            sb.append("-----------------------");
            appendLine(sb);
        }

        /**
         * Append line.
         * 
         * @param sb target string builder.
         */
        private final void appendLine(final StringBuilder sb) {
            sb.append("\r\n");
        }

    }

}
