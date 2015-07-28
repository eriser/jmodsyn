package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.FilterXPole;

public class FilterXPoleModel extends DspBlockModel<FilterXPole> {

	public FilterXPoleModel(FilterXPole amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.ctrlPoles, "pol", 4, 1, 16, 0));
		add(new InputModel(this, amp.ctrlCutoff, "frq", 0.1f, 0, 1));
		add(new InputModel(this, amp.ctrlReso, "Q", 0, 0, 5));
		add(new InputModel(this, amp.ctrlMode, "typ", 0, 0, 2, 0));

		add(new OutputModel(this, amp, "OUT"));
	}

	public FilterXPoleModel(Context c) {
		this(new FilterXPole());
	}

	@Override
	public String getName() {
		return "X-Pole";
	}

}
