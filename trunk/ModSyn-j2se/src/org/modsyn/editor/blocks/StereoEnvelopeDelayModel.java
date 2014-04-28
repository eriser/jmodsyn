package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.fx.EnvelopeDelayStereo;

public class StereoEnvelopeDelayModel extends DspBlockModel<EnvelopeDelayStereo> {

	public StereoEnvelopeDelayModel(EnvelopeDelayStereo dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));

		add(new InputModel(this, dsp.ctrlMix, "mix", 0.5f, 0, 1));
		add(new InputModel(this, dsp.ctrlDly, "dly", 0.2f, 0, 0.5f));
		add(new InputModel(this, dsp.ctrlMod, "mod", 0.1f, 0, 0.2f));
		add(new InputModel(this, dsp.ctrlFB, "fb", 0.1f, 0, 1f));
		add(new InputModel(this, dsp.ctrlSpd, "spd", 10, 0, 200f));

		add(new OutputModel(this, dsp.outL, "OUT-L"));
		add(new OutputModel(this, dsp.outR, "OUT-R"));
	}

	public StereoEnvelopeDelayModel(Context c) {
		this(new EnvelopeDelayStereo(c));
	}
}
