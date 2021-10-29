package com.cobbinterwebs.trades.format;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.cobbinterwebs.locale.DisplayKeys;

////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////

import com.cobbinterwebs.trades.TradeWindow;

public class TradeMonthAsTabular {
    NumberFormat percentageFormatter = new DecimalFormat("#.0%");
    NumberFormat shareVolumeFormatter = new DecimalFormat("#,###");
    NumberFormat usdFormatter = new DecimalFormat("$#,##0.00");
    NumberFormat usdTripsFormatter =    new DecimalFormat("$#,##0.000###");
    public String formatTradeMonth(TradeWindow pMonth) {
        String rVal =
                DisplayKeys.get(DisplayKeys.SUMMARY_REC_SEPARATOR) + "\n" +
                DisplayKeys.get(DisplayKeys.SUMMARY_REC_SEPARATOR) + "\n" +
                DisplayKeys.get(DisplayKeys.SUMMARY_OVERALL_HEADER) + "\n" +
                DisplayKeys.get(DisplayKeys.SUMMARY_MONTH_VWAP, usdTripsFormatter.format(pMonth.getAveragePrice())) + "\n" +
                DisplayKeys.get(DisplayKeys.SUMMARY_VOLUME,shareVolumeFormatter.format(pMonth.getVolume())) + "\n" +
                DisplayKeys.get(DisplayKeys.SUMMARY_BUY_VOL, shareVolumeFormatter.format(pMonth.getBuyVolume())) + "\n" +
                DisplayKeys.get(DisplayKeys.SUMMARY_SELL_VOL, shareVolumeFormatter.format(pMonth.getSellVolume())) + "\n" +
                DisplayKeys.get(DisplayKeys.SUMMARY_OTHER_VOL, shareVolumeFormatter.format(pMonth.getUnknownVolume())) + "\n" +
                DisplayKeys.get(DisplayKeys.SUMMARY_DOLLAR_VOL, usdFormatter.format(pMonth.getDollarVolume())) + "\n" +
                DisplayKeys.get(DisplayKeys.SUMMARY_BUY_DOLLAR_VOL, usdFormatter.format(pMonth.getBuyDollarVolume())) + "\n" +
                DisplayKeys.get(DisplayKeys.SUMMARY_SELL_DOLLAR_VOL, usdFormatter.format(pMonth.getSellDollarVolume())) + "\n" +
                DisplayKeys.get(DisplayKeys.SUMMARY_OTHER_DOLLAR_VOL, usdFormatter.format(pMonth.getUnknownDollarVolume())) + "\n" +
                DisplayKeys.get(DisplayKeys.SUMMARY_T_TRADE_COUNT, pMonth.getTeeTradeCount());

        return rVal;
    }
}
