package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.NoiseGate;

public class NoiseGateModel extends DspBlockModel<NoiseGate> {

	public NoiseGateModel(NoiseGate dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.ctrlThreshold, "thr", 0.5f, 0, 1));
		add(new InputModel(this, dsp.ctrlAttack, "atk", 0.0005f, 0.000010f, 0.001f, 6));
		add(new InputModel(this, dsp.ctrlHold, "hld", 0.2f, 0.1f, 1));
		add(new InputModel(this, dsp.ctrlRelease, "rel", 0.997500f, 0.997000f, 0.999999f, 6));
		add(new OutputModel(this, dsp, "OUT"));
	}

	public NoiseGateModel(Context c) {
		this(new NoiseGate(c));
	}
}
