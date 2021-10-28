package com.cobbinterwebs.trades.utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author Cobb Interwebs, LLC.
 *
 */
public class Infrastructure {
	
	private static final Logger log = LogManager.getLogger("com.cobbinterwebs.trades.utils.Infrastructure");
	
	/**
	 * com.cobbinterwebs.trades.utils.Infrastructure cannot be instantiated.
	 */
	private Infrastructure() {
		log.fatal("Infrastructure cannot be instantiated.");
		throw new IllegalAccessError("com.cobbinterwebs.trades.utils.Infrastructure cannot be instantiated.");
	}
	
	
	/**
	 * 
	 * 
	 * @param commandLine stock symbols
	 * @throws IOException any IO problems.
	 */
	public static void initialize(String basePath, CommandLine commandLine) throws IOException {
		log.debug("initialize(): basePath := {}", basePath);
		String pathStr = basePath;
		List<String> symbolList = commandLine.getArgList();	
		for(String ticker : symbolList) {
			String pathToCheckStr = pathStr + "/" + ticker + "/input";
			log.debug("pathToCheckStr := {}", pathToCheckStr);
			Path pathToCheck = Paths.get(pathToCheckStr);
			if(!Files.exists(pathToCheck)) {
				Files.createDirectories(pathToCheck);
			}
		}
	}
}
