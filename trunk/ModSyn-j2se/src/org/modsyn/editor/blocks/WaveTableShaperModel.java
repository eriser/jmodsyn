package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.WaveTableShaper;
import org.modsyn.util.WaveTables;

public class WaveTableShaperModel extends DspBlockModel<WaveTableShaper> {

	public WaveTableShaperModel(WaveTableShaper dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.ctrShape, "shp", 0, 0, WaveTables.SHAPE_ID_MAX, 0));
		add(new InputModel(this, dsp.ctrMix, "mix", 0, 0, 1));
		add(new OutputModel(this, dsp, "OUT"));
	}

	public WaveTableShaperModel() {
		this(new WaveTableShaper());
	}
}
