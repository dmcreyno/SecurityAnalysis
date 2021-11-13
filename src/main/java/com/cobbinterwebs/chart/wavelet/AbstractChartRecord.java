/**
 * 
 */
package com.cobbinterwebs.chart.wavelet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.text.StringTokenizer;

import com.cobbinterwebs.locale.DisplayKeys;

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
	

    protected List<String> rawTokens = new ArrayList<>();

    public AbstractChartRecord(String pData, char delim) {
        log.debug(DisplayKeys.get(DisplayKeys.LOG_PARSING), pData);
        StringTokenizer strtok = new StringTokenizer(pData,delim);
        while(strtok.hasNext()) {
            rawTokens.add(strtok.next().replaceAll("\"",""));
        }
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
