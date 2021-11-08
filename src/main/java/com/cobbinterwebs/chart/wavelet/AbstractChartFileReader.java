package com.cobbinterwebs.chart.wavelet;

import java.awt.Rectangle;
import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.svg.SVGGraphics2D;

import com.cobbinterwebs.trades.config.Configuration;

public class AbstractChartFileReader implements IChartFileReader {
    private static final Logger log = LogManager.getLogger("com.cobbinterwebs.chart.wavelet.AbstractChartFileReader");

    /**
     * The daily file this class represents.
     */
     protected File aFile;

    /**
     * The properties file used to control aspects of the ticker being analyzed. Multiple tickers are
     * processed and each can be configured to have different properties, rounding, precision, etc. The TradeDay
     * needs this information to control maths.
     */
     protected Configuration config;

    public AbstractChartFileReader(File pFile, Configuration pConfig) {
        config = pConfig;
        aFile = pFile;
    }

}
