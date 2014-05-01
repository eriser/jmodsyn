/*
 * Created on 26-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * Tube simulation with asymmetrical soft-clipping and envelope dependent duty-cycle modulation.
 * 
 * @author Erik Duijs
 */
public class TubeSim extends DefaultSignalOutput implements SignalInsert {

	private float thresholdTop = 0.75f;
	private float thresholdBottom = 0.75f;
	private float thresholdDCM = 0.75f;
	private float dcm = 1;
	private float gain = 1;
	private float envelope;
	private final int sampleRate;
	private float attack;
	private float release;

	public TubeSim(Context c) {
		super();
		this.sampleRate = c.getSampleRate();
		setAttackMillis(10);
		setReleaseMillis(10);
	}

	public SignalInput ctrlGain = new SignalInput() {
		@Override
		public void set(float signal) {
			gain = signal;
		}
	};
	public SignalInput ctrlThresholdTop = new SignalInput() {
		@Override
		public void set(float signal) {
			thresholdTop = signal;
		}
	};
	public SignalInput ctrlThresholdBottom = new SignalInput() {
		@Override
		public void set(float signal) {
			thresholdBottom = signal;
		}
	};

	public SignalInput ctrlThresholdDCM = new SignalInput() {
		@Override
		public void set(float signal) {
			thresholdDCM = signal;
		}
	};
	public SignalInput ctrlDCM = new SignalInput() {
		@Override
		public void set(float signal) {
			dcm = signal;
		}
	};

	public final SignalInput ctrlAttack = new SignalInput() {
		@Override
		public void set(float signal) {
			setAttackMillis(signal);
		}
	};
	public final SignalInput ctrlRelease = new SignalInput() {
		@Override
		public void set(float signal) {
			setReleaseMillis(signal);
		}
	};

	public void setAttackMillis(float attackMillis) {
		attack = (float) Math.exp(Math.log(0.01) / (attackMillis * sampleRate * 0.001));
	}

	public void setReleaseMillis(float releaseMillis) {
		release = (float) Math.exp(Math.log(0.01) / (releaseMillis * sampleRate * 0.001));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SignalInput#write(float)
	 */
	@Override
	public void set(float in) {
		connectedInput.set(process(in, gain, thresholdTop, thresholdBottom));
	}

	public final float process(float signal, float gain, float thresholdTop, float thresholdBottom) {

		// input gain
		signal *= gain;

		// Envelope follower
		float abs = Math.abs(signal);
		if (abs > envelope) {
			envelope = attack * (envelope - abs) + abs;
		} else {
			envelope = release * (envelope - abs) + abs;
		}

		// Duty-cycle modulation depending on the signal envelope
		float bias = 0;
		if (envelope > thresholdDCM) {
			bias = (envelope - thresholdDCM) * dcm;
		}
		signal += bias;

		// Positive soft-clipping
		if (signal > thresholdTop) {
			double t1 = 1.0 - thresholdTop;
			signal = (float) (thresholdTop + t1 * Math.tanh((signal - thresholdTop) / t1));
		}

		// Negative soft-clipping
		signal *= -1;
		if (signal > thresholdBottom) {
			double t1 = 1.0 - thresholdBottom;
			signal = (float) (thresholdBottom + t1 * Math.tanh((signal - thresholdBottom) / t1));
		}
		signal *= -1;

		final float input = signal;

		// signal = input - input_buf + 0.995f * output_buf;

		input_buf = input;
		output_buf = signal;

		return signal;
	}

	float input_buf, output_buf;
}
