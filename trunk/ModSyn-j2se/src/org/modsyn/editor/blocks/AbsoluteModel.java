package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Absolute;

public class AbsoluteModel extends DspBlockModel<Absolute> {

	public AbsoluteModel(Absolute dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.ctrlBias, "bias", -0.5f, -1, 1));
		add(new OutputModel(this, dsp, "OUT"));
	}

	public AbsoluteModel() {
		this(new Absolute());
	}
}
