/*
 * Created on Apr 27, 2004
 */
package org.modsyn.example;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.modules.Oscillator;
import org.modsyn.modules.ext.ToJavaSound;
import org.modsyn.util.Tools;
import org.modsyn.util.WaveTables;

/**
 * @author Erik Duijs
 *
 * Play a sinus tone for 2 seconds.
 */
public class SimpleTest {

	public static void main(String[] args) {
		// Always initialize org.modsyn.Sys first
		Context context = ContextFactory.create();

		// Master for play back using javax.sound
		ToJavaSound master = new ToJavaSound(context, ToJavaSound.MONO, 1024);
		
		// Sinus Generator
        Oscillator tone1 = new Oscillator(context, WaveTables.SINUS);
        // Frequency from MIDI note 69
		tone1.setFrequency(Tools.getFreq(69));
		// Connect to master
		tone1.connectTo(master.inputL);
		
		// Play some sound for 2.0 seconds
		context.update(2.0f);
		
		// Stop play back
		master.stop();
		System.exit(0);
		
	}

}
