package com.cobbinterwebs.trades.utils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.FileUtils;
///////////////////////////////////////////////////////////////////////////////
//Copyright 2021 Cobb Interwebs, LLC
//Permission is hereby granted, free of charge, to any person obtaining a copy of this
//software and associated documentation files (the "Software"), to deal in the
//Software without restriction, including without limitation the rights to use, copy, 
// modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
// and to permit persons to whom the Software is furnished to do so, subject to the
// following conditions:
//
//The above copyright notice and this permission notice shall be included in all copies
// or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
//OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
//IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
//CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
//TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
//SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Test;

import com.cobbinterwebs.trades.AbstractBaseTestCase;

import static org.junit.Assert.*;

/**
 * 
 * 
 *
 */
public class InfrastructureTest  extends AbstractBaseTestCase {
	private static final Logger log = LogManager.getLogger("com.cobbinterwebs.trades.utils.InfrastructureTest");
	private static final String baseDir = "/tmp/TradeData";
	private static final String[] symbolsArray = {"MSFT","GOLD"};
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception  {
		File deleteMe = new File(baseDir);
		FileUtils.deleteDirectory(deleteMe);
	}

	@Test
	public void testInitialize()  {
		String[] args = symbolsArray;
		com.cobbinterwebs.trades.Main app = new com.cobbinterwebs.trades.Main(args);
		
		try  {
			CommandLine cmd = app.initCommandLine(args);
			Infrastructure.initialize(baseDir,cmd);
		}  catch (IOException e) {
			log.error("test failed",e);
			fail();
		}
	}

	
	static boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
}
