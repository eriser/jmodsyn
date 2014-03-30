/*
 * Created on Apr 26, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.example;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.modules.Amplifier;
import org.modsyn.modules.FMOperator;
import org.modsyn.modules.Mixer;
import org.modsyn.modules.Oscillator;
import org.modsyn.modules.PanPot;
import org.modsyn.modules.ctrl.LFO;
import org.modsyn.modules.ext.ToJavaSound;
import org.modsyn.util.WaveTables;

/**
 * @author edy
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class TestFM {

	public static void main(String[] args) {
		Context context = ContextFactory.create();

		/*--------------------------------
		 * AUDIO SIGNALS
		 *--------------------------------
		 */

		// Master for play back using javax.sound
		ToJavaSound master = new ToJavaSound(context, ToJavaSound.STEREO, 1024);

		// Mixers connected to the L and R input of the master for playback
		// using javax.sound
		Mixer mixerL = new Mixer(context);
		mixerL.connectTo(master.inputL);
		Mixer mixerR = new Mixer(context);
		mixerR.connectTo(master.inputR);

		// Pan pot connected to L and R mixers
		PanPot pan1 = new PanPot();
		mixerL.addChannel(pan1.outputL);
		mixerR.addChannel(pan1.outputR);
		pan1.setPanning(0);

		// Amplifier connected to Pan pot
		// used to prevent clipping
		Amplifier volume1 = new Amplifier();
		volume1.connectTo(pan1);
		volume1.control.set(0.50f);

		// FM Operator
		FMOperator fm = new FMOperator(context, WaveTables.SINUS);
		fm.setFrequency(55);
		fm.connectTo(volume1);

		// Tone Generator
		Oscillator modulator = new Oscillator(context, WaveTables.SINUS);
		modulator.setFrequency(55);
		modulator.connectTo(fm);

		// LFO
		LFO lfo = new LFO(context, WaveTables.SINUS);
		lfo.setAmplitude(5);
		lfo.setOffset(10);
		lfo.setFrequency(0.7f);
		lfo.connectTo(fm.modulationIndexControl);

		// Play some sound for 5000 * buffer size samples
		for (int i = 0; i < 10000 * 32; i++) {
			// update all modules
			context.update();
			// fm.setModulation(50f - (i / 100f));
		}

		// Stop play back
		master.stop();
		System.exit(0);

	}
}
