package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.ext.MidiSupport;
import org.modsyn.modules.ext.MidiVoicePolyAdapter;

public class FromMidiPolyModel extends DspBlockModel<MidiVoicePolyAdapter> {

	public FromMidiPolyModel(MidiVoicePolyAdapter dsp) {
		super(dsp, dsp.voices.size());
		MidiSupport.INSTANCE.setListener(1, dsp);
		add(new InputModel(this, dsp.cBendRange, "pb-r", 2, -12, 12, 0));
		add(new InputModel(this, dsp.ctrl1nr, "c-nr", 16, 0, 127, 0));

		int voices = dsp.voices.size();
		for (int i = 0; i < voices; i++) {
			String nr = voices == 1 ? "" : Integer.toString(i);
			add(new OutputModel(this, dsp.voices.get(i).keyFreqOut, "frq" + nr));
			add(new OutputModel(this, dsp.voices.get(i).keyVeloOut, "vel" + nr));
			add(new OutputModel(this, dsp.voices.get(i).keyTrigOut, "trg" + nr));
		}
		add(new OutputModel(this, dsp.voices.get(0).bendOut, "pb"));
		add(new OutputModel(this, dsp.voices.get(0).modOut, "mod"));
		add(new OutputModel(this, dsp.voices.get(0).ctrl1Out, "c-out"));

	}

	@Override
	public String getName() {
		return "MIDI IN";
	}
}
