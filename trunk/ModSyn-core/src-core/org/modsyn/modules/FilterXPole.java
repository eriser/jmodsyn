package org.modsyn.modules;

import static java.lang.Math.round;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.FilterTypeChangeListener;
import org.modsyn.FilterTypeChangeObservable;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * Based on code posted on www.musicdsp.org by Ove Karlsen.<br/>
 * A fast 4-pole LP/BP/HP filter with resonance saturation.
 * 
 * @author Erik Duijs
 */
public class FilterXPole extends DefaultSignalOutput implements SignalInsert, FilterTypeChangeObservable {

	public static final int MODE_LPF = 0;
	public static final int MODE_BPF = 1;
	public static final int MODE_HPF = 2;

	public FilterXPole() {
		super();
	}

	public FilterXPole(int poles) {
		super();
		this.poles = poles;
	}

	public final SignalInput ctrlPoles = new SignalInput() {
		@Override
		public void set(float signal) {
			int i = round(signal);
			if (i >= 1 && i <= 16) {
				if (i < poles) {
					for (int j = i; j < poles; j++) {
						pole[j] = 0;
					}
				}
				poles = i;
			}
		}
	};

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
			connectedInput.set(pole[poles - 1]);
		}
	};

	private final SignalInput modeBPF = new SignalInput() {
		@Override
		public void set(float input) {
			connectedInput.set(pole[poles - 1] - pole[0]);
		}
	};

	private final SignalInput modeHPF = new SignalInput() {
		@Override
		public void set(float input) {
			connectedInput.set(input - pole[poles - 1]);
		}
	};

	private float reso = 0;
	private float cutoffreq = 0.1f;
	private SignalInput filterMode = modeLPF;

	private int poles = 1;
	private final float[] pole = new float[16];

	@Override
	public void set(float input) {
		float resonance = pole[poles - 1] * reso;
		if (resonance > 1) {
			resonance = 1;
		}
		input -= resonance;

		pole[0] += ((-pole[0] + input) * cutoffreq);
		for (int i = 1; i < poles; i++) {
			pole[i] += ((-pole[i] + pole[i - 1]) * cutoffreq);
		}

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
