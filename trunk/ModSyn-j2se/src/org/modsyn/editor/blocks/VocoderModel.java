package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.fx.Vocoder;

public class VocoderModel extends DspBlockModel<Vocoder> {

	public VocoderModel(Vocoder dsp) {
		super(dsp);

		add(new InputModel(this, dsp.inputModulator, "IN.M", 0, -1, 1));
		add(new InputModel(this, dsp.inputCarrier, "IN.C", 0, -1, 1));
		add(new InputModel(this, dsp.ctrlLow, "low", 0.01f, 0, 1));
		add(new InputModel(this, dsp.ctrlHigh, "high", 0.5f, 0, 1));
		add(new InputModel(this, dsp.ctrlPoles, "poles", 8, 0, 16, 0));
		add(new InputModel(this, dsp.ctrlReso, "reso", 1f, 0, 5));

		add(new OutputModel(this, dsp, "OUT"));
	}

	public VocoderModel() {
		this(new Vocoder());
	}

	@Override
	public String getName() {
		return "Vocoder";
	}
}
