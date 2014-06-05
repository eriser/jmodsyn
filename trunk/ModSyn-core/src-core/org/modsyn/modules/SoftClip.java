/*
 * Created on 26-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.modules;

import static java.lang.Math.abs;
import static java.lang.Math.tanh;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * Soft saturation towards abs(1).
 * 
 * @author Erik Duijs
 */
public class SoftClip extends DefaultSignalOutput implements SignalInsert {

	private float threshold = 0.75f;
	private float gain = 1;

	public SignalInput ctrlThreshold = new SignalInput() {
		@Override
		public void set(float signal) {
			threshold = signal;
		}
	};
	public SignalInput ctrlGain = new SignalInput() {
		@Override
		public void set(float signal) {
			gain = signal;
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SignalInput#write(float)
	 */
	@Override
	public void set(float in) {
		connectedInput.set(saturate(in * gain, threshold));
	}

	public final float saturate(float signal, float threshold) {
		if (abs(signal) < threshold) {
			return signal;
		} else {
			double t1 = 1.0 - threshold;
			if (signal > 0.f) {
				return (float) (threshold + t1 * tanh((signal - threshold) / t1));
			} else {
				return (float) -(threshold + t1 * tanh((-signal - threshold) / t1));
			}
		}
	}

	public static void main(String[] args) {
		SoftClip sc = new SoftClip();
		for (int i = 0; i < 500; i++) {
			float in = i / 100f;

			System.out.println(in + " - " + sc.saturate(in, 0.95f));

		}
	}
}
