package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.logic.SwitchValue;

public class SwitchValueModel extends DspBlockModel<SwitchValue> {

	public SwitchValueModel(SwitchValue dsp) {
		super(dsp);

		add(new InputModel(this, dsp.trigger1, "trigger1", 0, 0, 1));
		add(new InputModel(this, dsp.trigger2, "trigger2", 0, 0, 1));
		add(new InputModel(this, dsp.value1, "value1", 0, 0, 1));
		add(new InputModel(this, dsp.value2, "value2", 0, 0, 1));
		add(new OutputModel(this, dsp, "OUT"));
	}

	public SwitchValueModel() {
		this(new SwitchValue());
	}
}
