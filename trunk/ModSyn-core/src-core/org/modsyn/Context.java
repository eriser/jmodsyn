package org.modsyn;

/**
 * A Context acts as the source of where all DSP processing begins. SignalSource instances are registered here, and will
 * all be updated when this Context is updated.
 * 
 * @author Erik Duijs
 */
public interface Context {

	public static final float SPEED_OF_SOUND_MPS = 343;

	/**
	 * The sample rate that is used.
	 * 
	 * @return The sample rate.
	 */
	int getSampleRate();

	/**
	 * Register a SignalSource instance. Calling this method should normally be the responsibility of SignalSource
	 * classes themselves, and should be done when the SignalSource is completely initialized (typically at the end of
	 * the constructor).
	 * 
	 * @param sg
	 */
	void addSignalSource(SignalSource sg);

	/**
	 * Register a SignalSource instance that acts as a 'master', i.e. something at the end of the chain such as an
	 * object that outputs the signal to the audio hardware. Calling this method should normally be the responsibility
	 * of SignalSource classes themselves (that act as a 'master'), and should be done when the SignalSource is
	 * completely initialized (typically at the end of the constructor).
	 * 
	 * @param master
	 */
	void addMaster(SignalSource master);

	/**
	 * Remove a SignalSource. This can either be a 'normal' SignalSource or a 'master.
	 * 
	 * @param source
	 */
	void remove(SignalSource source);

	/**
	 * Clear all registered DspObjects
	 */
	void clear();

	/**
	 * Update all the registered SignalSource instances for the given amount of seconds.
	 * 
	 * @param seconds
	 */
	void update(float seconds);

	/**
	 * Update all the registered SignalSource instances for 1 sample.
	 */
	void update();
}
