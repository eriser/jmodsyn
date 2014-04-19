package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Tracker2;

public class EnvelopeFollower2Model extends DspBlockModel<Tracker2> {

	public EnvelopeFollower2Model(Tracker2 dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.ctrlAttack, "atk", 100, 0, 1000, 1));
		add(new InputModel(this, dsp.ctrlRelease, "rel", 100, 0, 1000, 1));
		add(new InputModel(this, dsp.ctrlAmp, "amp", 1, -2, 2));
		add(new InputModel(this, dsp.ctrlBias, "bias", 0, 0, 1));

		add(new OutputModel(this, dsp, "OUT"));
	}

	public EnvelopeFollower2Model(Context c) {
		this(new Tracker2(c));
	}

	@Override
	public String getName() {
		return "Tracker2";
	}
}
