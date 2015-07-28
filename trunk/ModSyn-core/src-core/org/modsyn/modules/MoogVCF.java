package org.modsyn.modules;

import static java.lang.Math.PI;
import static java.lang.Math.round;
import static java.lang.Math.sin;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.FilterTypeChangeListener;
import org.modsyn.FilterTypeChangeObservable;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * Based on code posted here:
 * http://www.musicdsp.org/showArchiveComment.php?ArchiveID=24
 * 
 * @author Erik Duijs
 */
public class MoogVCF extends DefaultSignalOutput implements SignalInsert, FilterTypeChangeObservable {
	public static final int MODE_LPF = 0;
	public static final int MODE_BPF = 1;
	public static final int MODE_HPF = 2;

	public final SignalInput ctrlCutOff = new SignalInput() {
		@Override
		public void set(float signal) {
			setCutOff(signal);
		}
	};

	public final SignalInput ctrlResonance = new SignalInput() {
		@Override
		public void set(float signal) {
			setResonance(signal);
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
			connectedInput.set((float) y4);
		}
	};

	private final SignalInput modeBPF = new SignalInput() {
		@Override
		public void set(float input) {
			connectedInput.set((float) (y4 - y1));
		}
	};

	private final SignalInput modeHPF = new SignalInput() {
		@Override
		public void set(float input) {
			connectedInput.set((float) (input - y4));
		}
	};

	private SignalInput filterMode = modeLPF;

	// cutoff and resonance [0 - 1]
	private double cutoff;
	private double resonance;
	private double y1, y2, y3, y4;
	private double oldx;
	private double oldy1, oldy2, oldy3;
	private double x;
	private double r;
	private double p;
	private double k;
	private double sampleRate;

	public MoogVCF(Context c) {
		super();
		setSampleRate(c.getSampleRate());
		setCutOff(1);
		setResonance(0);
		y1 = y2 = y3 = y4 = oldx = oldy1 = oldy2 = oldy3 = 0;
		calc();
	}

	public float getSampleRate() {
		return (float) sampleRate;
	}

	public void setSampleRate(float sr) {
		sampleRate = sr;
		calc();
	}

	public float getResonance() {
		return (float) resonance;
	}

	public void setResonance(float reso) {
		resonance = reso;
		calc();
	}

	public float getCutOff() {
		return (float) cutoff;
	}

	public float getCutOffHz() {
		return (float) (cutoff * sampleRate * 0.5);
	}

	public void setCutOff(float cutoff) {
		this.cutoff = cutoff;
		calc();
	}

	private void calc() {
		// empirical tuning
		p = cutoff * (1.8 - 0.8 * cutoff);
		// k = p + p - 1.0;
		// A much better tuning seems to be:
		k = (2.0 * sin(cutoff * PI * 0.5) - 1.0);

		double t1 = (1.0 - p) * 1.386249f;
		double t2 = 12.0 + t1 * t1;
		r = resonance * (t2 + 6.0 * t1) / (t2 - 6.0 * t1);
	}

	@Override
	public void set(float input) {
		calc(); // not sure if needed here

		// process input
		x = input - r * y4;

		// four cascaded one-pole filters (bilinear transform)
		y1 = x * p + oldx * p - k * y1;
		y2 = y1 * p + oldy1 * p - k * y2;
		y3 = y2 * p + oldy2 * p - k * y3;
		y4 = y3 * p + oldy3 * p - k * y4;

		// clipper band limited sigmoid
		y4 -= (y4 * y4 * y4) / 6.0;

		oldx = x;
		oldy1 = y1;
		oldy2 = y2;
		oldy3 = y3;

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
