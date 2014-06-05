package org.modsyn.modules.ext;

import static java.lang.Math.round;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.DspObject;
import org.modsyn.SignalInput;
import org.modsyn.util.Tools;

public class MidiVoiceAdapter implements MidiListener, DspObject {
	public final DefaultSignalOutput keyFreqOut = new DefaultSignalOutput(0);
	public final DefaultSignalOutput keyVeloOut = new DefaultSignalOutput(0);
	public final DefaultSignalOutput keyTrigOut = new DefaultSignalOutput(0);
	public final DefaultSignalOutput bendOut = new DefaultSignalOutput(0);
	public final DefaultSignalOutput modOut = new DefaultSignalOutput(0);
	public final DefaultSignalOutput ctrl1Out = new DefaultSignalOutput(0);

	private int c1nr = 16;

	int bendRange = 2;
	int key = 0;
	float bend = 0;

	public final SignalInput cBendRange = new SignalInput() {
		@Override
		public void set(float signal) {
			bendRange = round(signal);
		}
	};

	public final SignalInput ctrl1nr = new SignalInput() {
		@Override
		public void set(float signal) {
			c1nr = round(signal);
		}
	};

	private float calcFreq() {
		float keyFreq = Tools.getFreq(key);

		if (bend == 0) {
			return keyFreq;
		} else {
			if (bend > 0) {
				float bendUpFreq = Tools.getFreq(key + bendRange);
				return keyFreq + (bendUpFreq - keyFreq) * bend;
			} else {
				float bendDownFeq = Tools.getFreq(key - bendRange);
				return keyFreq + (keyFreq - bendDownFeq) * bend;
			}
		}
	}

	@Override
	public void keyOn(int key, int velo) {
		this.key = key;
		keyFreqOut.connectedInput.set(calcFreq());
		keyVeloOut.connectedInput.set(velo / 127f);
		keyTrigOut.connectedInput.set(velo == 0 ? 0 : 1);
	}

	@Override
	public void keyOff(int key, int velo) {
		if (key == this.key) {
			this.key = key;
			keyFreqOut.connectedInput.set(calcFreq());
			keyVeloOut.connectedInput.set(0);
			keyTrigOut.connectedInput.set(0);
		}
	}

	@Override
	public void controlChange(int control, int value) {
		if (control == 1) {
			modOut.connectedInput.set(value / 127f);
			// System.out.println(value / 127f);
		} else if (control == c1nr) {
			ctrl1Out.connectedInput.set(value / 127f);
		}
	}

	@Override
	public void pitchBend(int val) {
		this.bend = val / (float) 0x2000;
		keyFreqOut.connectedInput.set(calcFreq());
		bendOut.connectedInput.set(bend);
	}
}
