package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Butterworth24db;

public class Butterworth24dbModel extends DspBlockModel<Butterworth24db> {

	public Butterworth24dbModel(Butterworth24db amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.ctrlCutoff, "freq", 0.1f, 0.01f, 0.45f));
		add(new InputModel(this, amp.ctrlQ, "reso", 0, 0, 1));

		add(new OutputModel(this, amp, "OUT"));
	}

	public Butterworth24dbModel(Context c) {
		this(new Butterworth24db(c));
	}

	@Override
	public String getName() {
		return "Butterworth";
	}

}
