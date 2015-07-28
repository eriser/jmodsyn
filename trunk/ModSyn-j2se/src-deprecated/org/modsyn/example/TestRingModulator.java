/*
 * Created on Apr 25, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.example;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.modules.Amplifier;
import org.modsyn.modules.Mixer;
import org.modsyn.modules.Oscillator;
import org.modsyn.modules.PanPot;
import org.modsyn.modules.RingModulator;
import org.modsyn.modules.ext.ToJavaSound;
import org.modsyn.util.WaveTables;

/**
 * @author Erik Duijs
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class TestRingModulator {

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
		volume1.lvl.set(1);

		// Ring Modulator
		RingModulator ring = new RingModulator(context);
		ring.connectTo(volume1);
		ring.setModulation(0);

		// 2 Tone Generators
		Oscillator tone1 = new Oscillator(context, WaveTables.SINUS);
		tone1.connectTo(ring);
		// grabber.connectTo(volume1);
		Oscillator modulator = new Oscillator(context, WaveTables.SINUS);
		modulator.connectTo(ring.modInput);

		// Play some sound for 5000 * buffer size samples
		for (int i = 0; i < 5000 * 32; i++) {
			// update all modules
			context.update();
			tone1.setFrequency(220);
			modulator.setFrequency(220 + (i % (5000 * 32)) / (32));
			ring.setModulation((i % (5000 * 32)) / (5000f * 32));
		}

		// Stop play back
		master.stop();
		System.exit(0);

	}
}
