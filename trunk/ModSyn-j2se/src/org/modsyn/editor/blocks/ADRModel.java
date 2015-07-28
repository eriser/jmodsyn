package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.ADSR;

public class ADRModel extends DspBlockModel<ADSR> {

	public ADRModel(Context c) {
		this(new ADSR(c));
	}

	public ADRModel(ADSR dsp) {
		super(dsp);
		add(new InputModel(this, dsp.velo, "vel", 0, 0, 1));
		add(new InputModel(this, dsp.trigger, "trg", 0, 0, 1));
		add(new InputModel(this, dsp.velo_sens, "sen", 1, 0, 1));
		add(new InputModel(this, dsp.ctrlAttack, "atk", 0.001f, 0, 0.005f, 7));
		add(new InputModel(this, dsp.ctrlDecay, "dcy", 0.9999f, 0.999f, 1, 7));
		add(new InputModel(this, dsp.ctrlSustain, "sus", 0f, 0, 1, 2));
		add(new InputModel(this, dsp.ctrlRelease, "rel", 0.9999f, 0.999f, 1, 7));

		add(new OutputModel(this, dsp, "OUT"));
	}
}
