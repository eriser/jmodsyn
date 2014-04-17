package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Speaker;

public class SpeakerModel extends DspBlockModel<Speaker> {

	public SpeakerModel(Speaker dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.ctrlWeight, "weight", 0.8f, 0, 1));
		add(new InputModel(this, dsp.ctrlSpring, "spring", 0.15f, 0, 1));
		add(new OutputModel(this, dsp, "OUT"));
	}

	public SpeakerModel() {
		this(new Speaker());
	}
}
