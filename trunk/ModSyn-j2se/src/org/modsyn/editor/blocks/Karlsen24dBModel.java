package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Karlsen24dB;

public class Karlsen24dBModel extends DspBlockModel<Karlsen24dB> {

	public Karlsen24dBModel(Karlsen24dB amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.ctrlFreq, "frq", 0.1f, 0, 1));
		add(new InputModel(this, amp.ctrlReso, "Q", 5, 0, 50));

		add(new OutputModel(this, amp, "OUT"));
	}

	public Karlsen24dBModel(Context c) {
		this(new Karlsen24dB());
	}

	@Override
	public String getName() {
		return "Resonator";
	}

}
