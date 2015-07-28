package org.modsyn.modules;

import static java.lang.Math.round;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.FilterTypeChangeListener;
import org.modsyn.FilterTypeChangeObservable;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * Based on code posted on www.musicdsp.org by Ove Karlsen.<br/>
 * A basic 8-pole LP/BP/HP filter with hard resonance clipping.
 * 
 * @author Erik Duijs
 */
public class Filter8Pole extends DefaultSignalOutput implements SignalInsert, FilterTypeChangeObservable {

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
			int i = round(signal);
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

			if (ftcl != null) {
				ftcl.filterTypeChanged(i);
			}
		}
	};

	private final SignalInput modeLPF = new SignalInput() {
		@Override
		public void set(float input) {
			connectedInput.set(pole8);
		}
	};

	private final SignalInput modeBPF = new SignalInput() {
		@Override
		public void set(float input) {
			connectedInput.set(pole8 - pole1);
		}
	};

	private final SignalInput modeHPF = new SignalInput() {
		@Override
		public void set(float input) {
			connectedInput.set(input - pole8);
		}
	};

	private float reso = 0;
	private float cutoffreq = 0.1f;
	private SignalInput filterMode = modeLPF;

	private float pole1, pole2, pole3, pole4, pole5, pole6, pole7, pole8;

	@Override
	public void set(float input) {
		float resonance = pole8 * reso;
		if (resonance > 1) {
			resonance = 1;
		}
		input -= resonance;

		pole1 += ((-pole1 + input) * cutoffreq);
		pole2 += ((-pole2 + pole1) * cutoffreq);
		pole3 += ((-pole3 + pole2) * cutoffreq);
		pole4 += ((-pole4 + pole3) * cutoffreq);
		pole5 += ((-pole5 + pole4) * cutoffreq);
		pole6 += ((-pole6 + pole5) * cutoffreq);
		pole7 += ((-pole7 + pole6) * cutoffreq);
		pole8 += ((-pole8 + pole7) * cutoffreq);

		filterMode.set(input);
	}

	private FilterTypeChangeListener ftcl;

	@Override
	public void setFilterTypeChangeListener(FilterTypeChangeListener ftcl) {
		this.ftcl = ftcl;
		ftcl.filterTypeChanged(getFilterType());
	}

	private int getFilterType() {
		if (filterMode == modeBPF) {
			return MODE_BPF;
		} else if (filterMode == modeHPF) {
			return MODE_HPF;
		} else {
			return MODE_LPF;
		}
	}
}
