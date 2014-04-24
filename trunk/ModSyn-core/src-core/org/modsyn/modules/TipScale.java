package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalInsert;
import org.modsyn.SignalInsertValue;

/**
 * Useful for keyboard scaling (for example making a sound softer at higher frequencies).
 * 
 * @author Erik Duijs
 */
public class TipScale extends DefaultSignalOutput implements SignalInsert {

	/**
	 * The control signal, for example a frequency
	 */
	public final SignalInsertValue ctrl = new SignalInsertValue(440) {
		@Override
		public void set(float signal) {
			if (signal != 0) {
				super.set(signal);
			}
		}
	};

	/**
	 * The tipping point for the scaling.
	 */
	public final SignalInputValue center = new SignalInputValue(440);

	/**
	 * The scaling factor. 0 = no scaling (i.e. output<-input), 1 = output<-input*(center/ctrl).
	 */
	public final SignalInputValue scale = new SignalInputValue(0f);

	@Override
	public void set(float signal) {
		float f = 1 - (1 - (center.value / ctrl.value)) * scale.value;
		if (f < 0)
			f = 0;
		connectedInput.set(signal * f);
	}

	public static void main(String[] args) {
		final TipScale ts = new TipScale();
		ts.connectTo(new SignalInput() {
			@Override
			public void set(float signal) {
				System.out.println(ts.ctrl.value + " - " + signal);
			}
		});
		ts.scale.set(2);

		for (int i = 40; i < 10000; i += 100) {
			ts.ctrl.set(i);
			ts.set(1);
		}
	}
}
