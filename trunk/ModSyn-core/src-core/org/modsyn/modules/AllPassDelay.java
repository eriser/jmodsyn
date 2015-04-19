package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * An all-pass filter implementation, typically used for Reverb
 * 
 * <pre>
 *   >--->--------->---------->---->  -1 feed forward
 *   |     ______    _______       |
 * >-+-+->| Gain |->| Delay |--+---+---->
 *     |  |______|  |_______|  | 
 *     <---<-----<------<------< feedback
 * </pre>
 * 
 * @author Erik Duijs
 */
public class AllPassDelay implements SignalInsert {
	int delay;
	float gain;
	int pointer;
	float[] buffer;

	SignalInput connectedDevice;

	public AllPassDelay(Context context, float time, float gain) {
		this.delay = (int) (time * context.getSampleRate());
		this.gain = gain;
		this.pointer = 0;
		this.buffer = new float[delay];
	}

	public float process(float input) {
		float delayed = buffer[pointer];
		float feedfw = -input;
		float apfOut = delayed + feedfw;

		buffer[pointer] = (input + delayed) * gain;
		pointer = (pointer + 1) % delay;

		return apfOut;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SignalInput#write(float)
	 */
	@Override
	public void set(float input) {
		float delayed = buffer[pointer];
		float feedfw = -input;

		buffer[pointer] = (input + delayed) * gain;
		pointer = (pointer + 1) % delay;

		connectedDevice.set(delayed + feedfw);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SignalOutput#connectTo(org.modsyn.SignalInput)
	 */
	@Override
	public void connectTo(SignalInput input) {
		this.connectedDevice = input;
	}

}