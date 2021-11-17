package com.cobbinterwebs.chart.wavelet;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cobbinterwebs.trades.config.Configuration;

/**
 * 
 * @author Cobb Interwebs, LLC
 *
 */
public abstract class AbstractChartFileReader implements IChartFileReader {
    private static final Logger log = LogManager.getLogger("com.cobbinterwebs.chart.wavelet.AbstractChartFileReader");

    /**
     * The daily file this class represents.
     */
     protected File aFile;

     /**
      * 
      * @param pFile
      * @param pConfig
      */
    public AbstractChartFileReader(File pFile) {
       aFile = pFile;
    }


}
