package org.modsyn.modules;

import java.util.Random;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalSource;

public class Noise extends DefaultSignalOutput implements SignalSource {

	public final SignalInputValue color = new SignalInputValue(0.5f);

	private float signal, delta;
	private final Random rng = new Random();

	public Noise(Context c) {
		super();
		c.addSignalSource(this);
	}

	@Override
	public void updateSignal() {
		float rnd = (rng.nextFloat() - 0.5f) * color.value;
		delta += rnd;
		signal = signal + delta;
		if (signal < -1) {
			signal = -1;
			delta = 0;
		} else if (signal > 1) {
			signal = 1;
			delta = 0;
		}

		connectedInput.set(signal);
	}
}
