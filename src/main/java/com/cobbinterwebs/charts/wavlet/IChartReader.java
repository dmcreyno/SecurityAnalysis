package com.cobbinterwebs.charts.wavlet;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.cobbinterwebs.trades.config.Configuration;

public interface IChartReader {
	@SuppressWarnings("unchecked")
	static IChartReader create(File pFile, Configuration pConfig) throws Error {
		String className = Configuration.getInstance().getTradeChartType();
		IChartReader rVal;
		@SuppressWarnings("rawtypes")
		Class clazz;
		try {
			clazz = Class.forName(className);
			
			try {
			@SuppressWarnings("rawtypes") Constructor ctor = clazz.getConstructor(File.class, Configuration.class);
			rVal = (IChartReader) ctor.newInstance(pFile,pConfig);
			} catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
				throw new Error(e);
			}
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}
		
		return rVal;
	}

}
