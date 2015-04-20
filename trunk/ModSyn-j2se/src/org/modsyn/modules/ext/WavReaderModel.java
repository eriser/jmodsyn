package org.modsyn.modules.ext;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.OutputModel;

public class WavReaderModel extends DspBlockModel<FromFile> {

	public WavReaderModel(FromFile dsp) {
		super(dsp);

		add(new OutputModel(this, dsp.outL, "OUT-L"));
		add(new OutputModel(this, dsp.outR, "OUT-R"));
	}

	public WavReaderModel(Context c) {
		this(new FromFile(c));
	}

	@Override
	public String getName() {
		return "Wav IN";
	}
}
