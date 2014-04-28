package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.util.Debug;

/**
 * Delay with sub-sample accuracy and variable delay time. Use this for better precision and effects needing variable
 * delay time (chorus, flanger etc).
 * 
 * @author DU1381
 * 
 */
public class VarDelay implements SignalInsert {

	protected float delay;
	protected float feedback;
	protected int pointer;
	protected float[] buffer;

	protected SignalInput connectedDevice = NullInput.INSTANCE;

	/**
	 * This input controls the delay-time in seconds.
	 */
	public final SignalInput delayTimeControl = new ControlDelayTime();

	/**
	 * This input controls the delay feedback. 0.0f = no feedback, 1.0f is 100% feedback.
	 */
	public final SignalInput feedbackControl = new ControlFeedback();
	private final Context context;

	/**
	 * Constructor
	 * 
	 * @param maxDelayTimeSecs
	 *            defines the maximum delay time/buffer size
	 * @param delayTimeSecs
	 *            defines the delay time in seconds
	 * @param feedback
	 *            defines the feedback (0f=no feedback, 1f=100%, -0.5f=50% with reversed phase)
	 */
	public VarDelay(Context context, float maxDelayTimeSecs, float delayTimeSecs, float feedback) {
		if (delayTimeSecs > maxDelayTimeSecs) {
			throw new IllegalArgumentException("The delay time of " + delayTimeSecs + " is higher than the maximum of " + maxDelayTimeSecs + " seconds.");
		}
		this.delay = delayTimeSecs * context.getSampleRate();
		this.feedback = feedback;
		this.pointer = 0;
		this.buffer = new float[(int) (maxDelayTimeSecs * context.getSampleRate())];
		this.context = context;
	}

	/**
	 * Constructor. Allocates a buffer for max 1 second delay.
	 * 
	 * @param delayTimeSecs
	 * @param feedback
	 */
	public VarDelay(Context context, float delayTimeSecs, float feedback) {
		this(context, 1f, delayTimeSecs, feedback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SignalInput#write(float)
	 */
	@Override
	public void set(float sample) {
		float delayPointer = ((pointer - delay) + buffer.length) % buffer.length;
		float delayed = buffer[(int) (delayPointer)];

		buffer[pointer] = (sample + delayed * feedback);
		pointer = (pointer + 1) % buffer.length;

		connectedDevice.set(delayed);// / (1f + feedback));

		if (DEBUG) {
			Debug.checkSignal(delayed);
		}
	}

	/**
	 * Sets the delay time in seconds
	 * 
	 * @param secs
	 */
	public void setDelayTime(float secs) {
		this.delay = secs * context.getSampleRate();
	}

	/**
	 * Sets the feedback. Use a value between 0f...1f
	 * 
	 * @param feedback
	 */
	public void setFeedback(float feedback) {
		this.feedback = feedback;
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

	private class ControlDelayTime implements SignalInput {
		@Override
		public void set(float data) {
			setDelayTime(data);
		}
	}

	private class ControlFeedback implements SignalInput {
		@Override
		public void set(float data) {
			setFeedback(data);
		}
	}

}
