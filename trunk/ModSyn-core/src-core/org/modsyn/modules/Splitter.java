/*
 * Created on 3-jul-07
 */
package org.modsyn.modules;

import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;

/**
 * @author DU1381
 * 
 *         Utility for splitting a single input signal into 2 output signals.
 */
public class Splitter implements SignalInput {

	public SignalInput target1 = NullInput.INSTANCE, target2 = NullInput.INSTANCE;

	public final SignalOutput out1 = new SignalOutput() {
		@Override
		public void connectTo(SignalInput input) {
			target1 = input;
			target1.set(signal);
		}
	};
	public final SignalOutput out2 = new SignalOutput() {
		@Override
		public void connectTo(SignalInput input) {
			target2 = input;
			target2.set(signal);
		}
	};

	private float signal;

	@Override
	public void set(float sample) {
		this.signal = sample;
		target1.set(sample);
		target2.set(sample);
	}
}
