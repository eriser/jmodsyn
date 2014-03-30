package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.MultiAdder;

public class Adder8Model extends DspBlockModel<MultiAdder> {

	public Adder8Model(MultiAdder amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.add[0], "add1", 0, -100, 100));
		add(new InputModel(this, amp.add[1], "add2", 0, -100, 100));
		add(new InputModel(this, amp.add[2], "add3", 0, -100, 100));
		add(new InputModel(this, amp.add[3], "add4", 0, -100, 100));
		add(new InputModel(this, amp.add[4], "add5", 0, -100, 100));
		add(new InputModel(this, amp.add[5], "add6", 0, -100, 100));
		add(new InputModel(this, amp.add[6], "add7", 0, -100, 100));

		add(new OutputModel(this, amp, "OUT"));
	}

	public Adder8Model() {
		this(new MultiAdder(7));
	}

	@Override
	public String getName() {
		return "Add8";
	}
}
