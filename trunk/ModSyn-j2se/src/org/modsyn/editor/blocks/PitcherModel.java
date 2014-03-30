package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.PitchShifter;

public class PitcherModel extends DspBlockModel<PitchShifter> {

	public PitcherModel(PitchShifter dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.ctrlPitch, "pitch", 0.5f, 0.5f, 2));
		add(new OutputModel(this, dsp, "OUT"));
	}

	public PitcherModel() {
		this(new PitchShifter());
	}

	@Override
	public String getName() {
		return "PitchShift";
	}
}
