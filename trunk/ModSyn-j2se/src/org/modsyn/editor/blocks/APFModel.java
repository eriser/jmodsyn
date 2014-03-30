package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.VarAllPassFilter;

public class APFModel extends DspBlockModel<VarAllPassFilter> {

	public APFModel(VarAllPassFilter amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.delayTimeControl, "dly", 0.0015f, 1, 1));
		add(new InputModel(this, amp.feedbackControl, "fb", .9f, 0, 0.99f));

		add(new OutputModel(this, amp, "OUT"));
	}

	public APFModel(Context c) {
		this(new VarAllPassFilter(c, 2, 0.0015f, 0.9f));
	}

	@Override
	public String getName() {
		return "APF";
	}
}
