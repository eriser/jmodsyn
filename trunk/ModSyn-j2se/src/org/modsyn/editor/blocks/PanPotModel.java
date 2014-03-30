package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.PanPot;

public class PanPotModel extends DspBlockModel<PanPot> {

	public PanPotModel() {
		this(new PanPot());
	}

	public PanPotModel(PanPot dsp) {
		super(dsp);
		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.panControl, "pan", 0, -1, 1));

		add(new OutputModel(this, dsp.outputL, "OUT.L"));
		add(new OutputModel(this, dsp.outputR, "OUT.R"));
	}

	@Override
	public String getName() {
		return "Pan";
	}
}
