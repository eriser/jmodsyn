package org.modsyn.modules;

import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

public class DryWet implements SignalInsert {

	private final PanPot pan;
	private final Adder add;

	public SignalInput ctrl = new SignalInput() {
		@Override
		public void set(float signal) {
			pan.setPanning((signal * 2f) - 1f);
		}
	};

	public DryWet(SignalInsert insert) {
		this.pan = new PanPot();
		this.add = new Adder();
		pan.outputL.connectTo(add);
		pan.outputR.connectTo(insert);
		insert.connectTo(add.control);
	}

	@Override
	public void set(float signal) {
		pan.set(signal);
	}

	@Override
	public void connectTo(SignalInput input) {
		add.connectTo(input);
	}
}
