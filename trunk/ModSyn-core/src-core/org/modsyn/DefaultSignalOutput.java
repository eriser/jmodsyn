package org.modsyn;

/**
 * Convenience base-class for a SignalOutput. This will prevent connections to a
 * SignalInput that resolve to 'null', and will hold a reference to the
 * connected SignalInput for subclasses to use (i.e. 'connectedInput').
 * 
 * @author Erik Duijs
 */
public class DefaultSignalOutput implements SignalOutput {

	/**
	 * The SignalInput that this SignalOutput is connected to. This value is
	 * public for convenience, but should only be read!
	 */
	public SignalInput connectedInput = NullInput.INSTANCE;

	/**
	 * The signal that is transmitted when <code>connectTo(SignalInput)</code>
	 * is called, unless this value is <code>Float.MIN_VALUE</code>
	 */
	private final float signalWhenConnect;

	/**
	 * Constructor for when you need this SignalOutput to transmit a default
	 * signal as soon it's connected to a new SignalInput, unless the signal is
	 * <code>Float.MIN_VALUE</code>
	 * 
	 * @param signalWhenConnect
	 *            The signal to transmit when connecting to a SignalInput,
	 *            unless this value is <code>Float.MIN_VALUE</code>
	 */
	public DefaultSignalOutput(float signalWhenConnect) {
		this.signalWhenConnect = signalWhenConnect;
	}

	/**
	 * Default constructor that doesn't transmit a signal when calling
	 * <code>connectTo(SignalInput)</code>
	 */
	public DefaultSignalOutput() {
		this(Float.MIN_VALUE);
	}

	@Override
	public void connectTo(SignalInput input) {
		if (input == null) {
			connectedInput = NullInput.INSTANCE;
		} else {
			connectedInput = input;
			if (signalWhenConnect != Float.MIN_VALUE) {
				connectedInput.set(signalWhenConnect);
			}
		}
	}
}
