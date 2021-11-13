package com.cobbinterwebs.trades.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * Reads the configuration, "trade-data.properties" from the base directory passed in the "init" call.
 *
 */
public class Configuration {
    private static final Logger log = LogManager.getLogger("com.cobbinterwebs.trades.Configuration");

//    private final Locale locale = new Locale("en", "US");
//    //private ResourceBundle displayKeys = ResourceBundle.getBundle("DisplayKeys", locale);

    private Map<String,String> mapOfSymbolsFromCommandLine = new HashMap<>();

    /**
     * 
     * Helpful definitions of the items defined in the properties file.
     *
     */
    public static class PropertyConstants {
        public static final String CHART_RECORD_PROCESSOR_TYPE         = "com.cobbinterwebs.trade.chart.record.class";
        public static final String CHART_PROCESSOR_TYPE         = "com.cobbinterwebs.trade.chart.processor.class";
        public static final String TRADE_DAY_TYPE         = "com.cobbinterwebs.trade.day.class";
        public static final String TRADE_RECORD_TYPE      = "com.cobbinterwebs.trade.record.class";
        /**
         * The directory containing the folders for the various stock symbols to be processed.
         */
        public static final String HOME_KEY               = "com.cobbinterwebs.trades.home";
        public static final String RUNNING_AVERAGE        = "com.cobbinterwebs.running.vwda";
        static final String OUTPUT_HEADER_LINE_01         = "com.cobbinterwebs.trades.output.header1";
        static final String HEADER_SKIP_LINE_COUNT        = "com.cobbinterwebs.trades.skip.header";
        static final String DATE_LINE_NUM                 = "com.cobbinterwebs.trades.date.line.number"; // Date Line number in the Fidelity CSV export
        static final String BUCKET_NAMES                  = "com.cobbinterwebs.trades.bucket.names"; //=0001,0002
        static final String BUCKET_MINS                   = "com.cobbinterwebs.trades.bucket.mins";  //=0.00001,0.00019
        static final String BUCKET_MAXS                   = "com.cobbinterwebs.trades.bucket.maxs";  //=0.00020,0.000299
        static final String BUCKET_LOGIC                  = "com.cobbinterwebs.trades.bucket.logx";  //=INCLUSIVE,INCLUSIVE
        static final String BIG_NUMBER_SCALE              = "com.cobbinterwebs.trades.scale";
        static final String INCLUDE_TRADES                = "com.cobbinterwebs.trades.include";
    }

    /**
     * Marker used to help filter logging statements for appenders
     */
    public static final Marker DEV_MARKER = MarkerManager.getMarker("DEV");
    /**
     * Marker used to help filter logging statements for appenders
     */
    public static final Marker PRINT_MARKER = MarkerManager.getMarker("PRINT");

    /**
     * The file extension for a CSV file (csv)
     */
    public static final String CSV_FILE_EXTENSION = "csv";

    /**
     * The extensions of the files to process from the input dir.
     */
    public static final String[] FILE_EXT_FOR_PROCESSING = {CSV_FILE_EXTENSION};

    private static String baseDir;
    private static String fileSeparator;
    private static org.apache.commons.configuration2.Configuration config;

    private static Configuration _instance;
    
    /**
     * This is a singleton implementation.
     */
    static {
        _instance = new Configuration(System.getProperty(Configuration.PropertyConstants.HOME_KEY));
    }

    /**
     *
     * @param pBaseDir the directory containing the ticker folders.
     * @see com.cobbinterwebs.trades.config.Configuration.PropertyConstants.HOME_KEY
     */
    private Configuration(String pBaseDir) {
        baseDir = pBaseDir;
        fileSeparator = System.getProperty("file.separator");
        log.info("Configuration(String) BASE_DIR: " + baseDir);
        if(null == baseDir) {
            throw new Error("Configuration(String) based directory is null. Did you set the command line property, \"-D" + PropertyConstants.HOME_KEY + "?\"");
        }
        if(log.isDebugEnabled()) {
            log.debug("Configuration(String) FILE SEP: " + fileSeparator);
        }

        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class);

