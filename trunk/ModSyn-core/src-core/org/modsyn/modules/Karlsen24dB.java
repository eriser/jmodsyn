package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * Based on code posted on www.musicdsp.org by Ove Karlsen. However it seems
 * that this filter doesn't work as intended: It doesn't seem to be a LPF, but
 * only adding resonance to the input. Which is a usable effect, so I just kept
 * it that way.
 * 
 * @author Erik Duijs
 */
public class Karlsen24dB extends DefaultSignalOutput implements SignalInsert {

	/**
	 * 0.0-1.0
	 */
	public final SignalInput ctrlFreq = new SignalInput() {
		@Override
		public void set(float signal) {
			if (signal > 0 && signal < 1) {
				b_f = signal;
			}
		}
	};

	public final SignalInput ctrlReso = new SignalInput() {
		@Override
		public void set(float signal) {
			b_q = signal;
		}
	};

	// b_f = frequency 0..1
	// b_q = resonance 0..50
	private float b_f = 0.5f;
	private float b_q = 3f;

	private float b_fp;
	private float pole1;
	private float pole2;
	private float pole3;
	private float pole4;

	@Override
	public void set(float b_in) {
		float b_inSH = b_in;

		int b_oversample = 0;
		while (b_oversample < 2) { // 2x oversampling (@44.1khz)

			float prevfp = b_fp;

			if (prevfp > 1) {
				prevfp = 1;
			} // Q-limiter

			b_fp = (b_fp * 0.418f) + ((b_q * pole4) * 0.582f); // dynamic
																// feedback

			float intfp = (b_fp * 0.36f) + (prevfp * 0.64f); // feedback phase

			b_in = b_inSH - intfp; // inverted feedback

			pole1 = (b_in * b_f) + (pole1 * (1 - b_f)); // pole 1

			if (pole1 > 1) { // pole 1 clipping
				pole1 = 1;
			} else if (pole1 < -1) {
				pole1 = -1;
			}

			pole2 = (pole1 * b_f) + (pole2 * (1 - b_f)); // pole 2
			pole3 = (pole2 * b_f) + (pole3 * (1 - b_f)); // pole 3
			pole4 = (pole3 * b_f) + (pole4 * (1 - b_f)); // pole 4

			b_oversample++;
		}

		connectedInput.set(b_in);
	}

	@Override
	public void connectTo(SignalInput input) {
		connectedInput = input;
	}
}
