package org.modsyn;

/**
 * A SignalInput that does nothing. Used to prevent null-checks for unconnected
 * SignalOutputs. Every SignalOutput should default to this for it's target
 * SignalInput.
 * 
 * @author Erik Duijs
 */
public enum NullInput implements SignalInput {
	INSTANCE;

	@Override
	public void set(float signal) {
	}
}