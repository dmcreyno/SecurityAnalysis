package com.cobbinterwebs.trades;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import com.cobbinterwebs.locale.DisplayKeys;
import com.cobbinterwebs.trades.config.Configuration;
import com.cobbinterwebs.trades.format.TradeDayFormatFactory;
import com.cobbinterwebs.trades.format.TradeDayPresentation;
import com.cobbinterwebs.trades.format.TradeMonthAsTabular;

/**
 * Processes the data for a single stock symbol. Synchronous processing of the CSV
 * trade record files.
 * 
 * Supports multi-threaded application which is the default implementation
 * of the application. The application is able to process more than one set of data files
 * in parallel.
 * @author Cobb Interwebs, LLC
 *
 */
class TickerProcessor extends Thread {
    private static final Logger log = LogManager.getLogger("com.cobbinterwebs.fidelity.trades.TickerProcessor");
	String baseDirName;
	String tickerSymbol;
	int fileCounter = 0;
    private TradeWindow windowTotals;
   
	public TickerProcessor(String pBaseDirName, String pTickerSymbol) {
		this.baseDirName = pBaseDirName;
		this.tickerSymbol = pTickerSymbol;
		this.setName(pTickerSymbol);
	}

	/**
	 * @see java.lang.Thread
	 */
	@Override
	public void run() {
		
		try {
			processDirectory(baseDirName, tickerSymbol);
		} catch (IOException e) {
			log.error("There was a problem processing ticker ().", tickerSymbol, e);
		}
		
	}

    /**
     *
     * Traverses all directories and processes the inputs for each. Each directory is a ticker symbol.
     *
     * @param baseDirName
     * @param tickerSymbol
     * @throws IOException 
     */
    private void processDirectory(String baseDirName, String ticker) throws IOException {
        ThreadContext.put("ticker", tickerSymbol);
        log.info("\t"+DisplayKeys.get(DisplayKeys.PROCESSING_DIR), baseDirName);
        Configuration config = Configuration.getInstance();
        windowTotals = new TradeWindow(config); // Just assumes you've only a months worth. All the data will be in here though.
                                          // If you have 80 days worth of data, it will be in the TradeWindow.
        String OUT_HEADER = config.getOutputHeader();
        String outStr = config.getHomeDir();
        String inDirStr;
        File outfile;
        Collection<File> inputList;
        TreeSet<File> sortedInputList;
        String fileSeparator = System.getProperty("file.separator");

        TradeMonthAsTabular monthFormatter = new TradeMonthAsTabular();

        outfile = new File(outStr + fileSeparator + ticker + fileSeparator + ticker + "." + Configuration.CSV_FILE_EXTENSION);
        log.info(DisplayKeys.get(DisplayKeys.PROCESSING_OUTPUT_FILE), outfile.getAbsolutePath());

        try (   FileWriter outFileWriter = new FileWriter(outfile);
                PrintWriter pw = new PrintWriter(outFileWriter);
                FileWriter summaryFileWriter = new FileWriter(baseDirName + "/summary.txt");
                PrintWriter summaryPrintWriter = new PrintWriter(summaryFileWriter)
             ) {

            pw.println(OUT_HEADER);

            inDirStr = config.getHomeDir() + "/" + ticker + "/input"; // The input dir will be the HOME dir.
            inputList = FileUtils.listFiles(new File(inDirStr), Configuration.FILE_EXT_FOR_PROCESSING,false);
            sortedInputList = new TreeSet<>(Comparator.comparing(File::getName));

            sortedInputList.addAll(inputList);
            this.fileCounter = 0; // a file per day.

            sortedInputList.forEach( aFile -> {
                String currentFileName = aFile.getName();

                if(currentFileName.startsWith(".")) {
                    log.debug(DisplayKeys.get(DisplayKeys.SKIPPING_HIDDEN_FILE),currentFileName);
                }

                if(log.isDebugEnabled()) {
                    log.debug(DisplayKeys.get(DisplayKeys.PROCESSING_FILE),currentFileName);
                }

                
                ITradeDay aDay = ITradeDay.create(aFile, config);
                aDay.process();

                // if NOT zero volume.
                if(!BigDecimal.ZERO.equals(aDay.getVolume())) {
                    this.fileCounter++;
                    aDay.setDayOrdinal(this.fileCounter);
                    windowTotals.add(aDay);
                    TradeDayPresentation formatter = TradeDayFormatFactory.getCsvFormatter();
                    String logMessage = formatter.formatTradeDay(aDay);
                    if(true) {
                        summaryPrintWriter.println(TradeDayFormatFactory.getTabularFormatter().formatTradeDay(aDay));
                        log.debug("{}", logMessage);
                    }
                    if(true) {
                        try {
                            aDay.writeSummary(pw, formatter);
                            pw.flush();
                        } catch (Exception e) {
                            log.error(DisplayKeys.get(DisplayKeys.ERROR), e);
                            System.exit(-1);
                        }
                    }
                }
            });
            summaryPrintWriter.println(monthFormatter.formatTradeMonth(this.windowTotals));
        } finally {
            ThreadContext.pop();
        }
    }
	
}
