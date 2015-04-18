package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.logic.IfRange;

public class IfRangeModel extends DspBlockModel<IfRange> {

	public IfRangeModel(IfRange dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.low, "low", 0, -1, 1));
		add(new InputModel(this, dsp.high, "high", 0, -1, 1));
		add(new OutputModel(this, dsp, "trigger"));
		add(new OutputModel(this, dsp.outTrue, "out-true"));
		add(new OutputModel(this, dsp.outFalse, "out-false"));
	}

	public IfRangeModel() {
		this(new IfRange());
	}
}
