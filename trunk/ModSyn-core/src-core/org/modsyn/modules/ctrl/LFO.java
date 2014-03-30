/*
 * Created on Apr 26, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.modules.ctrl;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;
import org.modsyn.SignalSource;

/**
 * @author edy
 * 
 *         Low Frequency Oscillator.
 */
public class LFO extends DefaultSignalOutput implements SignalOutput, SignalSource {

	public final SignalInput trigger = new SignalInput() {
		@Override
		public void set(float sample) {
			trigger(sample != 0);
		}
	};
	public final SignalInput amplitudeControl = new AmplitudeControl();
	public final SignalInput frequencyControl = new FrequencyControl();
	public final SignalInput offsetControl = new OffsetControl();

	// private final SignalInput[] connectedDevices;
	// private final int iControl;

	private float offset = 0;
	private float amplitude = 1;
	private float[] wave;
	private int wavelen;

	private float index = 0;
	private float step = 0;
	private final Context context;

	public LFO(Context context, float[] wave) {
		// connectedDevices = new SignalInput[8];
		// iControl = 0;
		this.context = context;
		setWave(wave);
		context.addSignalSource(this);
	}

	public float process() {
		float sample = wave[(int) index] * amplitude + offset;

		// update index
		index = (index + step) % wavelen;

		return sample;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Controller#updateControl()
	 */
	@Override
	public void updateSignal() {
		if (amplitude != 0) {
			float sample = wave[(int) index] * amplitude + offset;

			// update index
			index = (index + step) % wavelen;

			connectedInput.set(sample);
			// toControllers(sample);
		}

	}

	// @Override
	// public void connectTo(SignalInput input) {
	// connectedDevices[iControl++] = (input);
	// }

	/**
	 * @param amplitude
	 *            The amplitude to set.
	 */
	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;// / 32768f;
	}

	/**
	 * @param frequency
	 *            The frequency to set.
	 */
	public void setFrequency(float frequency) {
		step = (frequency * wavelen) / context.getSampleRate();
	}

	/**
	 * @param offset
	 *            The offset to set.
	 */
	public void setOffset(float offset) {
		this.offset = offset;
	}

	/**
	 * @param wave
	 *            The wave to set.
	 */
	public void setWave(float[] wave) {
		this.wave = wave;
		this.wavelen = wave.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Triggered#trigger(boolean)
	 */
	public void trigger(boolean b) {
		if (b)
			index = 0;
	}

	class FrequencyControl implements SignalInput {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.modsyn.ControlInput#setControl(float)
		 */
		@Override
		public void set(float data) {
			setFrequency(data);
		}
	}

	class AmplitudeControl implements SignalInput {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.modsyn.ControlInput#setControl(float)
		 */
		@Override
		public void set(float data) {
			setAmplitude(data);
		}
	}

	class OffsetControl implements SignalInput {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.modsyn.ControlInput#setControl(float)
		 */
		@Override
		public void set(float data) {
			setOffset(data);
		}
	}
}
