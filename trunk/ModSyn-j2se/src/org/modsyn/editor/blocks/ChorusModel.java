package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.fx.Chorus;

public class ChorusModel extends DspBlockModel<Chorus> {

	public ChorusModel(Chorus amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.mix.panControl, "mix", 0, -1, +1));
		add(new InputModel(this, amp.wdt, "wdt", 0.0015f, 0, 0.05f));
		add(new InputModel(this, amp.frq, "frq", 1, 0, 10));
		add(new InputModel(this, amp.fbk, "fb", 0.1f, 0, 1));
		add(new InputModel(this, amp.dly, "dly", 0.04f, 0, 0.05f));

		add(new OutputModel(this, amp, "OUT"));
	}

	public ChorusModel(Context c) {
		this(new Chorus(c));
	}

	@Override
	public String getName() {
		return "Chorus";
	}
}
