package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * Based on code posted on www.musicdsp.org by Ove Karlsen.<br/>
 * A fast 2-pole LP/BP/HP filter with hard resonance clipping.
 * 
 * @author Erik Duijs
 */
public class Filter2Pole extends DefaultSignalOutput implements SignalInsert {

	public static final int MODE_LPF = 0;
	public static final int MODE_BPF = 1;
	public static final int MODE_HPF = 2;

	/**
	 * Cut-off control. 0..1
	 */
	public final SignalInput ctrlCutoff = new SignalInput() {
		@Override
		public void set(float signal) {
			if (signal > 0 && signal < 1) {
				cutoffreq = signal;
			}
		}
	};

	/**
	 * Resonance. 0..5
	 */
	public final SignalInput ctrlReso = new SignalInput() {
		@Override
		public void set(float signal) {
			reso = signal;
		}
	};

	public final SignalInput ctrlMode = new SignalInput() {
		@Override
		public void set(float signal) {
			int i = Math.round(signal);
			switch (i) {
			case MODE_LPF:
				filterMode = modeLPF;
				break;
			case MODE_BPF:
				filterMode = modeBPF;
				break;
			case MODE_HPF:
				filterMode = modeHPF;
				break;
			default:
				filterMode = modeLPF;
				break;
			}
		}
	};

	private final SignalInput modeLPF = new SignalInput() {
		@Override
		public void set(float input) {
			connectedInput.set(pole2);
		}
	};

	private final SignalInput modeBPF = new SignalInput() {
		@Override
		public void set(float input) {
			connectedInput.set(pole2 - pole1);
		}
	};

	private final SignalInput modeHPF = new SignalInput() {
		@Override
		public void set(float input) {
			connectedInput.set(input - pole2);
		}
	};

	private float reso = 0;
	private float cutoffreq = 0.1f;
	private SignalInput filterMode = modeLPF;

	private float pole1, pole2;

	@Override
	public void set(float input) {
		float resonance = pole2 * reso;
		if (resonance > 1) {
			resonance = 1;
		}
		input -= resonance;

		pole1 += ((-pole1 + input) * cutoffreq);
		pole2 += ((-pole2 + pole1) * cutoffreq);

		filterMode.set(input);
	}
}
