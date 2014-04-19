package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.OscillatorHQ;
import org.modsyn.util.WaveTables;

public class OscillatorHQModel extends DspBlockModel<OscillatorHQ> {

	public OscillatorHQModel(Context c, OscillatorHQ o) {
		super(o);

		add(new InputModel(this, o.ctrFreq, "freq", 440f, 0, 48000));
		add(new InputModel(this, o.ctrPWM, "pwm", 50, 0, 100));
		add(new InputModel(this, o.ctrDetune, "det", 1, 0.9f, 1.1f));
		add(new InputModel(this, o.ctrShape, "shape", 0, 0, WaveTables.SHAPE_ID_MAX, 0));
		add(new InputModel(this, o.ctrlOversampling, "o/s", 1, 1, 16, 0));
		add(new InputModel(this, o.ctrFilter, "filter", 1, 0.01f, 0.45f));

		add(new OutputModel(this, o, "OUT"));
	}

	public OscillatorHQModel(Context c) {
		this(c, new OscillatorHQ(c));
	}

	@Override
	public String getName() {
		return "Osc-HQ";
	}
}
