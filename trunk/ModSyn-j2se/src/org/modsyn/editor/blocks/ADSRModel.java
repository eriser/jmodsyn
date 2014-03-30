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
		add(new InputModel(this, dsp.trigger, "trig", 0, 0, 1));
		add(new InputModel(this, dsp.ctrlAtkLevel, "at-lvl", 0, 0, 1));
		add(new InputModel(this, dsp.ctrlAtkTime, "at-tim", 0.1f, 0, 1));
		add(new InputModel(this, dsp.ctrlDcyLevel, "dc-lvl", 1, 0, 1));
		add(new InputModel(this, dsp.ctrlDcyTime, "dc-tim", 0.3f, 0, 1));
		add(new InputModel(this, dsp.ctrlSusLevel, "su-lvl", .5f, 0, 1));
		add(new InputModel(this, dsp.ctrlRlsLevel, "rl-lvl", 0f, 0, 1));
		add(new InputModel(this, dsp.ctrlRlsTime, "rl-tim", .2f, 0, 1));

		add(new OutputModel(this, dsp, "OUT"));
	}

	@Override
	public String getName() {
		return "ADSR";
	}

}
