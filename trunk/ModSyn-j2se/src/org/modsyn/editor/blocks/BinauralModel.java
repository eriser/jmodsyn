package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Binaural;

public class BinauralModel extends DspBlockModel<Binaural> {

	public BinauralModel(Binaural dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.posX, "posX", 0, -10, 10));
		add(new InputModel(this, dsp.posY, "posY", 0, -10, 10));
		add(new OutputModel(this, dsp.leftEar.out, "OUT-L"));
		add(new OutputModel(this, dsp.rightEar.out, "OUT-R"));
	}

	public BinauralModel(Context c) {
		this(new Binaural(c));
	}
}
