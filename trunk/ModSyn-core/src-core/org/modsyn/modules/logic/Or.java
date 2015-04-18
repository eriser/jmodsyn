package org.modsyn.modules.logic;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;

/**
 * 
 * @author Erik Duijs
 */
public class Or extends DefaultSignalOutput {

	public final SignalInput in1 = new SignalInput() {
		@Override
		public void set(float signal) {
			connectedInput.set(signal);
		}
	};
	public final SignalInput in2 = new SignalInput() {
		@Override
		public void set(float signal) {
			connectedInput.set(signal);
		}
	};
}
