/*
 * Created on Apr 22, 2004
 */
package org.modsyn;

/**
 * An SignalOutputSource is a SignalOutput that is updated at every sample. This
 * typically means a signal source, for example an Oscillator.
 * 
 * @author Erik Duijs
 */
public interface SignalSource extends SignalOutput {

	/**
	 * Generates a new sample to transmit to our SignalOutput.
	 */
	public void updateSignal();
}
