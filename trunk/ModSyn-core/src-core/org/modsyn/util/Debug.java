package org.modsyn.util;

import org.modsyn.DspObject;

public class Debug implements DspObject {

	/**
	 * Check for problematic signal values, such as NaN, Infinity and denormals. If there is a problem, it will print a
	 * stacktrace.
	 * 
	 * @param signal
	 */
	public static void checkSignal(float signal) {
		if (signal != 0 && Math.abs(signal) < MIN_NORMAL) {
			new Throwable("Denormal: " + signal).printStackTrace();
		}

		if (Float.isNaN(signal) || Float.isInfinite(signal)) {
			new Throwable("Illegal signal value: " + signal).printStackTrace();
		}
	}

}
