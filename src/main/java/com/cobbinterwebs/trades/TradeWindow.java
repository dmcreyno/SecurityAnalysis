package com.cobbinterwebs.trades;

////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cobbinterwebs.trades.config.Configuration;
import com.cobbinterwebs.trades.format.TradeMonthAsTabular;

/**
 * Holds the days of trading. There really is no concept of a month in the application. It processes
 * all the CSV files found in the directory. It is assumed the user will be analyzing a month at a time.
 * However, there is no limit. The user may store more than one month of data in the directory and the
 * system will process it.
 */
public class TradeWindow {
	private static final Logger log = LogManager.getLogger("com.cobbinterwebs.trades.TradeWindow");
	
    private BigDecimal volume = BigDecimal.ZERO;
    private BigDecimal totalDollars = BigDecimal.ZERO;
    private BigDecimal buyVolume = BigDecimal.ZERO;
    private BigDecimal sellVolume = BigDecimal.ZERO;
    private BigDecimal unknownVolume = BigDecimal.ZERO;
    private BigDecimal totalBuyDollars = BigDecimal.ZERO;
    private BigDecimal totalSellDollars = BigDecimal.ZERO;
    private BigDecimal totalUnknownDollars = BigDecimal.ZERO;
    
    private final ArrayList<ITradeDay> tradeDayList = new ArrayList<>();
    
    public int size() {
		return tradeDayList.size();
	}


	public boolean isEmpty() {
		return tradeDayList.isEmpty();
	}


	public boolean add(ITradeDay pADay) {
        addTotalVolume(pADay.getVolume());
        addTotalBuyVolume(pADay.getBuyVolume());
        addTotalSellVolume(pADay.getSellVolume());
        addTotalUnknownVolume(pADay.getUnknownVolume());

        addTotalDollars(pADay.getDollarVolume());
        addTotalBuyDollars(pADay.getBuyDollarVolume());
        addTotalSellDollars(pADay.getSellDollarVolume());
        addTotalUnknownDollars(pADay.getUnknownDollarVolume());
        addToTeeTradeCount(pADay.getTeeTradeCount());
		return tradeDayList.add(pADay);
	}


	public void clear() {
		tradeDayList.clear();
	}

	private int tTradeCount;

    private Configuration config;
    
    /**
     *
     * @param pConfig TODO use the Configuration singleton instead of passing around a reference.
     */
    public TradeWindow(Configuration pConfig) {
        config = pConfig;
    }
    
    /**
     * This ctor is for testing only until I can refactor.
     */
    public TradeWindow() {
        config = Configuration.getInstance();
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void addTotalVolume(BigDecimal pArg) {
        volume = volume.add(pArg);
    }

    public BigDecimal getDollarVolume() {
        return totalDollars;
    }

    public void addTotalDollars(BigDecimal pArg) {
        totalDollars = totalDollars.add(pArg);
    }

    public BigDecimal getBuyVolume() {
        return buyVolume;
    }

    public void addTotalBuyVolume(BigDecimal pArg) {
        buyVolume = buyVolume.add(pArg);
    }

    public BigDecimal getSellVolume() {
        return sellVolume;
    }

    public void addTotalSellVolume(BigDecimal pArg) {
        sellVolume = sellVolume.add(pArg);
    }

    public BigDecimal getUnknownVolume() {
        return unknownVolume;
    }

    public void addTotalUnknownVolume(BigDecimal pArg) {
        unknownVolume = unknownVolume.add(pArg);
    }

    public BigDecimal getBuyDollarVolume() {
        return totalBuyDollars;
    }

    public void addTotalBuyDollars(BigDecimal pArg) {
        totalBuyDollars = totalBuyDollars.add(pArg);
    }

    public BigDecimal getSellDollarVolume() {
        return totalSellDollars;
    }

    public void addTotalSellDollars(BigDecimal pArg) {
        totalSellDollars = totalSellDollars.add(pArg);
    }

    public BigDecimal getUnknownDollarVolume() {
        return totalUnknownDollars;
    }

    public void addTotalUnknownDollars(BigDecimal pArg) {
        totalUnknownDollars = totalUnknownDollars.add(pArg);
    }

    /**
     * This is not going to be written to the CSV file, only the summary text. So, there is
     * no interface and no override.
     * @param psw
     * @param formatter
     */
    public void writeSummary(PrintWriter psw, TradeMonthAsTabular formatter) {
        psw.println(formatter.formatTradeMonth(this));
    }

    /**
     *
     * @return the average price for the month
     */
    public BigDecimal getAveragePrice() {
    	BigDecimal rVal = BigDecimal.ZERO;
    	try {
    		rVal = getDollarVolume().divide(getVolume(), config.getMathScale(), RoundingMode.HALF_UP);
    	} catch (ArithmeticException ex) {
    		log.debug("DollarVolume: {} / Volume: {}", getDollarVolume(), getVolume());
    	}
        return rVal;
    }
    
    public void addToTeeTradeCount(int tTrades) {
        tTradeCount = tTradeCount + tTrades;
    }
    
	public int getTeeTradeCount() {
        return tTradeCount;
	}


	public BigDecimal getPctBuyVol() {
        try {
            return buyVolume.divide(volume,5,RoundingMode.HALF_UP);
        } catch(ArithmeticException ae) { 
        	if(log.isErrorEnabled()) {
        		log.error("volume:{}",volume, ae);
        	}
        }

        return BigDecimal.ZERO;
    }

	public BigDecimal getPctSellVol() {
        try {
            return sellVolume.divide(volume,5,RoundingMode.HALF_UP);
        } catch(ArithmeticException ae) { 
        	if(log.isErrorEnabled()) {
        		log.error("volume:{}",volume, ae);
        	}
        }

        return BigDecimal.ZERO;
    }

	public BigDecimal getPctUnknownVol() {
        try {
            return unknownVolume.divide(volume,5,RoundingMode.HALF_UP);
        } catch(ArithmeticException ae) { 
        	if(log.isErrorEnabled()) {
        		log.error("volume:{}",volume, ae);
        	}
        }

        return BigDecimal.ZERO;
    }
    
}
