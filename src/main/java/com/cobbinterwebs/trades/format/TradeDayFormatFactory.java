package com.cobbinterwebs.trades.format;

////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////

import com.cobbinterwebs.locale.DisplayKeys;

public class TradeDayFormatFactory {

    public enum FORMATTER {TABULAR, CSV}


    public static TradeDayPresentation getFormatter(FORMATTER requestedFormatter) {
        TradeDayPresentation rVal = null;
        switch (requestedFormatter) {
            case CSV:
                rVal = new TradeDayAsCSVString();
                break;
            case TABULAR:
                rVal = new TradeDayAsTabular();
                break;
            default:
                throw new IllegalArgumentException(DisplayKeys.get(DisplayKeys.ERROR_FORMATTER_UNKNOWN, requestedFormatter));
        }

        return rVal;
    }

    /**
     *
     * @return a formatter with the default delimiter, comma.
     */
    public static TradeDayPresentation getCsvFormatter() {
        return getFormatter(FORMATTER.CSV);
    }


    /**
     *
     * @return a formatter with the default delimiter, colon.
     */
    public static TradeDayPresentation getTabularFormatter() {
        return getFormatter(FORMATTER.TABULAR);
    }
}
