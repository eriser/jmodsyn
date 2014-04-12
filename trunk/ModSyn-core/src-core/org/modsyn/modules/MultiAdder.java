package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.DspObject;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalInsert;

/**
 * @author Erik Duijs
 * 
 *         Add signal.
 */
public class MultiAdder extends DefaultSignalOutput implements SignalInsert, DspObject {

	public final SignalInputValue[] add;
	private final int channels;

	public MultiAdder(int channels) {
		super();
		this.channels = channels;
		this.add = new SignalInputValue[channels];
		for (int i = 0; i < channels; i++) {
			add[i] = new SignalInputValue();
		}
	}

	@Override
	public void set(float data) {
		for (int i = 0; i < channels; i++) {
			data += add[i].value;
		}
		connectedInput.set(data);
	}
}
