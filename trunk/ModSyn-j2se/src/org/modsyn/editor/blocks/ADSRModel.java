package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.ctrl.ADSREnvelope;

public class ADSRModel extends DspBlockModel<ADSREnvelope> {

	public ADSRModel(Context c) {
		this(new ADSREnvelope(c));
	}

	public ADSRModel(ADSREnvelope dsp) {
		super(dsp);
		add(new InputModel(this, dsp.trigger, "trg", 0, 0, 1));
		add(new InputModel(this, dsp.ctrlAtkLevel, "atL", 0, 0, 1));
		add(new InputModel(this, dsp.ctrlAtkTime, "atT", 0.1f, 0, 1));
		add(new InputModel(this, dsp.ctrlDcyLevel, "dcL", 1, 0, 1));
		add(new InputModel(this, dsp.ctrlDcyTime, "dcT", 0.3f, 0, 1));
		add(new InputModel(this, dsp.ctrlSusLevel, "suL", .5f, 0, 1));
		add(new InputModel(this, dsp.ctrlRlsLevel, "rlL", 0f, 0, 1));
		add(new InputModel(this, dsp.ctrlRlsTime, "rlT", .2f, 0, 1));

		add(new OutputModel(this, dsp, "OUT"));
	}

	@Override
	public String getName() {
		return "ADSR";
	}
}