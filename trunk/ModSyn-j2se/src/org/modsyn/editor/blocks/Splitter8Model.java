package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.MultiSplitter;

public class Splitter8Model extends DspBlockModel<MultiSplitter> {

	public Splitter8Model() {
		this(new MultiSplitter(8));
	}

	public Splitter8Model(MultiSplitter dsp) {
		super(dsp);
		add(new InputModel(this, dsp, "IN", 0, -1, 1));

		add(new OutputModel(this, dsp.outputs[0], "OUT-1"));
		add(new OutputModel(this, dsp.outputs[1], "OUT-2"));
		add(new OutputModel(this, dsp.outputs[2], "OUT-3"));
		add(new OutputModel(this, dsp.outputs[3], "OUT-4"));
		add(new OutputModel(this, dsp.outputs[4], "OUT-5"));
		add(new OutputModel(this, dsp.outputs[5], "OUT-6"));
		add(new OutputModel(this, dsp.outputs[6], "OUT-7"));
		add(new OutputModel(this, dsp.outputs[7], "OUT-8"));
	}

	@Override
	public String getName() {
		return "Split8";
	}

}
