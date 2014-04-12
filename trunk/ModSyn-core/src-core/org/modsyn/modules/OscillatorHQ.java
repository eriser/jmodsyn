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
	private boolean pwm50 = false;

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
			cutoffFactor = signal;
		}
	};
	public final SignalInputIntValue ctrlOversampling = new SignalInputIntValue(4);

	private SignalInput input = NullInput.INSTANCE;
	private float step = 0;

	private float detuneFactor;
	private final Context context;

	public OscillatorHQ(Context context) {
		this(context, WaveTables.SINUS);
	}

	public OscillatorHQ(Context context, float[] waveTable) {
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
		this.pwm50 = false;
	}

	public void setShape(float[] waveTable) {
		this.wave = waveTable;
	}

	public void setDetune(float scale) {
		this.detuneFactor = 1 + scale;
		step = ((frequency * detuneFactor) * wave.length) / context.getSampleRate();
	}

	private float cutoffFactor = 1f;
	private float pole1, pole2, pole3, pole4, pole5, pole6, pole7, pole8;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SoundSource#updateSound()
	 */
	@Override
	public void updateSignal() {

		float buffer = 0;

		float step = this.step / ctrlOversampling.value;
		float cutoffreq = cutoffFactor;// / ctrlOversampling.value;

		for (int i = 0; i < ctrlOversampling.value; i++) {
			float sample;

			if (pwm50) {
				sample = wave[(int) index];
			} else {
				float phase = (index / wave.length) * 100;
				if (phase > pwm) {
					phase = 50 + ((phase - pwm) % 50);
				}
				phase = (phase / 100f) * wave.length;
				sample = wave[(int) phase & (wave.length - 1)];
			}

			{ // anti-alias filter
				sample = filter(cutoffreq, sample);
			}

			index = (index + step) % wave.length;

			buffer += sample;
		}

		input.set(buffer / ctrlOversampling.value);
	}

	private float filter(float cutoffreq, float sample) {
		pole1 += ((-pole1 + sample) * cutoffreq);
		pole2 += ((-pole2 + pole1) * cutoffreq);
		pole3 += ((-pole3 + pole2) * cutoffreq);
		pole4 += ((-pole4 + pole3) * cutoffreq);
		pole5 += ((-pole5 + pole4) * cutoffreq);
		pole6 += ((-pole6 + pole5) * cutoffreq);
		pole7 += ((-pole7 + pole6) * cutoffreq);
		pole8 += ((-pole8 + pole7) * cutoffreq);

		sample = pole8;
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
