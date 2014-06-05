package org.modsyn.modules;

import static java.lang.Math.abs;
import static java.lang.Math.exp;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

public class Tracker2 extends DefaultSignalOutput implements SignalInsert {

	private final int sampleRate;
	private float envelope;
	private float attack;
	private float release;
	private float amp, bias;

	public final SignalInput ctrlAmp = new SignalInput() {
		@Override
		public void set(float value) {
			amp = value;
		}
	};
	public final SignalInput ctrlBias = new SignalInput() {
		@Override
		public void set(float value) {
			bias = value;
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

	public Tracker2(Context c) {
		super();
		this.sampleRate = c.getSampleRate();
		this.amp = 1;
		setAttackMillis(100);
		setReleaseMillis(100);
	}

	public void setAttackMillis(float attackMillis) {
		attack = (float) exp(Math.log(0.01) / (attackMillis * sampleRate * 0.001));
	}

	public void setReleaseMillis(float releaseMillis) {
		release = (float) exp(Math.log(0.01) / (releaseMillis * sampleRate * 0.001));
	}

	@Override
	public void set(float in) {
		float abs = abs(in);
		if (abs > envelope) {
			envelope = attack * (envelope - abs) + abs;
		} else {
			envelope = release * (envelope - abs) + abs;
		}

		connectedInput.set(bias + envelope * amp);
	}

}
