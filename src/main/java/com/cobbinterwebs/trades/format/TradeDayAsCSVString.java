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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Formats the trade day as a CSV string.
 */
public class TradeDayAsCSVString implements TradeDayPresentation {
    private static final Logger log = LogManager.getLogger("com.cobbinterwebs.trades.TradeDayAsCSVString");

    String delimiter = ",";

    public TradeDayAsCSVString() {

    }

    /**
     * @param aTradeDay
     * @return
     */
    @Override
    public String formatTradeDay(ITradeDay aTradeDay) {
        if(aTradeDay.getTradeList().isEmpty()) {
            return  aTradeDay.getDayOrdinal() + delimiter +
                    aTradeDay.getDateStr() + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0 + delimiter +
                    0;
        }

        StringBuilder recordString;
        String rVal;
            try {
                recordString = new StringBuilder(aTradeDay.getDayOrdinal() + delimiter +
                        aTradeDay.getDateStr() + delimiter +
                        aTradeDay.getAveragePrice() + delimiter +
                        aTradeDay.getVolume() + delimiter +
                        aTradeDay.getBuyVolume() + delimiter +
                        aTradeDay.getSellVolume() + delimiter +
                        aTradeDay.getUnknownVolume() + delimiter +
                        aTradeDay.getDollarVolume() + delimiter +
                        aTradeDay.getBuyDollarVolume() + delimiter +
                        aTradeDay.getSellDollarVolume() + delimiter +
                        aTradeDay.getUnknownDollarVolume() + delimiter +
                        aTradeDay.getPctBuyVol() + delimiter +
                        aTradeDay.getPctSellVol() + delimiter +
                        aTradeDay.getPctUnknownVol() + delimiter +
                        aTradeDay.getPctBuyDolVol() + delimiter +
                        aTradeDay.getPctSellDolVol() + delimiter +
                        aTradeDay.getPctUnknownDolVol() + delimiter +
                        aTradeDay.getTeeTradeCount());
                rVal = recordString.toString();
            } catch (Exception e) {
                log.error("ERROR processing trade day - {}",aTradeDay.getDebugString());
                throw e;
            }
            return rVal;
        }
}
