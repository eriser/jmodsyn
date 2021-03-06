/*
 * Created on Apr 23, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.modsyn.modules;

import static java.lang.Math.round;

import org.modsyn.Context;
import org.modsyn.DspObject;
import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;
import org.modsyn.WaveChangeListener;
import org.modsyn.WaveChangeObservable;
import org.modsyn.util.WaveTables;

/**
 * A simple oscillator class using pre-calculated waveforms. There is no
 * band-limiting here, so expect heavy aliasing at higher frequencies with rich
 * waveforms.
 * 
 * @author Erik Duijs
 */
public class Oscillator implements SignalSource, DspObject, WaveChangeObservable {

	private float[] wave;
	private float frequency;
	private float index;

	private float pwm = 50;

	private WaveChangeListener wcl;

	public final SignalInput ctrFreq = new SignalInput() {

		@Override
		public void set(float data) {
			setFrequency(data);
		}
	};
	public final SignalInput ctrPWM = new SignalInput() {
		@Override
		public void set(float data) {
			setPWM(data);
		}
	};
	public final SignalInput ctrDetune = new SignalInput() {
		@Override
		public void set(float data) {
			setDetune(data);
		}
	};
	public final SignalInput ctrShape = new SignalInput() {
		@Override
		public void set(float signal) {
			setShape(WaveTables.getWaveForm(round(signal)));
		}
	};

	private final Runnable noPWM = new Runnable() {
		@Override
		public void run() {
			buffer = wave[(int) index];

			index = (index + step) % wave.length;
			input.set(buffer);
		}
	};
	private final Runnable withPWM = new Runnable() {
		@Override
		public void run() {
			float phase = (index / wave.length) * 100;
			if (phase > pwm) {
				phase = 50 + ((phase - pwm) % 50);
			}
			phase = (phase / 100f) * wave.length;

			buffer = wave[(int) phase & (wave.length - 1)];

			index = (index + step) % wave.length;
			input.set(buffer);
		}
	};
	private Runnable updater = noPWM;

	private SignalInput input = NullInput.INSTANCE;
	private float step = 0;

	private float buffer;
	private float detuneFactor;
	private final Context context;

	public Oscillator(Context context) {
		this(context, WaveTables.SINUS);
	}

	public Oscillator(Context context, float[] waveTable) {
		this.context = context;
		setShape(waveTable);
		index = 0;
		detuneFactor = 1;
		context.addSignalSource(this);
	}

	public void setFrequency(float freq) {
		frequency = freq;
		step = ((frequency * detuneFactor) * wave.length) / context.getSampleRate();
	}

	public void setPWM(float pwm) {
		this.pwm = pwm % 100;
		if (pwm == 50) {
			updater = noPWM;
		} else {
			updater = withPWM;
		}
	}

	public void setShape(float[] waveTable) {
		if (wcl != null && this.wave != waveTable) {
			wcl.waveChanged(waveTable);
		}
		this.wave = waveTable;
	}

	/* (non-Javadoc)
	 * @see org.modsyn.modules.WaveChangeListenable#setWaveChangeListener(org.modsyn.util.WaveChangeListener)
	 */
	@Override
	public void setWaveChangeListener(WaveChangeListener wcl) {
		this.wcl = wcl;
		wcl.waveChanged(wave);
	}

	public void setDetune(float scale) {
		this.detuneFactor = 1 + scale;
		step = ((frequency * detuneFactor) * wave.length) / context.getSampleRate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SoundSource#updateSound()
	 */
	@Override
	public void updateSignal() {
		updater.run();
	}

	public float process() {
		float sample;

		// if (pwm50) {
		// sample = wave[(int) index];
		// } else {
		float phase = (index / wave.length) * 100;
		if (phase > pwm) {
			phase = 50 + ((phase - pwm) % 50);
		}
		phase = (phase / 100f) * wave.length;
		sample = wave[(int) phase & (wave.length - 1)];
		// }

		index = (index + step) % wave.length;
		return sample;
	}

	public float processNoPWM() {
		float sample = wave[(int) index];
		index = (index + step) % wave.length;
		return sample;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SoundOutput#connectTo(org.modsyn.SoundInput)
	 */
	@Override
	public void connectTo(SignalInput input) {
		this.input = input;
	}
}
