/**
 * 
 */
package com.cobbinterwebs.chart.wavelet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author davidmcreynolds
 *
 */
public abstract class AbstractChartRecord implements IChartRecord {
	
	protected BigDecimal highPrice;
	protected BigDecimal lowPrice;
	protected BigDecimal closePrice;
	protected BigInteger volume;
	protected Date dateTime;


	/**
	 * 
	 * @param csvRecord
	 */
	public AbstractChartRecord(String csvRecord) {
		
	}

	/**
	 * 
	 * @param highPrice
	 * @param lowPrice
	 * @param closePrice
	 * @param volume
	 * @param dateTime
	 */
	public AbstractChartRecord(BigDecimal highPrice, BigDecimal lowPrice, BigDecimal closePrice, BigInteger volume,
			Date dateTime) {
		super();
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.closePrice = closePrice;
		this.volume = volume;
		this.dateTime = dateTime;
	}

	@Override
	public BigDecimal getHighPrice() {
		return highPrice;
	}

	@Override
	public BigDecimal getLowPrice() {
		return lowPrice;
	}

	@Override
	public BigDecimal getClosePrice() {
		return closePrice;
	}

	@Override
	public BigInteger getVolume() {
		return volume;
	}

	@Override
	public Date getDateTime() {
		return dateTime;
	}

	

}
