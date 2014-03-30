package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Tracker;

public class EnvelopeFollowerModel extends DspBlockModel<Tracker> {

	public EnvelopeFollowerModel(Tracker amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.ctrlSpeed, "spd", 2000, 0, 20000));
		add(new InputModel(this, amp.ctrlAmp, "amp", 1, -2, 2));
		add(new InputModel(this, amp.ctrlBias, "bias", 0, 0, 1));

		add(new OutputModel(this, amp, "OUT"));
	}

	public EnvelopeFollowerModel() {
		this(new Tracker());
	}

	@Override
	public String getName() {
		return "Tracker";
	}
}
