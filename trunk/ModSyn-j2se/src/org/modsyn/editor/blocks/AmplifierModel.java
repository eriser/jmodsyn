package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Amplifier;

public class AmplifierModel extends DspBlockModel<Amplifier> {

	public AmplifierModel(Amplifier amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.control, "lvl", .5f, 0, 2));

		add(new OutputModel(this, amp, "OUT"));
	}

	public AmplifierModel() {
		this(new Amplifier());
	}

	@Override
	public String getName() {
		return "Amp";
	}
}
