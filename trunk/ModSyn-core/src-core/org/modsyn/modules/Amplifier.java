/*
 * Created on Apr 22, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.DspObject;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * @author Erik Duijs
 * 
 *         Amplifier of an audio signal.
 */
public final class Amplifier extends DefaultSignalOutput implements SignalInsert, DspObject {

	public final SignalInput control = new SignalInput() {
		@Override
		public void set(float signal) {
			amp = signal;
		}
	};

	private float amp = 1;

	@Override
	public void set(float data) {
		connectedInput.set(data * amp);
	}
}
