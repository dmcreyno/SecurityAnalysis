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
import com.cobbinterwebs.trades.format.TradeDayPresentation;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * 
 * 
 *
 */
public interface ITradeDay {
	void process();
	
	String getDateStr();
	
	ArrayList<ITradeRecord> getTradeList();
	
	BigDecimal getAveragePrice();
	
	BigDecimal getVolume();
	
	BigDecimal getBuyVolume();
	
	int getTeeTradeCount();
	
	BigDecimal getSellVolume();
	
	BigDecimal getUnknownVolume();
	
	BigDecimal getDollarVolume();
	
	BigDecimal getBuyDollarVolume();
	
	BigDecimal getSellDollarVolume();
	
	BigDecimal getUnknownDollarVolume();
	
	boolean isEmpty();
	
	int getDayOrdinal();
	
	void setDayOrdinal(int pDayOrdinal);
	
	BigDecimal getPctBuyVol();
	
	BigDecimal getPctSellVol();
	
	BigDecimal getPctUnknownVol();
	
	BigDecimal getPctBuyDolVol();
	
	BigDecimal getPctSellDolVol();
	
	BigDecimal getPctUnknownDolVol();
	
	void writeSummary(PrintWriter psw, TradeDayPresentation formatter);
	
	String getDebugString();
	
	@SuppressWarnings("unchecked")
	static ITradeDay create(File pFile, Configuration pConfig) throws Error {
		String className = Configuration.getInstance().getTradeDayType();
		ITradeDay rVal;
		@SuppressWarnings("rawtypes") Class clazz;
		try {
			clazz = Class.forName(className);
			
			try {
			@SuppressWarnings("rawtypes") Constructor ctor = clazz.getConstructor(File.class, Configuration.class);
			rVal = (ITradeDay) ctor.newInstance(pFile,pConfig);
			} catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
				throw new Error(e);
			}
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}
		
		return rVal;
	}
	
}
