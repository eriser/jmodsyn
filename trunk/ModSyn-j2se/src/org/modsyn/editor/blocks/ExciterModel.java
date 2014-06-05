package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.fx.Exciter;

public class ExciterModel extends DspBlockModel<Exciter> {

	public ExciterModel(Exciter dsp) {
		super(dsp);

		add(new InputModel(this, dsp.inL, "IN-L", 0, -1, 1));
		add(new InputModel(this, dsp.inR, "IN-R", 0, -1, 1));
		add(new InputModel(this, dsp.ctrlRange, "range", 0.5f, 0.1f, 1));
		add(new InputModel(this, dsp.ctrlBias, "bias", 0, -.5f, .5f));
		add(new InputModel(this, dsp.ctrlDrive, "drive", 1f, 0, 4));
		add(new InputModel(this, dsp.ctrlDry, "dry", 1, 0, 1));
		add(new InputModel(this, dsp.ctrlEffect, "effect", 0, 0, 1));
		add(new OutputModel(this, dsp.outL, "OUT-L"));
		add(new OutputModel(this, dsp.outR, "OUT-R"));
	}

	public ExciterModel(Context c) {
		this(new Exciter(c));
	}
}
