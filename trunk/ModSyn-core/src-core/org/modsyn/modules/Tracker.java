/*
 * Created on Apr 22, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.DspObject;
import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * @author Erik Duijs
 * 
 *         Envelope follower.
 */
public class Tracker implements SignalInsert, DspObject {

	public final SignalInput ctrlSpeed = new SignalInput() {
		@Override
		public void set(float value) {
			setSpeed(value);
		}
	};
	public final SignalInput ctrlAmp = new SignalInput() {
		@Override
		public void set(float value) {
			amp = value;
		}
	};
	public final SignalInput ctrlBias = new SignalInput() {
		@Override
		public void set(float value) {
			bias = value;
		}
	};

	SignalInput connectedDevice = NullInput.INSTANCE;

	int size = 1000;

	final float[] buffer = new float[20000];
	final int bufferSize = buffer.length;
	int index = 0;
	float buffered = 0;
	float bias = 0;
	float amp = 1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SoundInput#write(byte[])
	 */
	@Override
	public synchronized void set(float data) {
		data = Math.abs(data);
		buffer[index] = data;
		buffered += data;

		buffered -= buffer[(index + bufferSize - size) % bufferSize];
		index = (index + 1) % bufferSize;

		connectedDevice.set(bias + (buffered / size) * amp);
	}

	/**
	 * Sets bias of the signal
	 * 
	 * @param level
	 * 
	 */
	public synchronized void setSpeed(float level) {
		this.size = (int) level;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SoundOutput#connectTo(org.modsyn.SoundInput)
	 */
	@Override
	public synchronized void connectTo(SignalInput input) {
		connectedDevice = input;
	}

}
