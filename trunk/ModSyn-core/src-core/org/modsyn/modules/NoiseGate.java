package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalInsert;

public class NoiseGate extends DefaultSignalOutput implements SignalInsert {

	private static final int STATE_CLOSE = 0;
	private static final int STATE_ATTACK = 1;
	private static final int STATE_HOLD = 3;
	private static final int STATE_RELEASE = 4;

	private final Context c;

	public NoiseGate(Context c) {
		super();
		this.c = c;
	}

	private int state = STATE_CLOSE;
	private float amp = 0;
	private int hold, holdCounter;

	public final SignalInputValue ctrlThreshold = new SignalInputValue();
	public final SignalInputValue ctrlAttack = new SignalInputValue(0.001f);
	public final SignalInputValue ctrlRelease = new SignalInputValue(0.999f);
	public final SignalInput ctrlHold = new SignalInput() {
		@Override
		public void set(float signal) {
			hold = (int) (signal * c.getSampleRate());
		}
	};

	@Override
	public void set(float signal) {
		switch (state) {
		case STATE_CLOSE:
			if (signal > ctrlThreshold.value) {
				state = STATE_ATTACK;
				holdCounter = hold;
			}
			break;
		case STATE_ATTACK:
			amp += ctrlAttack.value;
			if (amp >= 1) {
				amp = 1;
				state = STATE_HOLD;
				holdCounter = hold;
			} else {
				if (signal > ctrlThreshold.value) {
					holdCounter = hold;
				} else if (--holdCounter <= 0) {
					state = STATE_RELEASE;
				}
			}
			break;
		case STATE_HOLD:
			if (signal > ctrlThreshold.value) {
				holdCounter = hold;
			} else if (--holdCounter <= 0) {
				state = STATE_RELEASE;
			}
			break;
		case STATE_RELEASE:
			amp *= ctrlRelease.value;
			if (amp <= MIN_NORMAL) {
				amp = 0;
				state = STATE_CLOSE;
			}
			break;
		}

		connectedInput.set(signal * amp);
	}
}
