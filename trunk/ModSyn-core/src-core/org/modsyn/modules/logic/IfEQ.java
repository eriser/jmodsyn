package org.modsyn.modules.logic;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalInsert;

/**
 * 
 * @author Erik Duijs
 */
public class IfEQ extends DefaultSignalOutput implements SignalInsert {

	public final SignalInputValue ctrl = new SignalInputValue(1);

	public final DefaultSignalOutput trigger = new DefaultSignalOutput(0);
	public final DefaultSignalOutput outTrue = new DefaultSignalOutput(0);
	public final DefaultSignalOutput outFalse = new DefaultSignalOutput(0);

	@Override
	public void set(float signal) {
		if (signal == ctrl.value) {
			connectedInput.set(1);
			outTrue.connectedInput.set(signal);
			outFalse.connectedInput.set(0);
		} else {
			connectedInput.set(0);
			outFalse.connectedInput.set(signal);
			outTrue.connectedInput.set(0);
		}
	}
}
