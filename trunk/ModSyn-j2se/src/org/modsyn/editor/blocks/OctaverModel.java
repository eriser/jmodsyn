package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Octaver;

public class OctaverModel extends DspBlockModel<Octaver> {

	public OctaverModel(Octaver dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.mixUpDown, "mix", -0.5f, -1, 1));
		add(new OutputModel(this, dsp, "OUT"));
	}

	public OctaverModel() {
		this(new Octaver());
	}
}
