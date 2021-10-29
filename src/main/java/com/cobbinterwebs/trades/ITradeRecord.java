package com.cobbinterwebs.trades;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////

import com.cobbinterwebs.trades.config.Configuration;

public interface ITradeRecord {
	static final Logger log = LogManager.getLogger("com.cobbinterwebs.trades.ITradeRecord");
	
	enum BuySell {BUY, SELL, UNKNOWN}
	
	BuySell sentiment();
	
	BigDecimal getDollarVolume();
	
	BigDecimal getPrice();
	
	BigDecimal getSize();
	
	Boolean isTeeTrade();
	
	@SuppressWarnings("unchecked")
	static ITradeRecord create(String data) throws Error {
		log.debug("record: {}" , data);
		String className = Configuration.getInstance().getTradeRecordType();
		ITradeRecord rVal = null;
		@SuppressWarnings("rawtypes") Class clazz;
		try {
			log.debug("Loading specific platform trade record impl: {}", className);
			clazz = Class.forName(className);
			try {
				@SuppressWarnings("rawtypes") Constructor ctor = clazz.getConstructor(String.class);
				rVal = (ITradeRecord) ctor.newInstance(data);
			} catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
				log.fatal("BOOM!!! could not find contructor", e);
				System.exit(-1);
			}
		} catch (ClassNotFoundException  e) {
			log.fatal("BOOM!!! Could not Load class {}", className, e);
			System.exit(-1);
		}


		return rVal;
	}
	
}
