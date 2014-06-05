package org.modsyn.modules.fx;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInputIntValue;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalInsert;
import org.modsyn.modules.AllPass1stOrderFilter;

public class Phaser extends DefaultSignalOutput implements SignalInsert {
	private static final float PI2 = (float) (PI * 2.);

	private final int sr;
	private final AllPass1stOrderFilter[] stages = new AllPass1stOrderFilter[32];
	private float floor, ceiling, range;
	private float input, feedback_line;
	private float lfoPhase, lfoInc;

	public final SignalInputIntValue ctrlStages = new SignalInputIntValue(6);
	public final SignalInputValue ctrlFeedBack = new SignalInputValue(0.5f);
	public final SignalInputValue ctrlAmount = new SignalInputValue(1f);
	public final SignalInputValue ctrlLfoDepth = new SignalInputValue(1f);
	public final SignalInput ctrlLfoFreq = new SignalInput() {

		@Override
		public void set(float signal) {
			lfoInc = PI2 * (signal / sr);
		}
	};
	public final SignalInputValue ctrlInput = new SignalInputValue(0);
	public final SignalInput ctrlFloorHz = new SignalInput() {

		@Override
		public void set(float signal) {
			floor = signal / sr;
			range = ceiling - floor;
		}
	};
	public final SignalInput ctrlCeilingHz = new SignalInput() {

		@Override
		public void set(float signal) {
			ceiling = signal / sr;
			range = ceiling - floor;
		}
	};

	private final SignalInput toOut = new SignalInput() {
		@Override
		public void set(float phased_signal) {
			feedback_line = phased_signal;
			connectedInput.set(input + phased_signal * ctrlAmount.value);
		}
	};

	public Phaser(Context c) {
		super();
		this.sr = c.getSampleRate();
		for (int i = 0; i < stages.length; i++) {
			stages[i] = new AllPass1stOrderFilter();
			if (i > 0) {
				stages[i].connectTo(stages[i - 1]);
			}
		}
		stages[0].connectTo(toOut);
		ctrlStages.set(6);
		ctrlAmount.set(1);
		ctrlFeedBack.set(0.5f);
		ctrlLfoDepth.set(1);
		ctrlLfoFreq.set(0.5f);
		ctrlFloorHz.set(440);
		ctrlCeilingHz.set(1600);
	}

	@Override
	public void set(float input) {
		this.input = input; // save input to mix with output later in 'toOut'

		float dLfo = (float) (range * (((sin(lfoPhase) * ctrlLfoDepth.value) + 1.f) / 2.f));
		float dIn = range * ctrlInput.value;

		for (int i = 0; i < stages.length; i++) {
			stages[i].ctrl.set(floor + dLfo + dIn);
		}

		lfoPhase += lfoInc;
		lfoPhase %= PI2;

		stages[ctrlStages.value - 1].set(input + feedback_line * ctrlFeedBack.value);
	}
}
