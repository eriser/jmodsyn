package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Mix;

public class MixerModel extends DspBlockModel<Mix> {

	public MixerModel(Mix dsp) {
		super(dsp, dsp.inputs.length);

		for (int i = 0; i < dsp.inputs.length; i++) {
			add(new InputModel(this, dsp.inputs[i], "in." + (i + 1), 0, -1, 1));
		}

		add(new OutputModel(this, dsp, "OUT"));
	}

	public MixerModel(Context c, int channels) {
		this(new Mix(c, channels));
	}

	@Override
	public String getName() {
		return "Mix";
	}
}
