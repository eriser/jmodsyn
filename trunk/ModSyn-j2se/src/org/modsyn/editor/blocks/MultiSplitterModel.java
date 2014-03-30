package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.MultiSplitter;

public class MultiSplitterModel extends DspBlockModel<MultiSplitter> {

	public MultiSplitterModel(int channels) {
		this(new MultiSplitter(channels));
	}

	public MultiSplitterModel(MultiSplitter dsp) {
		super(dsp, dsp.outputs.length);
		add(new InputModel(this, dsp, "IN", 0, -1, 1));

		for (int i = 0; i < dsp.outputs.length; i++) {
			add(new OutputModel(this, dsp.outputs[i], "OUT-" + (i + 1)));
		}
	}

	@Override
	public String getName() {
		return "Split";
	}

}
