package com.cobbinterwebs.trades.analytics.wavelet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////

import org.junit.Before;
import org.junit.Test;

import com.cobbinterwebs.test.utils.StringUtils;
import com.cobbinterwebs.trades.AbstractBaseTestCase;
import com.cobbinterwebs.trades.utils.Math;

public class DiscreteWaveletTransformTest extends AbstractBaseTestCase {
	@Before
	public void setUp() throws Exception {
	}
	
	
	@Test
	public void runForwardTest() {
		double[] a = {5,2,3,3,6,6,5,3};
		try {
			jwave.transforms.wavelets.daubechies.Daubechies6 dwt = new jwave.transforms.wavelets.daubechies.Daubechies6();
			double[] rVal = dwt.forward(a, a.length);
			for (double d : rVal) {
				System.out.print(d + ",");
			}
		} catch (IllegalArgumentException ex) {
			
		}
	}
	
	@Test
	public void highestPowerOfTwo() {
		int val = 150;
		int expected = 128;
		int calcVal = Math.highestPowerof2(val);
		assertEquals("", calcVal, expected);
	}
	
}
