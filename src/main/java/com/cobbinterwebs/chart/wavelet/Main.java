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

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import com.cobbinterwebs.locale.DisplayKeys;

import com.cobbinterwebs.trades.config.Configuration;

import jwave.exceptions.JWaveException;
import jwave.transforms.AncientEgyptianDecomposition;
import jwave.transforms.BasicTransform;
import jwave.transforms.FastWaveletTransform;
import jwave.transforms.wavelets.Wavelet;
import jwave.transforms.wavelets.daubechies.Daubechies5;

/**
 * 
 * @author Cobb Interwebs, LLC
 */
public class Main {
    private static final Logger log = LogManager.getLogger(com.cobbinterwebs.chart.wavelet.Main.class);
	private static String baseDir = "";
	private double[] arrTime;

	public Main(String[] args) {
		
	}

	public static void main(String[] args) {
        log.info(DisplayKeys.get(DisplayKeys.STARTUP));
		Configuration.getInstance().initTradeSymbolMap(args);
		         Main app = new Main(args);
        
        // The home directory has folders for each ticker symbol to be analyzed.
        baseDir = Configuration.getInstance().getHomeDir();
        if(baseDir == null || baseDir.length() == 0) {
            log.fatal(DisplayKeys.get(DisplayKeys.STARTUP_BASEDIR_MISSING), Configuration.PropertyConstants.HOME_KEY);
            System.exit(-1);
        }
        app.run();
        log.info(". . . finished.");


	}
	
	@SuppressWarnings("unused")
	void run() {
		Configuration config = Configuration.getInstance();
        File dir = FileUtils.getFile(baseDir);
        File[] files = dir.listFiles();
        File outfile;
        String fileSeparator = System.getProperty("file.separator");
        
        if(null == files) {
            log.error("No directories to process.");
        } else {
    		try {
           	Stream<File> theDirStream = Arrays.stream(files).filter(File::isDirectory);
            	theDirStream.forEach(file -> {
                    String baseDirName = file.getAbsolutePath();
                    String tickerSymbol = file.getName();
                    log.info("wavlet processing ticker, {}, in {}.", tickerSymbol, baseDirName);
//                    if(true) { // TODO revisit selective processing.
                    if(Configuration.getInstance().symbolWillBeProcessed(tickerSymbol)) {
                    	String inDirStr = config.getHomeDir() + "/" + tickerSymbol + "/chartInput"; // The input dir will be the HOME dir.
                    	Collection<File> inputList = FileUtils.listFiles(new File(inDirStr), Configuration.FILE_EXT_FOR_PROCESSING,false);
                    	TreeSet<File> sortedInputList = new TreeSet<>(Comparator.comparing(File::getName));
                    	sortedInputList.addAll(inputList);
                    	sortedInputList.forEach( aFile -> {
                            String currentFileName = aFile.getName();

                            if(currentFileName.startsWith(".")) {
                                log.debug(DisplayKeys.get(DisplayKeys.SKIPPING_HIDDEN_FILE),currentFileName);
                            }

                            if(log.isInfoEnabled()) {
                                log.info(DisplayKeys.get(DisplayKeys.PROCESSING_FILE),currentFileName);
                            }
                            
                            IChartFileReader chartFileReader = IChartFileReader.create(aFile);
                            double[] coeffs = chartFileReader.process();
                            XYDataset dataset = new DefaultXYDataset();
                            XYPlot xyplot = new XYPlot();
//    public XYPlot(XYDataset dataset, ValueAxis domainAxis, ValueAxis rangeAxis,
//                            XYItemRenderer renderer) {
                           

                    	});
                    } else {
                    	log.info("skipping {}", tickerSymbol);
                    }
                });
    		} catch (Exception ex) {
    			log.error("run failed.", ex);
    			System.exit(-1);
    		}

        }
	}
	

	
    public void initCommandLine(String[] args) {
        if(log.isDebugEnabled()) {
        	StringBuilder buf = new StringBuilder();
            for (String arg: args) {
            	buf.append(arg);
            }
            log.info("Main(String[]) - arg: {}", buf.toString());
        }
		try {
	    	Configuration.getInstance().initTradeSymbolMap(args);
		} catch (Exception ex) {
			log.error("Initialization failed.", ex);
			System.exit(-1);
		}
		
	}

}
