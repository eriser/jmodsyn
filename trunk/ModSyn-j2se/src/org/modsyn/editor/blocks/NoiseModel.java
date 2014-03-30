package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Noise;

public class NoiseModel extends DspBlockModel<Noise> {

	public NoiseModel(Noise dsp) {
		super(dsp);

		add(new InputModel(this, dsp.color, "col", -0.5f, -1, 1));
		add(new OutputModel(this, dsp, "OUT"));
	}

	public NoiseModel(Context c) {
		this(new Noise(c));
	}
}
