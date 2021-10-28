package com.cobbinterwebs.trades;

////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////

import com.cobbinterwebs.trades.config.Configuration;
import com.cobbinterwebs.trades.format.TradeDayFormatFactory;
import com.cobbinterwebs.trades.format.TradeDayPresentation;
import com.cobbinterwebs.locale.DisplayKeys;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Reads the data for one day of trading and stores the stats. Keeps the trades
 * in a list. Also, puts the dollar volume into <i>buckets</i> which are
 * defined in the configuration properties files.
 */
public abstract class TradeDay implements ITradeDay {
    private static final Logger log = LogManager.getLogger("com.cobbinterwebs.fidelity.trades.TradeDay");

    /**
     * The date for which the data has been stored. Format: yyyymmdd.
     */
     protected String dateStr;

    /**
     * The day-of-trading, 1, 2, 3, 4, 5, 6, etc of the collection of daily trade data input files.
     */
     protected int dayOrdinal;

    /**
     *
     */
     protected ArrayList<ITradeRecord> tradeList = new ArrayList<>();

    /**
     * The daily file this class represents.
     */
     protected File aFile;

    /**
     * The properties file used to control aspects of the ticker being analyzed. Multiple tickers are
     * processed and each can be configured to have different properties, rounding, precision, etc. The TradeDay
     * needs this information to control maths.
     */
     protected Configuration config;

    /**
     * The data comes as a CSV of trades for one day.
     */
    public TradeDay(File pFile, Configuration pConfig) {
        config = pConfig;
        aFile = pFile;
    }


    @Override
	public String getDateStr() {
        return dateStr;
    }

    @Override
	public ArrayList<ITradeRecord> getTradeList() {
        return tradeList;
    }


    /**
     *
     * @return the average price for the day
     */
    @Override
	public BigDecimal getAveragePrice() {
        return getDollarVolume().divide(getVolume(), config.getMathScale(), RoundingMode.HALF_UP);
    }

    /**
     *
     * @return the volume for the day
     */
    @Override
	public BigDecimal getVolume() {
        BigDecimal rVal = BigDecimal.ZERO;

        for (ITradeRecord tradeRecord : tradeList) {
            rVal = rVal.add(tradeRecord.getSize());
        }
        return rVal;
    }

    /**
     *
     * @return the buy volume for the day
     */
    @Override
	public BigDecimal getBuyVolume() {
        BigDecimal rVal = BigDecimal.ZERO;

        for (ITradeRecord trade : tradeList) {
            if (TradeRecord.BuySell.BUY == trade.sentiment()) {
                rVal = rVal.add(trade.getSize());
            }
        }
        return rVal;
    }


    /**
     *
     * @return the sell volume for the day
     */
    @Override
	public BigDecimal getSellVolume() {
        BigDecimal rVal = BigDecimal.ZERO;

        for (ITradeRecord trade : tradeList) {
            if (TradeRecord.BuySell.SELL == trade.sentiment()) {
                rVal = rVal.add(trade.getSize());
            }
        }
        return rVal;
    }

    /**
     *
     * @return the unknown volume for the day
     */
    @Override
	public BigDecimal getUnknownVolume() {
        BigDecimal rVal = BigDecimal.ZERO;

        for (ITradeRecord trade : tradeList) {
            if (ITradeRecord.BuySell.UNKNOWN == trade.sentiment()) {
                rVal = rVal.add(trade.getSize());
            }
        }
        return rVal;
    }

    /**
     *
     * @return the dollar volume for the day
     */
    @Override
	public BigDecimal getDollarVolume() {
        BigDecimal rVal = BigDecimal.ZERO;

        for (ITradeRecord tradeRecord : tradeList) {
            rVal = rVal.add(tradeRecord.getDollarVolume());
        }
        return rVal;
    }

    /**
     *
     * @return the dollar buy volume for the day
     */
    @Override
	public BigDecimal getBuyDollarVolume() {
        BigDecimal rVal = BigDecimal.ZERO;

        for (ITradeRecord trade : tradeList) {
            if (ITradeRecord.BuySell.BUY == trade.sentiment()) {
                rVal = rVal.add(trade.getDollarVolume());
            }
        }
        return rVal;
    }

    /**
     *
     * @return the dollar sell volume for the day
     */
    @Override
	public BigDecimal getSellDollarVolume() {
        BigDecimal rVal = BigDecimal.ZERO;

        for (ITradeRecord trade : tradeList) {
            if (TradeRecord.BuySell.SELL == trade.sentiment()) {
                rVal = rVal.add(trade.getDollarVolume());
            }
        }
        return rVal;
    }

