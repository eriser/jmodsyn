package org.modsyn;

/**
 * A WaveChangeObservable is an object where a WaveChangeListener can be set to
 * be notified of wave change events.
 * 
 * @author Erik Duijs
 */
public interface WaveChangeObservable {

	/**
	 * Set a WaveChangeListener
	 * 
	 * @param wcl
	 *            The WaveChangeListener
	 */
	void setWaveChangeListener(WaveChangeListener wcl);
}