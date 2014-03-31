package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;

public class Mix extends DefaultSignalOutput implements SignalSource {

	public final SignalInput[] inputs;

	private float buffer;

	public Mix(Context c, int channels) {
		inputs = new SignalInput[channels];
		for (int i = 0; i < channels; i++) {
			inputs[i] = new SignalInput() {
				@Override
				public void set(float signal) {
					buffer += signal;
				}
			};
		}
		c.addSignalSource(this);
	}

	@Override
	public void updateSignal() {
		connectedInput.set(buffer);
		buffer = 0;
	}
}
