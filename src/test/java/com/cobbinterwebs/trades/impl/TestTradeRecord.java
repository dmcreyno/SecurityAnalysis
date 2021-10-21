package com.cobbinterwebs.trades.impl;
////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////


import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cobbinterwebs.locale.DisplayKeys;
import com.cobbinterwebs.trades.TradeRecord;

public class TestTradeRecord extends TradeRecord {
	private static final Logger log = LogManager.getLogger("com.cobbinterwebs.trade.impl.TradeRecord");
	public final static class Indexes {
		static final int TIME_STAMP = 0;
		static final int LAST_PRICE = 1;
		static final int LAST_SIZE = 2;
		static final int BID_PRICE = 3;
		static final int ASK_PRICE = 4;
//		static final int BID_SIZE = 5;
//		static final int ASK_SIZE = 6;
//		static final int BID_XCHANGE = 7;
//		static final int ASK_XCHANGE = 8;
//		static final int LAST_XCHANGE = 9;
		static final int CONDITION = 10;
	}
	
	public TestTradeRecord(String pData) {
		super(pData, ',');
		
	}
	
	public TestTradeRecord(String pTimeString, BigDecimal pPrice, BigDecimal pSize, BigDecimal pBid, BigDecimal pAsk) {
		super(pTimeString, pPrice, pSize, pBid, pAsk);
	}
}
