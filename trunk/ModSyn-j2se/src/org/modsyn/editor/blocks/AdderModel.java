package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Adder;

public class AdderModel extends DspBlockModel<Adder> {

	public AdderModel(Adder amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.control, "add", 0, -100, 100));

		add(new OutputModel(this, amp, "OUT"));
	}

	public AdderModel() {
		this(new Adder());
	}

	@Override
	public String getName() {
		return "Add";
	}
}
