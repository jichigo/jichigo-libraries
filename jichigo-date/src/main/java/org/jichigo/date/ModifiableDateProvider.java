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

/**
 * Modifiable data provider.
 * <p>
 * if want date modify, please inject custom date modifier. default is not modified.
 * </p>
 * 
 * @since 1.0.0
 * @version 1.0.0
 * @author created by Kazuki Shimizu
 */
public abstract class ModifiableDateProvider implements DateProvider {

    /**
     * Date modifier.
     */
    protected DateModifier dateModifier = new DoNotAnythingDateModifier();

    /**
     * Provide date.
     * 
     * @return date.
     */
    public Date provide() {
        Date newDate = newDate();
        return dateModifier.modify(newDate);
    }

    /**
     * New date.
     * 
     * @return date.
     */
    protected abstract Date newDate();

    /**
     * Inject date modifier.
     * 
     * @param dateModifier date modifier.
     */
    public void setDateModifier(DateModifier dateModifier) {
        this.dateModifier = dateModifier;
    }

    /**
     * Don't anything date modifier.
     */
    protected class DoNotAnythingDateModifier implements DateModifier {
        public Date modify(Date targetDate) {
            return targetDate;
        }
    }

}
