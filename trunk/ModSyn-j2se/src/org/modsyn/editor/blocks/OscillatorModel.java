package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Oscillator;
import org.modsyn.util.WaveTables;

public class OscillatorModel extends DspBlockModel<Oscillator> {

	public OscillatorModel(Context c, Oscillator o) {
		super(o);

		add(new InputModel(this, o.ctrFreq, "freq", 440f, 0, 48000));
		add(new InputModel(this, o.ctrPWM, "pwm", 50, 0, 100));
		add(new InputModel(this, o.ctrDetune, "det", 1, 0.9f, 1.1f));
		add(new InputModel(this, o.ctrShape, "shape", 0, 0, WaveTables.SHAPE_ID_MAX, 0));

		add(new OutputModel(this, o, "OUT"));
	}

	public OscillatorModel(Context c) {
		this(c, new Oscillator(c));
	}

	@Override
	public String getName() {
		return "Osc";
	}
}
