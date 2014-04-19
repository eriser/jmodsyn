/*
 * Created on Apr 23, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInputIntValue;
import org.modsyn.SignalSource;
import org.modsyn.util.WaveTables;

/**
 * A variation of the Oscillator class that uses over-sampling to reduce aliasing.
 * 
 * @author Erik Duijs
 */
public class OscillatorHQ implements SignalSource {

	private float[] wave;
	private float frequency;
	private float index;

	private float pwm = 50;

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
			setShape(WaveTables.getWaveForm(Math.round(signal)));
		}
	};
	public final SignalInput ctrFilter = new SignalInput() {
		@Override
		public void set(float signal) {
			filter.set(signal * context.getSampleRate(), 0);
		}
	};
	public final SignalInputIntValue ctrlOversampling = new SignalInputIntValue(4) {
		@Override
		public void set(float signal) {
			super.set(signal);

			filter.setSampleRate(context.getSampleRate() * this.value);
			// bw.set(CUTOFF, 0);
		}
	};

	private SignalInput input = NullInput.INSTANCE;
	private float step = 0;
	private float ostep = 0;

	private float detuneFactor;
	private final Context context;
	private final Butterworth24db filter;

	public OscillatorHQ(Context context) {
		this(context, WaveTables.SINUS);
	}

	public OscillatorHQ(Context context, float[] waveTable) {
		this.context = context;
		setShape(waveTable);
		index = 0;
		detuneFactor = 1;
		context.addSignalSource(this);
		this.filter = new Butterworth24db(context);
		filter.set(12000, 0);
	}

	public void setFrequency(float freq) {
		frequency = freq;
		step = ((frequency * detuneFactor) * wave.length) / context.getSampleRate();
		ostep = step / ctrlOversampling.value;
	}

	public void setPWM(float pwm) {
		this.pwm = pwm % 100;
		updater = pwm == 50 ? updateNoPWM : updatePWM;
	}

	public void setShape(float[] waveTable) {
		this.wave = waveTable;
	}

	public void setDetune(float scale) {
		this.detuneFactor = 1 + scale;
		step = ((frequency * detuneFactor) * wave.length) / context.getSampleRate();
		ostep = step / ctrlOversampling.value;
	}

	private final Runnable updateNoPWM = new Runnable() {

		@Override
		public void run() {

			float buffer = 0;

			for (int i = ctrlOversampling.value; i > 0; i--) {
				buffer += filter.process(wave[(int) index]);
				index = (index + ostep) % wave.length;
			}

			input.set(buffer / ctrlOversampling.value);
		}
	};

	private final Runnable updatePWM = new Runnable() {

		@Override
		public void run() {

			float buffer = 0;

			for (int i = 0; i < ctrlOversampling.value; i++) {

				float phase = (index / wave.length) * 100;
				if (phase > pwm) {
					phase = 50 + ((phase - pwm) % 50);
				}
				phase = (phase / 100f) * wave.length;

				index = (index + ostep) % wave.length;

				buffer += filter.process(wave[(int) phase & (wave.length - 1)]);
			}

			input.set(buffer / ctrlOversampling.value);
		}
	};

	private Runnable updater = updateNoPWM;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SoundSource#updateSound()
	 */
	@Override
	public void updateSignal() {
		updater.run();
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
