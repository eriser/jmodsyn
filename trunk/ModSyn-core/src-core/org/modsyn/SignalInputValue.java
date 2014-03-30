package org.modsyn;

/**
 * Convenience class or a SignalInput that simply sets a value
 * 
 * @author Erik Duijs
 */
public class SignalInputValue implements SignalInput {

	/**
	 * The value of the signal. This is public for convenience, but should
	 * normally not be written to.
	 */
	public float value;

	/**
	 * Constructor.
	 * 
	 * @param def
	 *            The default value of this input.
	 */
	public SignalInputValue(float def) {
		this.value = def;
	}

	/**
	 * Constructor. The default value of this input is 0.
	 */
	public SignalInputValue() {
		this(0);
	}

	@Override
	public void set(float signal) {
		value = signal;
	}
}
