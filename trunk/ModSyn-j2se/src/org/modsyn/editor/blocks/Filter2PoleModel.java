package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Filter2Pole;

public class Filter2PoleModel extends DspBlockModel<Filter2Pole> {

	public Filter2PoleModel(Filter2Pole amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.ctrlCutoff, "freq", 0.1f, 0, 1));
		add(new InputModel(this, amp.ctrlReso, "reso", 0, 0, 5));
		add(new InputModel(this, amp.ctrlMode, "mode", 0, 0, 2, 0));

		add(new OutputModel(this, amp, "OUT"));
	}

	public Filter2PoleModel(Context c) {
		this(new Filter2Pole());
	}

	@Override
	public String getName() {
		return "2-Pole";
	}

}
