/*
 * Created on 3-jul-07
 */
package org.modsyn.modules;

import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;

/**
 * @author Erik Duijs
 * 
 *         Utility for splitting a single input signal into multiple output
 *         signals.
 */
public class MultiSplitter implements SignalInput {

	public final SignalOutput[] outputs;
	private final SignalInput[] targets;
	private final int channels;

	public MultiSplitter(int channels) {
		this.channels = channels;
		targets = new SignalInput[channels];
		outputs = new SignalOutput[channels];

		for (int i = 0; i < channels; i++) {
			final int channel = i;
			targets[i] = NullInput.INSTANCE;
			outputs[i] = new SignalOutput() {
				@Override
				public void connectTo(SignalInput input) {
					targets[channel] = input;
					targets[channel].set(signal);
				}
			};
		}
	}

	private float signal;

	@Override
	public void set(float sample) {
		this.signal = sample;
		for (int i = 0; i < channels; i++) {
			targets[i].set(sample);
		}
	}
}
