package org.modsyn.modules.logic;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalInsert;

/**
 * 
 * @author Erik Duijs
 */
public class IfGTE extends DefaultSignalOutput implements SignalInsert {

	public final SignalInputValue ctrl = new SignalInputValue();

	public final DefaultSignalOutput result = new DefaultSignalOutput(0);

	@Override
	public void set(float signal) {
		if (signal >= ctrl.value) {
			result.connectedInput.set(1);
		} else {
			result.connectedInput.set(0);
		}
		connectedInput.set(signal);
	}
}
