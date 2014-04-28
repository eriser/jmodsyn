package org.modsyn.modules.fx;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.modules.DryWet;
import org.modsyn.modules.Tracker2;
import org.modsyn.modules.VarDelayHQ;

/**
 * Envelope delay
 * 
 * @author Erik Duijs
 */
public class EnvelopeDelay implements SignalInsert {

	private float amp;
	private final VarDelayHQ delay;
	private final DryWet dw;
	private final Tracker2 tracker;

	public final SignalInput ctrlMix = new SignalInput() {

		@Override
		public void set(float signal) {
			dw.ctrl.set(signal);
		}
	};

	public final SignalInput ctrlMod = new SignalInput() {
		@Override
		public void set(float signal) {
			amp = signal;
			tracker.ctrlAmp.set(signal);
		}
	};
	public final SignalInput ctrlDly = new SignalInput() {
		@Override
		public void set(float signal) {
			if (signal + amp < 0.5f && signal - amp > 0) {
				tracker.ctrlBias.set(signal);
			}
		}
	};
	public final SignalInput ctrlFB = new SignalInput() {
		@Override
		public void set(float signal) {
			if (signal < 1) {
				delay.feedbackControl.set(signal);
			}
		}
	};
	public final SignalInput ctrlSpd = new SignalInput() {

		@Override
		public void set(float signal) {
			tracker.ctrlAttack.set(signal);
			tracker.ctrlRelease.set(signal);
		}
	};

	public EnvelopeDelay(Context context) {
		this.delay = new VarDelayHQ(context, 0.5f, 0.2f, 0);
		this.dw = new DryWet(delay);
		this.tracker = new Tracker2(context);

		dw.ctrl.set(0.5f);
		ctrlMod.set(0.001f);
		ctrlSpd.set(200);
		ctrlDly.set(0.002f);
		tracker.connectTo(delay.delayTimeControl);
	}

	@Override
	public void set(float signal) {
		tracker.set(signal);
		dw.set(signal);
	}

	@Override
	public void connectTo(SignalInput input) {
		dw.connectTo(input);
	}

}
