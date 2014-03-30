/*
 * Created on Apr 22, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.DspObject;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalInsert;

/**
 * @author Erik Duijs
 * 
 *         Amplifier of an audio signal.
 */
public class Amplifier extends DefaultSignalOutput implements SignalInsert, DspObject {

	public final SignalInputValue control = new SignalInputValue(1);

	@Override
	public synchronized void set(float data) {
		connectedInput.set(data * control.value);
	}
}
