package org.modsyn.util;

import org.modsyn.DspObject;
import org.modsyn.KeyboardListener;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;

public class Keyboard2Adapter implements KeyboardListener, DspObject {

	SignalInput freqReceiver;
	SignalInput trigReceiver;
	SignalInput veloReceiver;

	public final SignalOutput freqOut = new SignalOutput() {
		@Override
		public void connectTo(SignalInput input) {
			freqReceiver = input;
		}
	};
	public final SignalOutput trigOut = new SignalOutput() {
		@Override
		public void connectTo(SignalInput input) {
			trigReceiver = input;
		}
	};
	public final SignalOutput veloOut = new SignalOutput() {
		@Override
		public void connectTo(SignalInput input) {
			veloReceiver = input;
		}
	};

	@Override
	public void keyOn(float freq, float velo) {
		if (freqReceiver != null) {
			freqReceiver.set(freq);
		}
		if (trigReceiver != null) {
			trigReceiver.set(1);
		}
		if (veloReceiver != null) {
			veloReceiver.set(velo);
		}
	}

	@Override
	public void keyOff() {
		if (trigReceiver != null) {
			trigReceiver.set(0);
		}
		if (veloReceiver != null) {
			veloReceiver.set(0);
		}
	}
}
