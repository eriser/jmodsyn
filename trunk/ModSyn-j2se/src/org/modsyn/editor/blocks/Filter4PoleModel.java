package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Filter4Pole;

public class Filter4PoleModel extends DspBlockModel<Filter4Pole> {

	public Filter4PoleModel(Filter4Pole amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.ctrlCutoff, "freq", 0.1f, 0, 1));
		add(new InputModel(this, amp.ctrlReso, "reso", 0, 0, 5));
		add(new InputModel(this, amp.ctrlMode, "mode", 0, 0, 2, 0));

		add(new OutputModel(this, amp, "OUT"));
	}

	public Filter4PoleModel(Context c) {
		this(new Filter4Pole());
	}

	@Override
	public String getName() {
		return "4-Pole";
	}

}
