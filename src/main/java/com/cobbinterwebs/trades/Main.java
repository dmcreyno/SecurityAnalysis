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
import com.cobbinterwebs.trades.format.TradeMonthAsTabular;
import com.cobbinterwebs.trades.utils.Infrastructure;
import com.cobbinterwebs.locale.DisplayKeys;
import com.cobbinterwebs.trades.ITradeDay;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * Program to read a collection of Fidelity trades exported from ActiveTraderPro as CSV files.
 * Uses a "base" directory structure to hold the input files. The directory referenced
 * by -Dom.ga.fidelity.trades.home is assumed to have a collection of ticker directories each of
 * which has an input folder named, "input" containing CSV files named with the
 * date of the day the trades were executed.
 *
 * Reads the "fidelity.properties file referenced on the command line
 * using the -D option where the base directory is defined. It is assumed the directory will have
 * a sub-folder named <i>input</i> when the CSV files downloaded from Fidelity will be found.
 *
 * The code will travers all subdirectories and pick up any file with a "csv" extension inside an "input" directory.
 * <b>Example</b><br>
 * -Dcom.ga.fidelity.trades.home=/users/mary/trade_data
 *
 * The code will process only the passed subdirectories and pick up any file with a "csv" extension inside an "input"
 * directory.
 * <b>Example</b><br>
 * -Dcom.ga.fidelity.trades.home=/users/mary/trade_data
 *
 * Examples:
 * Initialize a set of folders for three trading symbols.
 * java -Dcom.ga.fidelity.trades.home=/research/tickers Main -i MSFT ORCL IBM
 *
 * Process the data for all the trading symbols under the home directory.
 * java -Dcom.ga.fidelity.trades.home=/research/tickers Main
 *
 * Process the data under the home directory for the symbol MSFT.
 * java -Dcom.ga.fidelity.trades.home=/research/tickers Main MSFT
 *
 * TODO Alternative
 * Using your operating system specific features set an environment
 * variable named "com.ga.fidelity.trades.home"
 */
public class Main {
    private static final Logger log = LogManager.getLogger("com.cobbinterwebs.fidelity.trades.Main");
    private static String baseDir = "";
    private TradeWindow monthly;
    private Options options = new Options();
    CommandLineParser parser;
    CommandLine cmd;


    /**
     * Constructor
     * @param args
     */
    @SuppressWarnings({"unused"})
    public Main(String[] args) {
    	initCommandLine(args);
    }
    
    public CommandLine initCommandLine(String[] args) {
		parser = new DefaultParser();
    	
        if(log.isInfoEnabled()) {
            for (String arg: args) {
                log.info("Main(String[]) - arg: {}", arg);
            }
        }
		//***Parsing Stage***
		//parse the options passed as command line arguments
		try {
			cmd = parser.parse( options, args);
		} catch (ParseException e) {
			log.error("could not start", e);
			System.exit(-1);
		}

		try {
			baseDir = Configuration.getInstance().getHomeDir();
	    	Infrastructure.initialize(baseDir, cmd);
	    	Configuration.getInstance().initTradeSymbolMap(args);
		} catch (Exception ex) {
			log.error("Initialization failed.", ex);
			System.exit(-1);
		}
		
		return cmd;

	}

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        log.info(DisplayKeys.get(DisplayKeys.STARTUP));
        Main app = new Main(args);
        
        // The home directory has folders for each ticker symbol to be analyzed.
        baseDir = System.getProperty(Configuration.PropertyConstants.HOME_KEY);
        if(baseDir == null || baseDir.length() == 0) {
            log.fatal(DisplayKeys.get(DisplayKeys.STARTUP_BASEDIR_MISSING), Configuration.PropertyConstants.HOME_KEY);
            System.exit(-1);
        }
        app.process();
    }
    
    /**
     *
     */
    private void process() {
    	ArrayList<Thread> threadList = new ArrayList<>();
        File dir = FileUtils.getFile(baseDir);
        File[] files = dir.listFiles();
        if(null == files) {
            log.error("No directories to process.");
        } else {
        	
        	// Refactor to expect list of tickers on command line.
        	// Initialize the directories if missing.
        	Stream<File> theDirStream = Arrays.stream(files).filter(File::isDirectory);
        	theDirStream.forEach(file -> {
                String baseDirName = file.getAbsolutePath();
                String tickerSymbol = file.getName();
                if(Configuration.getInstance().symbolWillBeProcessed(tickerSymbol)) {
                    Runnable ticketProcessor = new TickerProcessor(baseDirName, tickerSymbol);
                    // TODO move to a configurable thread pool.
                    Thread t = new Thread(ticketProcessor);
                    threadList.add(t);
                    t.start();
                }
            });
        	threadList.forEach(aThread -> {
            	try {
					aThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            });
        }
    }


	List<String> getTickerList() {
		cmd.getArgList().forEach(new Consumer<String>() {
			@Override
			public void accept(String t) {
				log.info("found ticker, {}.",t);
			}
		});
		return cmd.getArgList();
	}
	
}
