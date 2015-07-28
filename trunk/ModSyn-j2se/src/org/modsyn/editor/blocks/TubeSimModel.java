package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.TubeSim;

public class TubeSimModel extends DspBlockModel<TubeSim> {

	public TubeSimModel(TubeSim amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.ctrlGain, "lvl", 1, 0, 20));
		add(new InputModel(this, amp.ctrlThresholdTop, "thrs+", .75f, 0, 1));
		add(new InputModel(this, amp.ctrlThresholdBottom, "thrs-", 0.6f, 0, 1));
		add(new InputModel(this, amp.ctrlThresholdDCM, "thrsDCM", 0.7f, 0, 1));
		add(new InputModel(this, amp.ctrlDCM, "DCM", 1, 0, 5));
		add(new InputModel(this, amp.ctrlAttack, "DCM-at", 10, 0, 200));
		add(new InputModel(this, amp.ctrlRelease, "DCM-rl", 100, 0, 200));

		add(new OutputModel(this, amp, "OUT"));
	}

	public TubeSimModel(Context c) {
		this(new TubeSim(c));
	}
}
