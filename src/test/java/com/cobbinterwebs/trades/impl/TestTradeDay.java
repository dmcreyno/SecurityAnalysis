package com.cobbinterwebs.trades.impl;
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cobbinterwebs.locale.DisplayKeys;
import com.cobbinterwebs.trades.ITradeRecord;
import com.cobbinterwebs.trades.format.TradeDayFormatFactory;
import com.cobbinterwebs.trades.format.TradeDayPresentation;


@SuppressWarnings("unused")
public class TestTradeDay implements com.cobbinterwebs.trades.ITradeDay {
	private static final Logger log = LogManager.getLogger("com.cobbinterwebs.trades.impl.TestTradeDay");

	/**
	 * The day-of-trading, 1, 2, 3, 4, 5, 6, etc., of the collection of daily trade data input files.
	 */
	private int dayOrdinal;
	
	/**
	 *
	 */
	private final ArrayList<ITradeRecord> tradeList = new ArrayList<>();
	
	/**
	 * The daily file this class represents.
	 */
	private final File aFile;
	
	
	/**
	 * The data comes as a CSV of trades for one day.
	 */
	public TestTradeDay(File pFile) {
		aFile = pFile;
	}
	
	/**
	 * Reads the File for the day. Puts the trade dollar-volume in the
	 * appropriate bucket.
	 */
	@Override
	public void process() {
	}
	
	
	@Override
	public String getDateStr() {
		return "12/12/2012";
	}
	
	/**
	 *
	 * @return the average price for the day
	 */
	@Override
	public BigDecimal getAveragePrice() {
		return getDollarVolume().divide(getVolume(), 4, RoundingMode.HALF_UP);
	}
	
	/**
	 *
	 * @return the volume for the day
	 */
	@Override
	public BigDecimal getVolume() {
		BigDecimal rVal = BigDecimal.ZERO;
		
		for (ITradeRecord ITradeRecord : tradeList) {
			rVal = rVal.add(ITradeRecord.getSize());
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
			if (ITradeRecord.BuySell.BUY == trade.sentiment()) {
				rVal = rVal.add(trade.getSize());
			}
		}
		return rVal;
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
	
	/**
	 *
	 * @return the sell volume for the day
	 */
	@Override
	public BigDecimal getSellVolume() {
		BigDecimal rVal = BigDecimal.ZERO;
		
		for (ITradeRecord trade : tradeList) {
			if (ITradeRecord.BuySell.SELL == trade.sentiment()) {
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
		
		for (ITradeRecord ITradeRecord : tradeList) {
			rVal = rVal.add(ITradeRecord.getDollarVolume());
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
			if (ITradeRecord.BuySell.SELL == trade.sentiment()) {
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
		} catch(ArithmeticException ignored) { }
		
		return BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal getPctSellVol() {
		try {
			return getSellVolume().divide(getVolume(),5,RoundingMode.HALF_UP);
		} catch(ArithmeticException ignored) { }
		
		return BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal getPctUnknownVol() {
		try {
			return getUnknownVolume().divide(getVolume(),5,RoundingMode.HALF_UP);
		} catch(ArithmeticException ignored) { }
		
		return BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal getPctBuyDolVol() {
		try {
			return getBuyDollarVolume().divide(getDollarVolume(), 5, RoundingMode.HALF_UP);
		} catch(ArithmeticException ignored) { }
		
		return BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal getPctSellDolVol() {
		try {
			return getSellDollarVolume().divide(getDollarVolume(),5,RoundingMode.HALF_UP);
		} catch(ArithmeticException ignored) { }
		
		return BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal getPctUnknownDolVol() {
		try {
			return getUnknownDollarVolume().divide(getDollarVolume(),5,RoundingMode.HALF_UP);
		} catch(ArithmeticException ignored) { }
		
		return BigDecimal.ZERO;
	}
	
	/**
	 * Looks like this prints the trade records for the day.
	 * @param psw the print writer
	 * @param formatter specific formatter
	 */
	@Override
	public void writeSummary(PrintWriter psw, TradeDayPresentation formatter) {
		psw.println(formatter.formatTradeDay(this));
	}
	
	/**
	 * Wrapper around a buffered reader. While there is not much value in wrapping that class
	 * this class will skip the summary header info Fidelity puts in their exports.
	 */
	public static class CSVInputReader {
		private final Logger log = LogManager.getLogger("com.cobbinterwebs.trades.impl.CSVInputReader");
		//private final int LINE_NO_DATE = config.getDateLineNumber();
		private BufferedReader reader;
		//private String dateStr;
		private final File file;
		
		/**
		 * CTOR accepting an instance of a File .
		 * @param pFile the file to read from.
		 */
		public CSVInputReader(File pFile) {
			file=pFile;
		}
		
		void initFile() throws IOException {
			log.info("Reading file {}", file.getName());
			reader = new BufferedReader(new FileReader(file));
		}
		
		String readLine() throws IOException {
			return reader.readLine();
		}
		
		void close() {
			try {
				reader.close();
			} catch (Exception e) {
				log.error(DisplayKeys.get(DisplayKeys.ERROR_FILE_CLOSE),file.getAbsolutePath(), e);
			}
		}
	}
	
	public String getDebugString() {
		String delimiter = "|";
		
		return this.getDayOrdinal() + delimiter +
				this.getDateStr() + delimiter +
				this.getVolume() + delimiter +
				this.getBuyVolume() + delimiter +
				this.getSellVolume() + delimiter +
				this.getUnknownVolume() + delimiter +
				this.getDollarVolume() + delimiter +
				this.getBuyDollarVolume() + delimiter +
				this.getSellDollarVolume() + delimiter +
				this.getUnknownDollarVolume() + "FidelityTradeDay[Date=" + getDateStr() + ", " +
				"Volume=" + getVolume() + ", " +
				"BuyVolume=" + getBuyVolume() + ", " +
				"SellVolume=" + getSellVolume() + ", " +
				"UnknownVolume=" + getUnknownVolume() + ", " +
				"DollarVolume=" + getDollarVolume() + ", " +
				"BuyDollarVolume=" + getBuyDollarVolume() + ", " +
				"SellDollarVolume=" + getSellDollarVolume() + ", " +
				"UnknownDollarVolume=" + getUnknownDollarVolume() + ", " +
				"TeeTrade=" + getTeeTradeCount() + "]";
	}

	@Override
	public void addTradeRecord(ITradeRecord pTradeRecord) {
		// TODO Auto-generated method stub
		
	}
}
