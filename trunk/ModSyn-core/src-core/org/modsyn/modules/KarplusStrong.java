/*
 * Created on 19-jul-07
 */
package org.modsyn.modules;

import static java.lang.Math.PI;

import java.util.Random;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.Device;
import org.modsyn.DeviceControl;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;
import org.modsyn.SignalSource;
import org.modsyn.util.WaveTables;

/**
 * @author DU1381
 * 
 *         Simulates plucked string sounds using the Karplus-Strong algorithm, which is a simple physical modelling
 *         algorithm. The ControlInput is the frequency control. The Triggered interface initiates the 'pluck', thus the
 *         start of the sound.
 */
public class KarplusStrong extends DefaultSignalOutput implements SignalSource, Device {

	public final SignalInput trigger = new SignalInput() {
		@Override
		public void set(float sample) {
			trigger(sample != 0);
		}
	};
	public final SignalInput frequencyControl = new SignalInput() {
		@Override
		public void set(float data) {
			setFrequency(data);
		}
	};

	public final SignalInput pluckControl = new SignalInput() {
		@Override
		public void set(float data) {
			setPluckTime(data);
		}
	};
	public final SignalInput pluckEnvControl = new SignalInput() {
		@Override
		public void set(float data) {
			setPluckEnvelopeOffs(data);
		}
	};

	public final SignalInput fbControl = new SignalInput() {
		@Override
		public void set(float data) {
			setFeedBack(data);
		}
	};

	public final SignalInput cutoffControl = new SignalInput() {
		@Override
		public void set(float data) {
			setCutOff(data);
		}
	};

	SignalOutput[] outputs = new SignalOutput[1];
	DeviceControl[] controls = new DeviceControl[4];

	// white noise for the 'pluck'
	Random rnd = new Random();
	boolean triggered;
	int pluckTime;
	float pluckOffs;
	int envelopePos;

	// comb filter
	float[] buffer;
	float delay;
	int pointer;
	float feedback;

	// low pass filter
	final float pi = (float) PI;
	final float pi2 = pi * 2f;
	float vibrapos = 0;
	float vibraspeed = 0;
	float resofreq;
	float resonance;
	float w;

	// output amplification/attenuation
	float outAmp;
	private final Context context;

	public KarplusStrong(Context context) {
		this.w = pi2 * resofreq / context.getSampleRate();
		this.envelopePos = Integer.MAX_VALUE;
		this.buffer = new float[context.getSampleRate()]; // 1 sec of buffer
		this.pointer = 0;
		this.context = context;
		setPluckTime(0.01f); // 10ms pluck
		setPluckEnvelopeOffs(1f);
		setResonance(1.0f);
		setOutputAmp(1f);
		setFeedBack(0.995f);
		setCutOff(6000f);
		setFrequency(440f);

		context.addSignalSource(this);

		outputs[0] = this;

		controls[0] = new DeviceControl("Pluck Time", pluckControl, 0.01f, 0, 0.1f);
		controls[1] = new DeviceControl("Pluck Envelope (up-down)", pluckEnvControl, 1f, 0, 1f);
		controls[2] = new DeviceControl("Feedback", fbControl, 0.995f, 0.95f, 1f);
		controls[3] = new DeviceControl("Filter Cut-off", cutoffControl, 6000f, 20, 20000);
	}

	public void setPluckTime(float secs) {
		pluckTime = (int) (context.getSampleRate() * secs);
	}

	public void setPluckEnvelopeOffs(float offs) {
		pluckOffs = offs;
	}

	public void setCutOff(float cutoff) {
		resofreq = cutoff;
		w = pi2 * resofreq / context.getSampleRate();
	}

	public void setFeedBack(float feedback) {
		this.feedback = feedback;
	}

	public void setOutputAmp(float amp) {
		this.outAmp = amp * 0.5f;
	}

	private void setDelayTime(float secs) {
		this.delay = secs * context.getSampleRate();
	}

	public void setFrequency(float freq) {
		if (freq != 0)
			setDelayTime(1f / freq);
	}

	/**
	 * @param reso
	 *            Resonance (1.0 or higher)
	 */
	public void setResonance(float reso) {
		resonance = reso;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.UpdatedSignal#updateSignal()
	 */
	@Override
	public void updateSignal() {

		// If triggered, generate a short burst of white noise.
		// This simulates the 'plucking' of a string.
		// After the 'pluck', the sound will resonate in the comb filter for the
		// amount of time which can be controlled by the comb filter's feedback.
		float sample;
		if (envelopePos < pluckTime) {
			sample = (rnd.nextFloat() * 2f) - 1f;
			sample *= (pluckOffs - (envelopePos / (float) pluckTime));
			envelopePos++;
		} else {
			sample = 0;
		}

		// Send to comb filter.
		// The comb filter is a high precision variable delay with interpolation
		// to ensure correct pitch of the resulting tone.
		double delayPointer = ((pointer - delay) + buffer.length) % buffer.length;

		int dlyPtr = (int) (delayPointer);
		double deltaPtr = delayPointer - dlyPtr;
		double delayed0 = buffer[dlyPtr];
		double delayed1 = buffer[(dlyPtr + 1) % buffer.length];
		double deltaDly = delayed1 - delayed0;
		double delayed = delayed0 + deltaPtr * deltaDly;

		// Comb filter feedback goes through LPF to simulate damping of the
		// strings; the lower the cut-off frequency, the more the sound is
		// damped.
		double fbVal = delayed * feedback;
		double q = 1.0f - w / (2.0f * (resonance + 0.5f / (1.0f + w)) + w - 2.0f);
		double r = q * q;
		double c = (r + 1.0f - 2.0f * WaveTables.cos(w) * q);
		vibraspeed += (fbVal - vibrapos) * c;
		vibrapos += vibraspeed;
		vibraspeed *= r;
		fbVal = vibrapos;
		//

		// update delay buffer with the input + feedback
		buffer[pointer] = (float) (sample + fbVal);
		pointer = (pointer + 1) % buffer.length;

		// send comb filter output to connectedDevice
		connectedInput.set((float) (delayed * outAmp));
	}

	public void trigger(boolean b) {
		triggered = b;
		if (b) {
			envelopePos = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getName()
	 */
	@Override
	public String getName() {
		return "Karplus-Strong Generator";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getSignalInputs()
	 */
	@Override
	public SignalInput[] getSignalInputs() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getSignalOutputs()
	 */
	@Override
	public SignalOutput[] getSignalOutputs() {
		return outputs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getParameterControls()
	 */
	@Override
	public DeviceControl[] getDeviceControls() {
		return controls;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getSubDevices()
	 */
	@Override
	public Device[] getSubDevices() {
		return null;
	}

}