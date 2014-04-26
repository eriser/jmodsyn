/*
 * Created on 26-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * Cubic distortion
 * 
 * @author Erik Duijs
 */
public class Cubic extends DefaultSignalOutput implements SignalInsert {

	private float clean, dist;

	public final SignalInput ctrl = new SignalInput() {

		@Override
		public void set(float signal) {
			clean = 1 - signal;
			dist = signal;

		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SignalInput#write(float)
	 */
	@Override
	public void set(float in) {
		connectedInput.set((in * in * in) * dist + (in * clean));
	}
}
