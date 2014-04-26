package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Cubic;

public class CubicModel extends DspBlockModel<Cubic> {

	public CubicModel(Cubic dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.ctrl, "amount", 0, 0, 1));
		add(new OutputModel(this, dsp, "OUT"));
	}

	public CubicModel() {
		this(new Cubic());
	}
}
