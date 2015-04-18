package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.logic.Or;

public class OrModel extends DspBlockModel<Or> {

	public OrModel(Or dsp) {
		super(dsp);

		add(new InputModel(this, dsp.in1, "in-1", 0, 0, 1));
		add(new InputModel(this, dsp.in2, "in-2", 0, 0, 1));
		add(new OutputModel(this, dsp, "OUT"));
	}

	public OrModel() {
		this(new Or());
	}
}
