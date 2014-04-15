package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalSource;

public class ADR extends DefaultSignalOutput implements SignalSource {

	private static final int STATE_RELEASE = 0;
	private static final int STATE_ATTACK = 1;
	private static final int STATE_DECAY = 2;

	private int state;
	private float attack = 1.01f, decay = 0.99f, release = 0.5f;
	private float signal;
	private float amp;

	public final SignalInput trigger = new SignalInput() {
		@Override
		public void set(float signal) {
			if (signal != 0) {
				state = STATE_ATTACK;
			} else {
				state = STATE_RELEASE;
			}
		}
	};
	public final SignalInput velo = new SignalInput() {
		@Override
		public void set(float signal) {
			if (signal != 0) {
				amp = signal * velo_sens.value + (1 - velo_sens.value);

			}
		}
	};

	public final SignalInputValue velo_sens = new SignalInputValue(1);

	public final SignalInput ctrlAttack = new SignalInput() {
		@Override
		public void set(float signal) {
			attack = signal;
		}
	};
	public final SignalInput ctrlDecay = new SignalInput() {
		@Override
		public void set(float signal) {
			decay = signal;
		}
	};
	public final SignalInput ctrlRelease = new SignalInput() {
		@Override
		public void set(float signal) {
			release = signal;
		}
	};

	public ADR(Context c) {
		c.addSignalSource(this);
	}

	@Override
	public void updateSignal() {

		switch (state) {
		case STATE_ATTACK:
			signal += attack;
			if (signal > 1) {
				signal = 1;
				state = STATE_DECAY;
			}
			break;
		case STATE_RELEASE:
			signal *= release;
			if (signal < MIN_NORMAL) {
				signal = 0;
			}
			break;
		case STATE_DECAY:
			signal *= decay;
			if (signal < MIN_NORMAL) {
				signal = 0;
			}
		}

		connectedInput.set(signal * amp);
	}
}
