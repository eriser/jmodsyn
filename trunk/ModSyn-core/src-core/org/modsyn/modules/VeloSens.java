package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInputValue;

/**
 * Designed to react to velocity signals of MIDI key-on events.
 * 
 * @author Erik Duijs
 */
public class VeloSens extends DefaultSignalOutput {

	public final SignalInput velo = new SignalInput() {
		@Override
		public void set(float signal) {
			if (signal != 0) { // ignore key-off events
				connectedInput.set((float) (Math.pow(signal, pow.value) * (sens.value + bias.value)));
			}
		}
	};

	public final SignalInputValue sens = new SignalInputValue(1);
	public final SignalInputValue pow = new SignalInputValue(1);
	public final SignalInputValue bias = new SignalInputValue();
}
