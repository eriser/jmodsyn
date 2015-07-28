package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Arpeggio;

public class ArpeggioModel extends DspBlockModel<Arpeggio> {

	public ArpeggioModel(Arpeggio dsp) {
		super(dsp, dsp.channels.length);

		add(new InputModel(this, dsp.ctrlLength, "length", 0, -1, 1));

		for (int i = 0; i < dsp.channels.length; i++) {
			add(new InputModel(this, dsp.channels[i].freq, "frq" + i, 0, 0, 20000));
			add(new InputModel(this, dsp.channels[i].velo, "vel" + i, 0, 0, 1));
			add(new InputModel(this, dsp.channels[i].trig, "trg" + i, 0, 0, 1, 0));

		}

		add(new OutputModel(this, dsp.freqOut, "frq"));
		add(new OutputModel(this, dsp.veloOut, "vel"));
		add(new OutputModel(this, dsp.trigOut, "trg"));
	}

	public ArpeggioModel(Context c, int channels) {
		this(new Arpeggio(c, channels));
	}
}
