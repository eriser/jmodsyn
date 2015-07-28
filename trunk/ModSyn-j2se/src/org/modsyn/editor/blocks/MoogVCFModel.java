package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.MoogVCF;

public class MoogVCFModel extends DspBlockModel<MoogVCF> {

	public MoogVCFModel(MoogVCF amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.ctrlCutOff, "frq", 0.1f, 0, 1));
		add(new InputModel(this, amp.ctrlResonance, "Q", 0, 0, 5));
		add(new InputModel(this, amp.ctrlMode, "typ", 0, 0, 2, 0));

		add(new OutputModel(this, amp, "OUT"));
	}

	public MoogVCFModel(Context c) {
		this(new MoogVCF(c));
	}

	@Override
	public String getName() {
		return "MoogVCF";
	}
}
