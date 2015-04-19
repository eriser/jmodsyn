/*
 * Created on Apr 22, 2004
 */
package org.modsyn;

/**
 * An SignalSource is a SignalOutput that is updated at every sample. This typically means a signal source, for example
 * an Oscillator.<br>
 * A DspObject that is not a SignalSource is typically updated when it receives a signal on a SignalInput (and/or in
 * some cases an external event, such as a MIDI event or GUI event).
 * 
 * @author Erik Duijs
 */
public interface SignalSource extends SignalOutput {

	/**
	 * Generates a new sample to transmit to our SignalOutput.
	 */
	public void updateSignal();
}
