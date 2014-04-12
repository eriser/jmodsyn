package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * Add signal. This is the cheapest way to mix signals, because this is not a SignalSource (so not necessarily updated
 * every frame). The SignalInput is the 'carrier', and the 'control' field is used to add a second signal. This means
 * that the second signal will never make it to the SignalOutput unless there is an input.
 * 
 * @author Erik Duijs
 */
public class Adder extends DefaultSignalOutput implements SignalInsert {

	public final SignalInput control = new SignalInput() {
		@Override
		public void set(float signal) {
			add = signal;
		}
	};

	private float add = 0;

	@Override
	public void set(final float data) {
		connectedInput.set(data + add);
	}
}
