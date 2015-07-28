package org.modsyn;

/**
 * Listener for wave change events.
 * 
 * @author Erik Duijs
 */
public interface WaveChangeListener {

	/**
	 * Notify us of a wave change event.
	 * 
	 * @param wave
	 *            The new wave form
	 */
	void waveChanged(float[] wave);
}
