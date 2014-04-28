package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.fx.Reverb242;

public class Reverb242Model extends DspBlockModel<Reverb242> {

	public Reverb242Model(Reverb242 dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.left, "IN-L", 0, -1, 1));
		add(new InputModel(this, dsp.right, "IN-R", 0, -1, 1));

		add(new InputModel(this, dsp.mixControl, "mix", 0.05f, 0, 1));
		add(new InputModel(this, dsp.timeControl, "size", 1, 0, 10));
		add(new InputModel(this, dsp.decayControl, "decay", 0.9f, 0, 1));
		add(new InputModel(this, dsp.densityControl, "dens", 0.5f, 0, 1));

		add(new OutputModel(this, dsp.left, "OUT-L"));
		add(new OutputModel(this, dsp.right, "OUT-R"));
	}

	public Reverb242Model(Context c) {
		this(new Reverb242(c));
	}
}
