package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.modules.ext.ToJavaSound;

public class ToJavaSoundModel extends DspBlockModel<ToJavaSound> {

	public ToJavaSoundModel(ToJavaSound dsp) {
		super(dsp);
		add(new InputModel(this, dsp.inputL, "IN.L", 0, -1, 1));
		add(new InputModel(this, dsp.inputR, "IN.R", 0, -1, 1));
	}

	@Override
	public String getName() {
		return "Audio OUT";
	}
}
