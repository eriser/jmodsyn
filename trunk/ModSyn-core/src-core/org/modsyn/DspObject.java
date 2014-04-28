package org.modsyn;

/**
 * An empty interface that is primarily just meant to mark a class as a 'core' class to perform DSP functions.
 * 
 * @author Erik Duijs
 */
public interface DspObject {
	/**
	 * The minimum allowed normal float value. Used to prevent denormals.
	 */
	static final float MIN_NORMAL = 10e-30f;

	/**
	 * Enable debugging.
	 */
	static final boolean DEBUG = false;
}
