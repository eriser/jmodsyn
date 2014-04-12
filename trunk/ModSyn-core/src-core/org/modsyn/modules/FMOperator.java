/*
 * Created on Apr 26, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.Device;
import org.modsyn.DeviceControl;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;
import org.modsyn.SignalSource;
import org.modsyn.modules.ctrl.ADSREnvelope;

/**
 * @author edy
 * 
 *         To change the template for this generated type comment go to Window - Preferences - Java - Code Generation -
 *         Code and Comments
 */
public class FMOperator implements SignalInput, SignalSource, Device {

	public static final int CTRL_FREQ_MULTIPLIER = 0;
	public static final int CTRL_MODULATION_INDEX = 1;

	private float frequency = 440;
	private float freqScale;
	private float modulator;
	private float gain;
	private float[] wave;
	private float index;
	private float detuneFactor;
	private float phStep;

	private SignalInput connectedDevice;

	public final ADSREnvelope envelope;
	DeviceControl[] controls = new DeviceControl[2];
	Device[] subDevices = new Device[1];

	public final SignalInput frequencyControl = new FrequencyControl();
	public final SignalInput detuneControl = new DetuneControl();
	public final SignalInput modulationIndexControl = new ModulationIndexControl();
	public final SignalInput freqMultiplierControl = new SignalInput() {
		@Override
		public void set(float data) {
			setFreqScale(data);
		}
	};
	private float modIdx;
	private float basefreq;
	private String name;

	public FMOperator(Context context, float[] wave) {
		envelope = new ADSREnvelope(context);
		init(context, wave, "FM-Op");
	}

	public FMOperator(Context context, float[] wave, String name) {
		envelope = new ADSREnvelope(context);
		init(context, wave, name);
	}

	private void init(Context context, float[] wave, String string) {
		this.wave = wave;
		detuneFactor = 1;
		gain = 0;
		modIdx = 1;
		freqScale = 1;
		phStep = (float) wave.length / (float) context.getSampleRate();
		controls[0] = new DeviceControl("Freq. Multiplier", freqMultiplierControl, 1, 0, 16);
		controls[1] = new DeviceControl("Output (mod. index)", modulationIndexControl, 1, 0, 16);
		subDevices[0] = envelope;
		name = string;
		context.addSignalSource(this);
	}

	/**
	 * Write data for the modulator
	 * 
	 * @see org.modsyn.SignalInput#set(float[])
	 */
	@Override
	public void set(float data) {
		setModulatorInput(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioGenerator#updateAudio()
	 */
	@Override
	public void updateSignal() {
		connectedDevice.set(process());

	}

	public float process() {
		gain = envelope.process();
		float sample = wave[(int) index];
		float freq = frequency + frequency * modulator;

		// update index
		float step = (freq * phStep * detuneFactor);// / (float) Sys.sampleRate;
		index = (index + step + wave.length) % wave.length;

		return sample * gain * modIdx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioOutput#connectTo(org.modsyn.AudioInput)
	 */
	@Override
	public void connectTo(SignalInput input) {
		connectedDevice = input;
	}

	/**
	 * @param frequency
	 *            The frequency to set.
	 */
	public void setFrequency(float frequency) {
		this.basefreq = frequency;
		this.frequency = frequency * freqScale * detuneFactor;
	}

	public void setFreqScale(float scale) {
		this.freqScale = scale;
		setFrequency(basefreq);
	}

	public void setDetune(float scale) {
		this.detuneFactor = scale;
	}

	/**
	 * @param modulator
	 *            The modulator to set.
	 */
	public void setModulatorInput(float modulator) {
		this.modulator = modulator;
	}

	public void setModulationIndex(float modIdx) {
		this.modIdx = modIdx;
	}

	/**
	 * @param wave
	 *            The wave to set.
	 */
	public void setWave(float[] wave) {
		this.wave = wave;
	}

	class ModulationIndexControl implements SignalInput {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.modsyn.ControlInput#setControl(float)
		 */
		@Override
		public void set(float data) {
			setModulationIndex(data);
		}
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

	class FrequencyScaleControl implements SignalInput {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.modsyn.ControlInput#setControl(float)
		 */
		@Override
		public void set(float data) {
			setFreqScale(data);
		}
	}

	class DetuneControl implements SignalInput {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.modsyn.ControlInput#setControl(float)
		 */
		@Override
		public void set(float data) {
			setDetune(data);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DeviceControl[] getDeviceControls() {
		return controls;
	}

	@Override
	public SignalInput[] getSignalInputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SignalOutput[] getSignalOutputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Device[] getSubDevices() {
		return subDevices;
	}

}
