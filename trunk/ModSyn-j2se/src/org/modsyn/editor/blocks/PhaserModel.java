package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.fx.Phaser;

public class PhaserModel extends DspBlockModel<Phaser> {

	public PhaserModel(Phaser dsp) {
		super(dsp);

		add(new InputModel(this, dsp, "IN", 0, -1, 1));
		add(new InputModel(this, dsp.ctrlInput, "ctrlIN", 0, -1, 1));
		add(new InputModel(this, dsp.ctrlStages, "stages", 6, 1, 32, 0));
		add(new InputModel(this, dsp.ctrlAmount, "depth", 1, 0, 1));
		add(new InputModel(this, dsp.ctrlFeedBack, "fb", 0.5f, 0, 0.99f));
		add(new InputModel(this, dsp.ctrlFloorHz, "floorHz", 440, 20, 5000));
		add(new InputModel(this, dsp.ctrlCeilingHz, "ceilHz", 1600, 20, 5000));
		add(new InputModel(this, dsp.ctrlLfoFreq, "lfoFreq", 0.5f, 0, 5));
		add(new InputModel(this, dsp.ctrlLfoDepth, "lfoDep", 1, 0, 1));

		add(new OutputModel(this, dsp, "OUT"));
	}

	public PhaserModel(Context c) {
		this(new Phaser(c));
	}
}
