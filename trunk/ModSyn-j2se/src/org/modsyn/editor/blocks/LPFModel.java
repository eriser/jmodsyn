package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.LPF;

public class LPFModel extends DspBlockModel<LPF> {

	public LPFModel(LPF amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.cutOffControl, "frq", 1000, 1, 20000));
		add(new InputModel(this, amp.resonanceControl, "Q", 1, 1, 10));

		add(new OutputModel(this, amp, "OUT"));
	}

	public LPFModel(Context c) {
		this(new LPF(c));
	}

	@Override
	public String getName() {
		return "LPF";
	}

}
