package org.modsyn.modules.logic;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalInsert;

/**
 * 
 * @author Erik Duijs
 */
public class IfRange extends DefaultSignalOutput implements SignalInsert {

	public final SignalInputValue low = new SignalInputValue();
	public final SignalInputValue high = new SignalInputValue();

	public final DefaultSignalOutput outTrue = new DefaultSignalOutput(0);
	public final DefaultSignalOutput outFalse = new DefaultSignalOutput(0);

	@Override
	public void set(float signal) {
		if (signal >= low.value && signal <= high.value) {
			connectedInput.set(1);
			outTrue.connectedInput.set(signal);
		} else {
			connectedInput.set(0);
			outFalse.connectedInput.set(signal);
		}
	}
}
