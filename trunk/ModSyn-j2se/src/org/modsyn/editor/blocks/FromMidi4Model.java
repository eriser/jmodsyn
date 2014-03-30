package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.ext.MidiSupport;
import org.modsyn.modules.ext.MidiVoicePolyAdapter;

public class FromMidi4Model extends DspBlockModel<MidiVoicePolyAdapter> {

	public FromMidi4Model(MidiVoicePolyAdapter dsp) {
		super(dsp);
		MidiSupport.INSTANCE.setListener(1, dsp);
		add(new InputModel(this, dsp.cBendRange, "pb-r", 2, -12, 12, 0));
		add(new InputModel(this, dsp.ctrl1nr, "c-nr", 16, 0, 127, 0));

		int voices = dsp.voices.size();
		for (int i = 0; i < voices; i++) {
			String nr = voices == 1 ? "" : Integer.toString(i);
			add(new OutputModel(this, dsp.voices.get(i).keyFreqOut, "freq" + nr));
			add(new OutputModel(this, dsp.voices.get(i).keyVeloOut, "velo" + nr));
			add(new OutputModel(this, dsp.voices.get(i).keyTrigOut, "trig" + nr));
			add(new OutputModel(this, dsp.voices.get(i).bendOut, "bend" + nr));
			add(new OutputModel(this, dsp.voices.get(i).modOut, "mod" + nr));
			// add(new OutputModel(this, dsp.voices.get(i).ctrl1Out, "c-out"));
		}

	}

	@Override
	public String getName() {
		return "MIDI IN";
	}
}
