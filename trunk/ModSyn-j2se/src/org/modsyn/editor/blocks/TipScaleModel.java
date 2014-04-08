package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.TipScale;

public class TipScaleModel extends DspBlockModel<TipScale> {

	public TipScaleModel(TipScale dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.ctrl, "ctrl", 440, 0, 20000));
		add(new InputModel(this, dsp.center, "center", 440, 1, 20000));
		add(new InputModel(this, dsp.scale, "scale", 0, 0, 2));

		add(new OutputModel(this, dsp, "OUT"));
		add(new OutputModel(this, dsp.ctrl, "ctrl-o"));
	}

	public TipScaleModel() {
		this(new TipScale());
	}
}
