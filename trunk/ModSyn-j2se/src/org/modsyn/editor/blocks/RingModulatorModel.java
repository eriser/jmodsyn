package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.RingModulator;

public class RingModulatorModel extends DspBlockModel<RingModulator> {

	public RingModulatorModel(RingModulator dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.modInput, "mod", 0, -1, 1));
		add(new InputModel(this, dsp.modulationControl, "amount", 0, 0, 1));
		add(new OutputModel(this, dsp, "OUT"));
	}

	public RingModulatorModel(Context c) {
		this(new RingModulator(c));
	}

	@Override
	public String getName() {
		return "RingMod";
	}
}
