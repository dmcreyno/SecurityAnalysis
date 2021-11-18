/**
 * 
 */
package com.cobbinterwebs.chart.wavelet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cobbinterwebs.trades.config.Configuration;



/**
 * @author davidmcreynolds
 *
 */
public interface IChartRecord {
	static final Logger log = LogManager.getLogger("om.cobbinterwebs.chart.wavelet.IChartRecord");
	
	public Date getDateTime();

	public BigDecimal getOpenPrice();
	public BigDecimal getHighPrice();
	public BigDecimal getLowPrice();
	public BigDecimal getClosePrice();

	public BigInteger getVolume();
	
	/**
	 * 
	 * @param data a row (record) from the chart file csv.
	 * @return An instance of the class configured in the properties file.
	 * @throws Error
	 */
	@SuppressWarnings("unchecked")
	static IChartRecord create(String data) throws Error {
		log.debug("record: {}" , data);
		String className = Configuration.getInstance().getChartRecordProcessorType();
		IChartRecord rVal = null;
		@SuppressWarnings("rawtypes") Class clazz;
		try {
			log.debug("Loading specific platform chart record impl: {}", className);
			clazz = Class.forName(className);
			try {
				@SuppressWarnings("rawtypes") Constructor ctor = clazz.getConstructor(String.class);
				rVal = (IChartRecord) ctor.newInstance(data);
			} catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
				log.fatal("BOOM!!! could not find contructor", e);
				System.exit(-1);
			}
		} catch (ClassNotFoundException  e) {
			log.fatal("BOOM!!! Could not Load class {}", className, e);
			System.exit(-1);
		}

		return rVal;
	}


}
