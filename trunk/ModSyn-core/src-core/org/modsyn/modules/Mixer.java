/*
 * Created on Apr 23, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.modsyn.modules;

import java.util.ArrayList;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;
import org.modsyn.SignalSource;

/**
 * @author Erik Duijs
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Mixer implements SignalSource, SignalInput {

	private final ArrayList<SignalOutput> channels = new ArrayList<SignalOutput>();
	private SignalInput connectedDevice;
	private float buffer;
    
    
    //int mixed = 0;

	public Mixer(Context context) {
		context.addSignalSource(this);
	}

	public void addChannel(SignalOutput output) {
		output.connectTo(this);
		channels.add(output);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioOutput#connectTo(org.modsyn.AudioInput)
	 */
	@Override
	public void connectTo(SignalInput input) {
		this.connectedDevice = input;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioGenerator#updateAudio()
	 */
	@Override
	public void updateSignal() {
		connectedDevice.set(buffer);
		buffer = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioInput#write(byte[])
	 */
	/**
	 * Write & mix channel data
	 */
	@Override
	public void set(float data) {
        buffer += data;
	}

}
