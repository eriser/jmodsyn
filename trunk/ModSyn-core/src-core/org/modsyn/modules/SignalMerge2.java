/*
 * Created on 3-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;
import org.modsyn.SignalSource;

/**
 * @author DU1381
 * 
 *         Add 2 inputs
 */
public class SignalMerge2 extends DefaultSignalOutput implements SignalSource, SignalInput {

	public SignalOutput channel1, channel2;
	private float buffer;

	public SignalMerge2(Context context) {
		context.addSignalSource(this);
	}

	public void setChannel(int channel, SignalOutput output) {
		output.connectTo(this);
		if (channel == 0) {
			channel1 = output;
		} else {
			channel2 = output;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioGenerator#updateAudio()
	 */
	@Override
	public void updateSignal() {
		connectedInput.set(buffer);
		buffer = 0;
	}

	/**
	 * Write & mix channel data
	 */
	@Override
	public void set(float data) {
		buffer += data;
	}
}