    /**
     *
     * @return the dollar unknown volume for the day
     */
    @Override
	public BigDecimal getUnknownDollarVolume() {
        BigDecimal rVal = BigDecimal.ZERO;

        for (ITradeRecord trade : tradeList) {
            if (ITradeRecord.BuySell.UNKNOWN == trade.sentiment()) {
                rVal = rVal.add(trade.getDollarVolume());
            }
        }
        return rVal;
    }

    @Override
	public boolean isEmpty() {
        return this.tradeList.isEmpty();
    }

    @Override
	public int getDayOrdinal() {
        return dayOrdinal;
    }

    @Override
	public void setDayOrdinal(int pDayOrdinal) {
        dayOrdinal = pDayOrdinal;
    }


    @Override
    public String toString() {
        return TradeDayFormatFactory.getTabularFormatter().formatTradeDay(this);
    }

    @Override
	public BigDecimal getPctBuyVol() {
        try {
            return getBuyVolume().divide(getVolume(),5,RoundingMode.HALF_UP);
        } catch(ArithmeticException ae) { }

        return BigDecimal.ZERO;
    }

    @Override
	public BigDecimal getPctSellVol() {
        try {
            return getSellVolume().divide(getVolume(),5,RoundingMode.HALF_UP);
        } catch(ArithmeticException ae) { }

        return BigDecimal.ZERO;
    }

    @Override
	public BigDecimal getPctUnknownVol() {
        try {
            return getUnknownVolume().divide(getVolume(),5,RoundingMode.HALF_UP);
        } catch(ArithmeticException ae) { }

        return BigDecimal.ZERO;
    }

    @Override
	public BigDecimal getPctBuyDolVol() {
        try {
            return getBuyDollarVolume().divide(getDollarVolume(), 5, RoundingMode.HALF_UP);
        } catch(ArithmeticException ae) { }

        return BigDecimal.ZERO;
    }

    @Override
	public BigDecimal getPctSellDolVol() {
        try {
            return getSellDollarVolume().divide(getDollarVolume(),5,RoundingMode.HALF_UP);
        } catch(ArithmeticException ae) { }

        return BigDecimal.ZERO;
    }

    @Override
	public BigDecimal getPctUnknownDolVol() {
        try {
            return getUnknownDollarVolume().divide(getDollarVolume(),5,RoundingMode.HALF_UP);
        } catch(ArithmeticException ae) { }

        return BigDecimal.ZERO;
    }

    /**
     * Looks like this prints the trade records for the day.
     * @param psw
     * @param formatter
     */
    @Override
	public void writeSummary(PrintWriter psw, TradeDayPresentation formatter) {
        psw.println(formatter.formatTradeDay(this));
    }

	@Override
	public int getTeeTradeCount() {
        int rVal = 0;
        for (ITradeRecord trade : tradeList) {
            if (trade.isTeeTrade()) {
                rVal = rVal + 1;
            }
        }
        
        return rVal;
	}

    @Override
	public String getDebugString() {
        String delimiter = "|";
        StringBuilder buf = new StringBuilder(this.getDayOrdinal() + delimiter +
                this.getDateStr() + delimiter +
                this.getVolume() + delimiter +
                this.getBuyVolume() + delimiter +
                this.getSellVolume() + delimiter +
                this.getUnknownVolume() + delimiter +
                this.getDollarVolume() + delimiter +
                this.getBuyDollarVolume() + delimiter +
                this.getSellDollarVolume() + delimiter +
                this.getUnknownDollarVolume());

        buf.append("TradeDay[Date=").append(getDateStr()).append(", ");
        buf.append("Volume=").append(getVolume()).append(", ");
        buf.append("BuyVolume=").append(getBuyVolume()).append(", ");
        buf.append("SellVolume=").append(getSellVolume()).append(", ");
        buf.append("UnknownVolume=").append(getUnknownVolume()).append(", ");
        buf.append("DollarVolume=").append(getDollarVolume()).append(", ");
        buf.append("BuyDollarVolume=").append(getBuyDollarVolume()).append(", ");
        buf.append("SellDollarVolume=").append(getSellDollarVolume()).append(", ");
        buf.append("UnknownDollarVolume=").append(getUnknownDollarVolume()).append("]");

        return buf.toString();
    }

}
