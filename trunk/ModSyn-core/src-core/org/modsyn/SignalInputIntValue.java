package org.modsyn;

/**
 * Convenience class or a SignalInput that simply sets an Integer value.
 * 
 * @author Erik Duijs
 */
public class SignalInputIntValue implements SignalInput {

	/**
	 * The value of this signal is public for convenience, but should normally
	 * never be written to.
	 */
	public int value;

	/**
	 * Constructor.
	 * 
	 * @param def
	 *            The default signal.
	 */
	public SignalInputIntValue(int def) {
		this.value = def;
	}

	/**
	 * Constructor. The default signal is 0.
	 */
	public SignalInputIntValue() {
		this(0);
	}

	/**
	 * Setting this signal will round the value to an Integer.
	 */
	@Override
	public void set(float signal) {
		value = Math.round(signal);
	}
}