        builder.configure(params.properties().setListDelimiterHandler(new DefaultListDelimiterHandler(','))
                .setFileName(baseDir+fileSeparator+"trade-data.properties"));

        try {
            config = builder.getConfiguration();
        } catch(ConfigurationException cex) {
            log.error("Configuration(String) configuration failed.",cex);
            System.exit(-1);
        }
    }
    
    /**
     * @deprecated Use Configuration.getInstance()
     * @param pathName not used
     * @return instance of Configuration
     */
    public static Configuration init(String pathName) {
        log.info("init(String) pathName - {}",pathName);
        _instance = new Configuration(pathName);
        return _instance;
    }


    /**
     * 
     * @return the single instance of com.cobbinterwebs.trades.config.Configuration.
     */
    public static Configuration getInstance() {
        return _instance;
    }

    /**
     * 
     * @return the home (base) directory as defined by the JVM parameter "com.cobbinterwebs.trades.home"
     */
    public String getHomeDir() {
        return baseDir;
    }

    public int getDateLineNumber() {
        return config.getInt(PropertyConstants.DATE_LINE_NUM, 2);
    }

    /**
     * The input CSV file has some meta data which we will skip. This could vary between CSV providers
     * and that is why the value is configurable.
     * @return returns the value set in the trade-data.properties file; com.cobbinterwebs.trades.skip.header
     */
    public int getHeaderSkipLineCount() {
        return config.getInt(PropertyConstants.HEADER_SKIP_LINE_COUNT, 9);
    }

    public int getMathScale() {
        return config.getInt(PropertyConstants.BIG_NUMBER_SCALE, 8);
    }

    public boolean includeTrades() {
        return config.getBoolean(PropertyConstants.INCLUDE_TRADES, false);
    }

    public boolean includeRunningAverage() {
        return config.getBoolean(PropertyConstants.RUNNING_AVERAGE, false);
    }
    
    /**
     * The header is configured in the properties file.
     * @return the value of the header to use in the output file.
     */
    public String getOutputHeader() {
        // There are two rows to the header with different numbers of columns.
        StringBuilder buffer = new StringBuilder();
        int listSize;
        List<String> columnNames = config.getList(String.class, PropertyConstants.OUTPUT_HEADER_LINE_01);
        listSize = columnNames.size();
        for(int i = 0; i < listSize; i++) {
            buffer.append(columnNames.get(i));
            if(i != listSize-1) {
                buffer.append(",");
            }
        }
        return  buffer.toString();
    }
    
    public String getTradeRecordType() {
        return config.getString(PropertyConstants.TRADE_RECORD_TYPE);
    }
    public String getTradeDayType() {
        return config.getString(PropertyConstants.TRADE_DAY_TYPE);
    }

    /**
     * 
     * @param symbolsArray
     */
    public void initTradeSymbolMap(String[] symbolsArray) {
    	if(null == symbolsArray || symbolsArray.length < 1) {
    		throw new IllegalArgumentException("no trading symbols to process");
    	}
    	
    	for(String symbol : symbolsArray) {
    		this.mapOfSymbolsFromCommandLine.put(symbol, symbol);
    	}
    }
    
    /**
     * 
     * @param symbol
     * @return
     */
    public Boolean symbolWillBeProcessed(String symbol) {
    	return mapOfSymbolsFromCommandLine.containsKey(symbol);
    }

	/**
	 * @return
	 */
	public String getChartRecordProcessorType() {
		String rVal = config.getString(PropertyConstants.CHART_RECORD_PROCESSOR_TYPE);
		if(null == rVal || rVal.length() ==0) {
			throw new IllegalArgumentException(PropertyConstants.CHART_RECORD_PROCESSOR_TYPE + " - is null or empty");
		}
		return rVal;
	}

	/**
	 * @return
	 */
	public String getChartProcessorType() {
		String rVal = config.getString(PropertyConstants.CHART_PROCESSOR_TYPE);
		if(null == rVal || rVal.length() ==0) {
			throw new IllegalArgumentException(PropertyConstants.CHART_PROCESSOR_TYPE + " - is null or empty");
		}
		return rVal;
	}

}
