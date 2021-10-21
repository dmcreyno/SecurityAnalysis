package com.cobbinterwebs.trades.format;

////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////

import com.cobbinterwebs.trades.ITradeDay;
import com.cobbinterwebs.trades.ITradeRecord;
import com.cobbinterwebs.trades.config.Configuration;
import com.cobbinterwebs.locale.DisplayKeys;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 */
public class TradeDayAsTabular implements TradeDayPresentation {

    public TradeDayAsTabular() {}


    @Override
    public String formatTradeDay(ITradeDay aTradeDay) {
        if(aTradeDay.getTradeList().isEmpty()) {
            return  "No trades recorded.";
        }

        // Create some formatters for the money and percentages and share volume
        NumberFormat percentageFormatter = new DecimalFormat("0.0##%");
        NumberFormat shareVolumeFormatter = new DecimalFormat("#,###");
        NumberFormat usdFormatter = new DecimalFormat("$#,##0.00");
        NumberFormat usdTripsFormatter =    new DecimalFormat("$#,##0.000###");


        String rVal =
                DisplayKeys.get(DisplayKeys.SUMMARY_REC_SEPARATOR) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_HEADER, aTradeDay.getDateStr()) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_AVG_PRICE,usdTripsFormatter.format(aTradeDay.getAveragePrice())) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_VOLUME,shareVolumeFormatter.format(aTradeDay.getVolume())) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_BUY_VOL, shareVolumeFormatter.format(aTradeDay.getBuyVolume())) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_SELL_VOL, shareVolumeFormatter.format(aTradeDay.getSellVolume())) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_OTHER_VOL, shareVolumeFormatter.format(aTradeDay.getUnknownVolume())) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_DOLLAR_VOL, usdFormatter.format(aTradeDay.getDollarVolume())) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_BUY_DOLLAR_VOL, usdFormatter.format(aTradeDay.getBuyDollarVolume())) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_SELL_DOLLAR_VOL, usdFormatter.format(aTradeDay.getSellDollarVolume())) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_OTHER_DOLLAR_VOL, usdFormatter.format(aTradeDay.getUnknownDollarVolume())) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_BUY_DOLLAR_VOL_PCT, percentageFormatter.format(aTradeDay.getPctBuyDolVol())) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_SELL_DOLLAR_VOL_PCT, percentageFormatter.format(aTradeDay.getPctSellDolVol())) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_OTHER_DOLLAR_VAL_PCT, percentageFormatter.format(aTradeDay.getPctUnknownDolVol())) + "\n" +
                        DisplayKeys.get(DisplayKeys.SUMMARY_T_TRADE_COUNT, aTradeDay.getTeeTradeCount());


        if(Configuration.getInstance().includeTrades()) {
            // Now append the trades for the day.
            ArrayList<ITradeRecord> trades = aTradeDay.getTradeList();
            TreeSet<ITradeRecord> sortedTrades = new TreeSet<>(trades);
            StringBuilder buf = new StringBuilder(rVal);
            sortedTrades.forEach(aRecord -> buf.append("\n").append(aRecord.toString()));

            rVal = buf.toString();
        }
        return rVal;
    }

}