package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

public class AllPass1stOrderFilter extends DefaultSignalOutput implements SignalInsert {

	private float amp, buffer;

	public final SignalInput ctrl = new SignalInput() {
		@Override
		public void set(float signal) {
			amp = (1.f - signal) / (1.f + signal);
		}
	};

	@Override
	public void set(float signal) {
		float out = signal * -amp + buffer;
		buffer = out * amp + signal;
		connectedInput.set(out);
	}

}