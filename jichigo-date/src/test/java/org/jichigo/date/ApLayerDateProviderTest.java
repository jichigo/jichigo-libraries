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
package org.jichigo.date;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test Case of ApLayerDateFactory.
 * 
 * @see {@link org.junit.*}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ApLayerDateProvider.class })
public class ApLayerDateProviderTest {

    /**
     * if success,<br>
     * 1. verify get date.
     */
    @Test
    public void provideSuccess() throws InterruptedException {

        // --------
        // setup.
        // --------
        PowerMockito.mockStatic(System.class);

        Date expectedDate = new Date();

        PowerMockito.when(System.currentTimeMillis()).thenReturn(expectedDate.getTime());

        Thread.sleep(50);

        // --------
        // test
        // --------
        ApLayerDateProvider target = new ApLayerDateProvider();
        Date actualDate = target.provide();

        // --------
        // assert
        // --------
        Assert.assertThat(actualDate.getTime(), CoreMatchers.is(expectedDate.getTime()));

    }

}
