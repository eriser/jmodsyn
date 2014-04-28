package org.modsyn.modules.fx;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;

/**
 * Stereo envelope delay
 * 
 * @author Erik Duijs
 */
public class EnvelopeDelayStereo implements SignalInput {

	private float amp;
	private final EnvelopeDelay edL, edR;

	public final SignalOutput outL = new SignalOutput() {
		@Override
		public void connectTo(SignalInput input) {
			edL.connectTo(input);
		}
	};
	public final SignalOutput outR = new SignalOutput() {
		@Override
		public void connectTo(SignalInput input) {
			edR.connectTo(input);
		}
	};
	public final SignalInput inL = new SignalInput() {

		@Override
		public void set(float signal) {
			edL.set(signal);
		}
	};
	public final SignalInput inR = new SignalInput() {

		@Override
		public void set(float signal) {
			edR.set(signal);
		}
	};
	public final SignalInput ctrlMix = new SignalInput() {

		@Override
		public void set(float signal) {
			edL.ctrlMix.set(signal);
			edR.ctrlMix.set(signal);
		}
	};

	public final SignalInput ctrlMod = new SignalInput() {
		@Override
		public void set(float signal) {
			edL.ctrlMod.set(signal);
			edR.ctrlMod.set(-signal);
		}
	};
	public final SignalInput ctrlDly = new SignalInput() {
		@Override
		public void set(float signal) {
			edL.ctrlDly.set(signal);
			edR.ctrlDly.set(signal);
		}
	};
	public final SignalInput ctrlFB = new SignalInput() {
		@Override
		public void set(float signal) {
			edL.ctrlFB.set(signal);
			edR.ctrlFB.set(signal);
		}
	};
	public final SignalInput ctrlSpd = new SignalInput() {

		@Override
		public void set(float signal) {
			edL.ctrlSpd.set(signal);
			edR.ctrlSpd.set(signal);
		}
	};

	public EnvelopeDelayStereo(Context context) {
		this.edL = new EnvelopeDelay(context);
		this.edR = new EnvelopeDelay(context);
		ctrlMod.set(0.001f);
		ctrlSpd.set(200);
		ctrlDly.set(0.002f);
	}

	@Override
	public void set(float signal) {
		edL.set(signal);
		edR.set(signal);
	}
}
