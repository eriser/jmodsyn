package org.modsyn.synth;

import org.modsyn.Context;
import org.modsyn.MIDISynth;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;
import org.modsyn.modules.Amplifier;
import org.modsyn.modules.Oscillator;
import org.modsyn.util.WaveTables;

/**
 * @author DU1381
 * 
 *         Almost the simplest synthesizer possible (it does react to velocity).
 */
public class Simple implements MIDISynth {

	public Oscillator osc;
	public Amplifier amp;

	SignalInput output;

	public Simple(Context context) {
		osc = new Oscillator(context, WaveTables.SAWTOOTH);
		amp = new Amplifier();

		osc.connectTo(amp);
		amp.control.set(0);
	}

	@Override
	public void keyOn(float freq, float velo) {
		osc.setFrequency(freq);
		amp.control.set(velo);
	}

	@Override
	public void keyOff() {
		amp.control.set(0);
	}

	@Override
	public SignalOutput getOutput(int channel) {
		return amp;
	}

	@Override
	public SignalInput getInput(int index) {
		return amp;
	}

}
