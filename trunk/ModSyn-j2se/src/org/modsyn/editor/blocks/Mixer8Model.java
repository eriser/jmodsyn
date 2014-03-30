package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Mix;

public class Mixer8Model extends DspBlockModel<Mix> {

	public Mixer8Model(Mix amp) {
		super(amp);

		for (int i = 0; i < amp.inputs.length; i++) {
			add(new InputModel(this, amp.inputs[i], "in." + (i + 1), 0, -1, 1));
		}

		add(new OutputModel(this, amp, "OUT"));
	}

	public Mixer8Model(Context c) {
		this(new Mix(c, 8));
	}

	@Override
	public String getName() {
		return "Mix8";
	}
}
