/*
 * Created on Apr 29, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.example;

import org.modsyn.Context;
import org.modsyn.modules.Oscillator;
import org.modsyn.modules.RingModulator;
import org.modsyn.modules.ext.FromJavaSound;
import org.modsyn.modules.ext.ToJavaSound;
import org.modsyn.util.WaveTables;

/**
 * @author edy
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class TestFrame2AudioThread extends Thread {

	public float oscfreq = 300;
	public float mod = 80;
	private final Context context;

	public TestFrame2AudioThread(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void run() {
		super.run();

		/*--------------------------------
		 * AUDIO SIGNALS
		 *--------------------------------
		 */

		FromJavaSound grabber = new FromJavaSound(context, 32, 8192);

		// Master for play back using javax.sound
		ToJavaSound master = new ToJavaSound(context, ToJavaSound.MONO, 1024);

		// Ring Modulator
		RingModulator ring = new RingModulator(context);
		ring.connectTo(master.inputL);

		grabber.connectTo(ring);

		Oscillator modulator = new Oscillator(context, WaveTables.SINUS);
		modulator.connectTo(ring.modInput);

		while (true) {
			// update all modules
			ring.setModulation(mod);
			modulator.setFrequency(oscfreq);
			context.update();
		}
	}
}
