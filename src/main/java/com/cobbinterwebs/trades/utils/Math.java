package com.cobbinterwebs.trades.utils;

/**
 * 
 * Functions that have no home.
 * 
 * Helpers.
 *
 */
public class Math {

	/**
	 * 
	 * @param n the number to check
	 * @return the highest power of 2 that is less than or equal to n.
	 */
	public static int highestPowerof2(int n) {
	    int res = 0;
	    for(int i = n; i >= 1; i--) {
	         
	        // If i is a power of 2
	        if (isPowerOfTwo(i)) {
	            res = i;
	            break;
	        }
	    }
	    return res;
	}

	/**
	 * 
	 * @param i
	 * @return true if i is a power of 2. Otherwise, false.
	 */
	public static boolean isPowerOfTwo(int i) {
		return (i & (i-1)) == 0;
	}
}
