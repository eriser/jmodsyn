package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

public class Compressor extends DefaultSignalOutput implements SignalInsert {
	/**
	 * Offset to prevent de-normalisation.
	 */
	private static final float DC_OFFSET = 10e-30f;

	public final SignalInput ctrlThreshold = new SignalInput() {
		@Override
		public void set(float signal) {
			threshold = signal;
			transferB = (float) (output * Math.pow(threshold, -transferA));
		}
	};

	public final SignalInput ctrlRatio = new SignalInput() {
		@Override
		public void set(float signal) {
			transferA = signal - 1.f;
			transferB = (float) (output * Math.pow(threshold, -transferA));
		}
	};

	public final SignalInput ctrlAttack = new SignalInput() {
		@Override
		public void set(float signal) {
			attack = (float) Math.exp(-1.f / signal);
		}
	};

	public final SignalInput ctrlRelease = new SignalInput() {
		@Override
		public void set(float signal) {
			release = (float) Math.exp(-1.f / signal);
			envelopeDecay = (float) Math.exp(-4.f / signal);

		}
	};

	public final SignalInput ctrlOutput = new SignalInput() {
		@Override
		public void set(float signal) {
			output = signal;
			transferB = (float) (output * Math.pow(threshold, -transferA));
		}
	};

	public final DefaultSignalOutput meterOut = new DefaultSignalOutput();

	private float threshold;
	private float attack;
	private float release;
	private float output;
	private float transferA, transferB;
	private float envelope, gain;
	private float envelopeDecay;

	public Compressor() {
		threshold = 1.f;
		attack = release = envelopeDecay = 0.f;
		output = 1.f;

		transferA = 0.f;
		transferB = 1.f;

		envelope = 0.f;
		gain = 1.f;
	}

	@Override
	public void set(float signal) {
		float abs = Math.abs(signal) + DC_OFFSET;

		envelope = abs >= envelope ? abs : abs + envelopeDecay * (envelope - abs);

		float transferGain = (float) (envelope > threshold ? Math.pow(envelope, transferA) * transferB : output);

		gain = transferGain < gain ? transferGain + attack * (gain - transferGain) : transferGain + release * (gain - transferGain);

		connectedInput.set(signal * gain);
		meterOut.connectedInput.set(gain - (output - 1));
	}
}
