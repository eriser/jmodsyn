package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.Splitter;

public class SplitterModel extends DspBlockModel<Splitter> {

	public SplitterModel() {
		this(new Splitter());
	}

	public SplitterModel(Splitter dsp) {
		super(dsp);
		add(new InputModel(this, dsp, "IN", 0, -1, 1));

		add(new OutputModel(this, dsp.out1, "OUT-1"));
		add(new OutputModel(this, dsp.out2, "OUT-2"));
	}

	@Override
	public String getName() {
		return "Split";
	}

}
