package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.VeloSens;

public class VeloSensModel extends DspBlockModel<VeloSens> {

	public VeloSensModel(VeloSens dsp) {
		super(dsp);

		add(new InputModel(this, dsp.velo, "vel", 0, 0, 1));
		add(new InputModel(this, dsp.sens, "sen", 1, 0, 2));
		add(new InputModel(this, dsp.bias, "bias", 0, 0, 1));
		add(new InputModel(this, dsp.pow, "pow", 1, 0, 1));
		add(new OutputModel(this, dsp, "OUT"));
	}

	public VeloSensModel() {
		this(new VeloSens());
	}
}
