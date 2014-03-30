package org.modsyn;

/**
 * Create a Context instance.
 * 
 * @author Erik Duijs
 */
public class ContextFactory {

	/**
	 * Create a Context with a given sample-rate.
	 * 
	 * @param sampleRate
	 *            The sample-rate.
	 * @return The created Context.
	 */
	public static Context create(int sampleRate) {
		return new ContextImpl(sampleRate);
	}

	/**
	 * Create a Context with a sample-rate of 44100 Hz.
	 * 
	 * @return The created Context.
	 */
	public static Context create() {
		return new ContextImpl(44100);
	}
}
