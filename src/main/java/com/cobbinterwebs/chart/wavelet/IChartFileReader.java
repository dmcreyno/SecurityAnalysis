package com.cobbinterwebs.chart.wavelet;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cobbinterwebs.trades.config.Configuration;

/**
 * 
 * @author Cobb Interwebs, LLC
 *
 */
public interface IChartFileReader {
	static final Logger log = LogManager.getLogger("om.cobbinterwebs.chart.wavelet.IChartFileReader");
	public double[] process();
	
	/**
	 * 
	 * @param the file to be read.
	 * @return An instance of the class configured in the properties file.
	 * @throws Error
	 */
	@SuppressWarnings("unchecked")
	static IChartFileReader create(File pFile) throws Error {
		String className = Configuration.getInstance().getChartProcessorType();
		IChartFileReader rVal = null;
		@SuppressWarnings("rawtypes")
		Class clazz;
		try {
			log.debug("Loading specific platform chart file impl: {}", className);
			clazz = Class.forName(className);			
			try {
				@SuppressWarnings("rawtypes") Constructor ctor = clazz.getConstructor(File.class);
				rVal = (IChartFileReader) ctor.newInstance(pFile);
			} catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
				log.fatal("BOOM!!! could not find contructor", e);
				System.exit(-1);
			}
		} catch (ClassNotFoundException e) {
			log.fatal("BOOM!!! Could not Load class {}", className, e);
			System.exit(-1);
		}
		
		return rVal;
	}

	/**
	 * @return
	 */
	double[] getOpenPriceArray();

}
