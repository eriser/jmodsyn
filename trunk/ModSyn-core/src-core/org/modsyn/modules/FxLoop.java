package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

public class FxLoop implements SignalInsert {

	SignalInput connectedDevice;
	SignalInsert insert;
	Splitter split = new Splitter();
	Amplifier amp = new Amplifier();
	SignalMerge2 merge;

	public FxLoop(Context context, SignalInsert insert) {
		this.merge = new SignalMerge2(context);
		this.insert = insert;

		Wire wire = new Wire();
		split.target1 = amp;
		split.target2 = wire;
		amp.connectTo(insert);

		amp.control.set(0.5f);

		merge.setChannel(0, insert);
		merge.setChannel(1, wire);
	}

	@Override
	public void set(float sample) {
		split.set(sample);
	}

	public void setFxLevel(float level) {
		amp.control.set(level);
	}

	@Override
	public void connectTo(SignalInput input) {
		merge.connectTo(input);
	}
}
