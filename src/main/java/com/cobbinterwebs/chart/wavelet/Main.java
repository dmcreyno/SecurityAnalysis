/*
Copyright 2021 Cobb Interwebs, LLC.

Permission is hereby granted, free of charge, to any person obtaining a copy of this
software and associated documentation files (the "Software"), to deal in the Software
without restriction, including without limitation the rights to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be included in all copies
or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package com.cobbinterwebs.chart.wavelet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.cobbinterwebs.trades.config.Configuration;

import jwave.exceptions.JWaveException;
import jwave.transforms.AncientEgyptianDecomposition;
import jwave.transforms.BasicTransform;
import jwave.transforms.FastWaveletTransform;
import jwave.transforms.wavelets.Wavelet;
import jwave.transforms.wavelets.WaveletBuilder;

/**
 * 
 * @author Cobb Interwebs, LLC
 */
public class Main {
    private static final Logger log = LogManager.getLogger(com.cobbinterwebs.chart.wavelet.Main.class);
	private String baseDir;
	private double[] arrTime;

	public Main() {
		
	}

	public static void main(String[] args) {
		

	}
	
	void run() {
		try {
			baseDir = Configuration.getInstance().getHomeDir();
			
		} catch (Exception ex) {
			log.error("Initialization failed.", ex);
			System.exit(-1);
		}

	}
	
	void doWaveletTransform() throws JWaveException {
		Wavelet wavelet = WaveletBuilder.create("");
		BasicTransform basicTransform = new FastWaveletTransform(wavelet);
		AncientEgyptianDecomposition transform = new AncientEgyptianDecomposition(basicTransform);
		double[] resultArr = transform.forward(arrTime);
	}

}
