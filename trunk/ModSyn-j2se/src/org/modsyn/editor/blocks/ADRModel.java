package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.ADR;

public class ADRModel extends DspBlockModel<ADR> {

	public ADRModel(Context c) {
		this(new ADR(c));
	}

	public ADRModel(ADR dsp) {
		super(dsp);
		add(new InputModel(this, dsp.velo, "velo", 0, 0, 1));
		add(new InputModel(this, dsp.trigger, "trig", 0, 0, 1));
		add(new InputModel(this, dsp.velo_sens, "v-sens", 1, 0, 1));
		add(new InputModel(this, dsp.ctrlAttack, "attack", 1.01f, 1, 2));
		add(new InputModel(this, dsp.ctrlDecay, "decay", 0.9999f, 0.999f, 1, 7));
		add(new InputModel(this, dsp.ctrlRelease, "release", 0.9999f, 0.999f, 1, 7));

		add(new OutputModel(this, dsp, "OUT"));
	}
}
