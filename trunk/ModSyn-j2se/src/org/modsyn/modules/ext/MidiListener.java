package org.modsyn.modules.ext;

public interface MidiListener {

	/**
	 * Key On
	 * 
	 * @param key
	 *            0..127
	 * @param velo
	 *            0..127
	 */
	void keyOn(int key, int velo);

	/**
	 * Key Off
	 * 
	 * @param key
	 *            0..127
	 * @param velo
	 *            0..127
	 */
	void keyOff(int key, int velo);

	/**
	 * Control Change
	 * 
	 * @param control
	 * @param value
	 */
	void controlChange(int control, int value);

	/**
	 * Pitch Bend
	 * 
	 * @param val
	 *            -0x2000 - 0x1fff
	 */
	void pitchBend(int val);
}
