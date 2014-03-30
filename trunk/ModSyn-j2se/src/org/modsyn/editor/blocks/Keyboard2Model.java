package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.util.Keyboard2Adapter;

public class Keyboard2Model extends DspBlockModel<Keyboard2Adapter> {

	public Keyboard2Model(Keyboard2Adapter dsp) {
		super(dsp);
		add(new OutputModel(this, dsp.freqOut, "freq") {
			@Override
			public void connectTo(InputModel input) {
				super.connectTo(input);
				input.getInput().set(0);
			}
		});
		add(new OutputModel(this, dsp.veloOut, "lvl") {
			@Override
			public void connectTo(InputModel input) {
				super.connectTo(input);
				input.getInput().set(0);
			}
		});
		add(new OutputModel(this, dsp.trigOut, "trig") {
			@Override
			public void connectTo(InputModel input) {
				super.connectTo(input);
				input.getInput().set(0);
			}
		});
	}

	@Override
	public String getName() {
		return "Keyboard";
	}
}
