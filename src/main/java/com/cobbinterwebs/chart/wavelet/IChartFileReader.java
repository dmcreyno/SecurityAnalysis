package com.cobbinterwebs.chart.wavelet;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.cobbinterwebs.trades.config.Configuration;

/**
 * 
 * @author Cobb Interwebs, LLC
 *
 */
public interface IChartFileReader {
	public void process();
	
	@SuppressWarnings("unchecked")
	static IChartFileReader create(File pFile, Configuration pConfig) throws Error {
		String className = Configuration.getInstance().getChartProcessorType();
		IChartFileReader rVal;
		@SuppressWarnings("rawtypes")
		Class clazz;
		try {
			clazz = Class.forName(className);
			
			try {
			@SuppressWarnings("rawtypes") Constructor ctor = clazz.getConstructor(File.class, Configuration.class);
			rVal = (IChartFileReader) ctor.newInstance(pFile,pConfig);
			} catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
				throw new Error(e);
			}
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}
		
		return rVal;
	}

}
