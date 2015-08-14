package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Compressor;

public class CompressorModel extends DspBlockModel<Compressor> {

	public CompressorModel(Compressor dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.ctrlThreshold, "thr", .1f, 0, 1f));
		add(new InputModel(this, dsp.ctrlRatio, "rat", 1, 0, 1));
		add(new InputModel(this, dsp.ctrlAttack, "atk", 500, 0, 2000));
		add(new InputModel(this, dsp.ctrlRelease, "rel", 500, 0, 2000));
		add(new InputModel(this, dsp.ctrlOutput, "out", 1, 0, 5));

		add(new OutputModel(this, dsp, "OUT"));
		add(new OutputModel(this, dsp.meterOut, "meter"));
	}

	public CompressorModel() {
		this(new Compressor());
	}
}
