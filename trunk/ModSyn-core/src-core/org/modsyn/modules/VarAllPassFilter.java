/*
 * Created on 24-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * @author DU1381
 * 
 *         All-Pass Filter with variable delay time.
 */
public class VarAllPassFilter implements SignalInsert {
	float delay;
	float feedback;
	int pointer;
	float[] buffer;

	SignalInput connectedDevice = NullInput.INSTANCE;

	public final SignalInput delayTimeControl = new SignalInput() {
		@Override
		public void set(float delayTimeSecs) {
			setDelayTime(delayTimeSecs);
		}
	};

	public final SignalInput feedbackControl = new SignalInput() {
		@Override
		public void set(float data) {
			setFeedback(data);
		}
	};
	private final Context context;

	public VarAllPassFilter(Context context, float maxDelayTimeSecs, float delayTimeSecs, float feedback) {
		if (delayTimeSecs > maxDelayTimeSecs) {
			throw new IllegalArgumentException("The delay time of " + delayTimeSecs + " is higher than the maximum of " + maxDelayTimeSecs + " seconds.");
		}
		this.context = context;
		this.delay = delayTimeSecs * context.getSampleRate();
		this.feedback = feedback;
		this.pointer = 0;
		this.buffer = new float[(int) (maxDelayTimeSecs * context.getSampleRate())];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SignalInput#write(float)
	 */
	@Override
	public void set(float input) {
		float delayPointer = ((pointer - delay) + buffer.length) % buffer.length;

		float feedfw = input * (-feedback);
		float delayed = buffer[(int) delayPointer];
		float output = delayed + feedfw;
		float feedbk = output * (feedback);

		buffer[pointer] = input + feedbk;
		pointer = (pointer + 1) % buffer.length;

		connectedDevice.set(output);
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

	public void setDelayTime(float delayTimeSecs) {
		this.delay = delayTimeSecs * context.getSampleRate();
	}

	/**
	 * @param f
	 */
	public void setFeedback(float fb) {
		this.feedback = fb;

	}

}