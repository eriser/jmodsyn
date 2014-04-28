package org.modsyn.modules.ext;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;

public class WavWriterModel extends DspBlockModel<ToFile> {

	public WavWriterModel(ToFile dsp) {
		super(dsp);

		add(new InputModel(this, dsp.inL, "IN-L", 0, -1, 1));
		add(new InputModel(this, dsp.inR, "IN-R", 0, -1, 1));
		add(new OutputModel(this, dsp.outL, "OUT-L"));
		add(new OutputModel(this, dsp.outR, "OUT-R"));
	}

	public WavWriterModel(Context c) {
		this(new ToFile(c));
	}

	@Override
	public String getName() {
		return "Wav OUT";
	}
}
