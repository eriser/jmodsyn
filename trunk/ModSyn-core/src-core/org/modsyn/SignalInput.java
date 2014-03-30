package org.modsyn;

/**
 * A SignalInput is an interface to set a signal. Everything is built around
 * signals, and a SignalInput can be an input of for example a volume
 * controller, an audio input, a trigger, etc. <br/>
 * Conceptually it can be directly compared to a physical signal input, for
 * example an audio-in jack on an amplifier.
 * 
 * @author Erik Duijs
 */
public interface SignalInput extends DspObject {
	/**
	 * Set the signal strength on this signal input. Normally 0.0f means 'no
	 * signal', and normally the range is -1.0 until 1.0 for audio signals.
	 * However, nothing prevents a signal to be outside of this range; and they
	 * will often be. For example mixing multiple signals together will
	 * typically lead to signals getting out of that -1.0 to 1.0 range. And a
	 * signal doesn't need to be audio, but can be any kind of signal (for
	 * example a signal that controls a frequency).<br/>
	 * A signal should be treated as being in the range -1.0f to 1.0f for audio,
	 * but can otherwise be completely arbitrary.
	 * 
	 * @param signal
	 *            The signal
	 */
	public void set(float signal);
}
