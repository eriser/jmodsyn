/*
 * Created on Apr 25, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;

/**
 * @author Erik Duijs
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class RingModulator implements SignalSource, SignalInput {

	public final SignalInput modulationControl = new SignalInput() {
		@Override
		public void set(float data) {
			setModulation(data);
		}
	};

	public final SignalInput modInput = new RingMod();

	private SignalInput connectedDevice = NullInput.INSTANCE;

	private float modulation = 0.5f;
	private float buffer;
	private float modBuffer;

	public RingModulator(Context context) {
		context.addSignalSource(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioGenerator#updateAudio()
	 */
	@Override
	public void updateSignal() {
		float sample = buffer * (1f - modulation);
		buffer = sample + (buffer * ((modBuffer) * modulation));
		connectedDevice.set(buffer);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioInput#write(float[])
	 */
	@Override
	public void set(float data) {
		buffer = data;
	}

	/**
	 * Modulation
	 * 
	 * @param mod
	 *            0 percent = dry, 1 = full (no dry signal)
	 */
	public void setModulation(float mod) {
		modulation = mod;
	}

	class RingMod implements SignalInput {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.modsyn.AudioInput#write(float[])
		 */
		@Override
		public void set(float data) {
			modBuffer = data;
		}
	}
}
