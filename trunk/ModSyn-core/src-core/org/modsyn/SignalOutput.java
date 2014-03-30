package org.modsyn;

/**
 * A SignalOutput can be directly compared to a physical signal output, for
 * example a speaker output or audio output jack on an amplifier. A SignalOutput
 * is to be connected to a SignalInput.
 * 
 * @author Erik Duijs
 */
public interface SignalOutput extends DspObject {
	/**
	 * Connect this SignalOutput to a SignalInput.
	 * 
	 * @param input
	 *            The SignalInput to connect to.
	 */
	public void connectTo(SignalInput input);
}
