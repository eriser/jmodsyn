/*
 * Created on Apr 25, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.DspObject;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;

/**
 * @author Erik Duijs
 * 
 *         Pan pot. -1 = full left, 0 = center, 1 = full right
 */
public class PanPot implements SignalInput, DspObject {

	public final SignalInput panControl = new SignalInput() {
		@Override
		public void set(float data) {
			setPanning(data);
		}
	};

	private final Amplifier left = new Amplifier();
	private final Amplifier right = new Amplifier();

	public final SignalOutput outputL = left;
	public final SignalOutput outputR = right;

	public PanPot() {
		setPanning(0.5f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioInput#write(float[])
	 */
	@Override
	public void set(float data) {
		left.set(data);
		right.set(data);
	}

	/**
	 * Set panning. 0 = center, 1 = right, -1 = left.
	 * 
	 * @param value
	 */
	public void setPanning(float value) {
		left.control.set(0.50f - value / 2);
		right.control.set(0.50f + value / 2);
	}
}
