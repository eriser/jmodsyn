package org.modsyn.modules.logic;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInputValue;

/**
 * 
 * @author Erik Duijs
 */
public class SwitchValue extends DefaultSignalOutput {

	public final SignalInput trigger1 = new SignalInput() {
		@Override
		public void set(float signal) {
			if (signal != 0) {
				connectedInput.set(value1.value);
			}
		}
	};
	public final SignalInput trigger2 = new SignalInput() {
		@Override
		public void set(float signal) {
			if (signal != 0) {
				connectedInput.set(value2.value);
			}
		}
	};
	public final SignalInputValue value1 = new SignalInputValue(0);
	public final SignalInputValue value2 = new SignalInputValue(0);
}
