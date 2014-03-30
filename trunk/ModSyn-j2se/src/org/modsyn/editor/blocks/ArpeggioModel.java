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
			add(new InputModel(this, dsp.channels[i].freq, "freq" + i, 0, 0, 20000));
			add(new InputModel(this, dsp.channels[i].velo, "velo" + i, 0, 0, 1));
			add(new InputModel(this, dsp.channels[i].trig, "trig" + i, 0, 0, 1, 0));

		}

		add(new OutputModel(this, dsp.freqOut, "freq"));
		add(new OutputModel(this, dsp.veloOut, "velo"));
		add(new OutputModel(this, dsp.trigOut, "trig"));
	}

	public ArpeggioModel(Context c, int channels) {
		this(new Arpeggio(c, channels));
	}
}
