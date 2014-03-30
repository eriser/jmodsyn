package org.modsyn;

/**
 * A SignalInsert is a combination of a SignalInput and a SignalOutput. Usually
 * something that processes a signal (for example a filter or an amplifier) is a
 * SignalInsert.
 * 
 * @author Erik Duijs
 */
public interface SignalInsert extends SignalInput, SignalOutput, DspObject {
}
