/*
 * Created on Apr 27, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.example;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.modules.Oscillator;
import org.modsyn.modules.RingModulator;
import org.modsyn.modules.ext.FromJavaSound;
import org.modsyn.modules.ext.ToJavaSound;
import org.modsyn.util.WaveTables;

/**
 * @author Erik Duijs
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class TestGrabber {

	public static void main(String[] args) {
		Context context = ContextFactory.create();

		/*--------------------------------
		 * AUDIO SIGNALS
		 *--------------------------------
		 */

		FromJavaSound grabber = new FromJavaSound(context, 32, 1024 * 16);

		// Master for play back using javax.sound
		ToJavaSound master = new ToJavaSound(context, ToJavaSound.MONO, 1024);

		// Ring Modulator
		RingModulator ring = new RingModulator(context);
		ring.connectTo(master.inputL);
		ring.setModulation(0.75f);

		grabber.connectTo(ring);

		Oscillator modulator = new Oscillator(context, WaveTables.SINUS);
		modulator.connectTo(ring.modInput);
		modulator.setFrequency(500);

		for (int i = 0; i < 105000 * 32; i++) {
			// update all modules
			context.update();
			// tone1.setFrequency(220);
		}

		// Stop play back
		master.stop();
		System.exit(0);

	}
}
