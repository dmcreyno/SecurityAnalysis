package com.cobbinterwebs.trades;
import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////
import com.cobbinterwebs.trades.config.Configuration;
import com.cobbinterwebs.trades.impl.TestTradeDay;

public class TradeWindowTests extends AbstractBaseTestCase {
	static final String baseDir = "C:/Users/Trader/IdeaProjects/TradeData/test-data";
	@BeforeClass
	public static void beforeClass() {
		System.setProperty(Configuration.PropertyConstants.HOME_KEY, baseDir);
	}
	
	@Test
	public void testWavelet() {
		File testData = new File(baseDir + "/TEST/input/TEST_19000101.csv");
		ITradeDay iTradeDay = new TestTradeDay(testData);
		iTradeDay.process();
		
		TradeWindow month = new TradeWindow();
		month.add(iTradeDay);
		double[] prices = month.getPriceListForAll();
//		DiscreteWaveletTransform dwt = new DiscreteWaveletTransform(prices, 1);
//		dwt.doFilterTest(64);
	}
}
