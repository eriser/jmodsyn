package org.modsyn.modules;

import static java.lang.Math.abs;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalInsert;

/**
 * A SignalInsert that will transform every incoming signal to an absolute value, plus a bias signal.
 * 
 * @author Erik Duijs
 */
public class Absolute extends DefaultSignalOutput implements SignalInsert {

	/**
	 * SignalInput to control the bias. For example if you want to use this on an audio signal (which is in the range of
	 * -1 to 1), you can set the bias to -.5 to keep the normal signal at 0.
	 */
	public final SignalInputValue ctrlBias = new SignalInputValue();

	@Override
	public void set(float signal) {
		connectedInput.set(abs(signal) + ctrlBias.value);
	}
}
