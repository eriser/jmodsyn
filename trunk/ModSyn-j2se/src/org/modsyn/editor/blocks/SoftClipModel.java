package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.SoftClip;

public class SoftClipModel extends DspBlockModel<SoftClip> {

	public SoftClipModel(SoftClip amp) {
		super(amp);

		add(new InputModel(this, amp, "IN", 0, -1, 1));
		add(new InputModel(this, amp.ctrlGain, "lvl", 1, 0, 20));
		add(new InputModel(this, amp.ctrlThreshold, "thr", .75f, 0, 1));

		add(new OutputModel(this, amp, "OUT"));
	}

	public SoftClipModel() {
		this(new SoftClip());
	}

	@Override
	public String getName() {
		return "SoftClip";
	}
}
