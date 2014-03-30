package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.fx.Phaser;

public class PhaserModel extends DspBlockModel<Phaser> {

	public PhaserModel(Phaser amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.pan, "mix", -1, -1, 1));
		add(new InputModel(this, amp.lfo.amplitudeControl, "widt", 0.0015f, 0, 0.05f));
		add(new InputModel(this, amp.lfo.frequencyControl, "freq", 0.5f, 0, 10));
		add(new InputModel(this, amp.lfo.offsetControl, "dly", 0.003f, 0, 0.05f));
		add(new InputModel(this, amp.allpass.feedbackControl, "fb", 0.9f, 0, 1));

		add(new OutputModel(this, amp, "OUT"));
	}

	public PhaserModel(Context c) {
		this(new Phaser(c));
	}

	@Override
	public String getName() {
		return "Phaser";
	}
}
