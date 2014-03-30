/*
 * Created on Apr 22, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.example;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.modules.Amplifier;
import org.modsyn.modules.LPF;
import org.modsyn.modules.Mixer;
import org.modsyn.modules.Oscillator;
import org.modsyn.modules.PanPot;
import org.modsyn.modules.ctrl.ADSREnvelope;
import org.modsyn.modules.ext.ToJavaSound;
import org.modsyn.util.WaveTables;

/**
 * @author Erik Duijs
 * 
 *         A test sound with 2 tone generators connected to filters, an
 *         amplifier to prevent clipping and panning. Plays back using JavaSound
 */
public class Test {

	// @formatter:off
	// Patch in this test:
	// +--------+ /|
	// +--------------+ +-----+ +-----+ +-----+ 1| | +-/ |
	// |tone generator|==>| LPF |==>| Amp |==>| Pan |==>| Mixer |====>| | |
	// master out L
	// +------^-------+ +--^--+ +-----+ | | | | +-\ |
	// | | +-----+\ +--------+ \|
	// | | \\//2
	// Envelope Envelope XX
	// //\\2
	// +--------------+ +-----+ +-----+ +-----+/ +--------+ /|
	// |tone generator|==>| LPF |==>| Amp |==>| Pan |==>| | +-/ |
	// +------^-------+ +--^--+ +-----+ | | 1| Mixer |====>| | | master out R
	// | | +-----+ | | +-\ |
	// | | +--------+ \|
	// Envelope Envelope
	// @formatter:on

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

		// 2 Pan pots connected to L and R mixers
		PanPot pan1 = new PanPot();
		PanPot pan2 = new PanPot();
		mixerL.addChannel(pan1.outputL);
		mixerL.addChannel(pan2.outputL);
		mixerR.addChannel(pan1.outputR);
		mixerR.addChannel(pan2.outputR);
		pan1.setPanning(+1f);
		pan2.setPanning(-1f);

		// 2 Amplifiers connected to Pan pots
		// used to prevent clipping
		Amplifier volume1 = new Amplifier();
		Amplifier volume2 = new Amplifier();
		volume1.connectTo(pan1);
		volume2.connectTo(pan2);
		volume1.control.set(0.20f);
		volume2.control.set(0.20f);

		// 2 Low Pass Filters connected to Amplifiers
		LPF filter1 = new LPF(context);
		LPF filter2 = new LPF(context);
		filter1.connectTo(volume1);
		filter2.connectTo(volume2);
		filter1.setResonance(1);
		filter2.setResonance(10);

		// 2 Tone Generators connected to filters
		Oscillator tone1 = new Oscillator(context, WaveTables.SQUARE);
		Oscillator tone2 = new Oscillator(context, WaveTables.SQUARE);
		tone1.connectTo(filter1);
		tone2.connectTo(filter2);

		/*--------------------------------
		 * CONTROL SIGNALS
		 *--------------------------------
		 */

		// 2 envelopes for controlling the 2 filters' cut-off frequency
		ADSREnvelope envFilter1 = new ADSREnvelope(context);
		ADSREnvelope envFilter2 = new ADSREnvelope(context);
		envFilter1.connectTo(filter1.cutOffControl);
		envFilter2.connectTo(filter2.cutOffControl);

		envFilter1.setLevel(ADSREnvelope.ATTACK_LEVEL, 80);
		envFilter1.setTime(ADSREnvelope.ATTACK_TIME, 0.1f);
		envFilter1.setLevel(ADSREnvelope.DECAY_LEVEL, 10000);
		envFilter1.setTime(ADSREnvelope.DECAY_TIME, 1.5f);
		envFilter1.setLevel(ADSREnvelope.SUSTAIN_LEVEL, 1000);
		envFilter1.setLevel(ADSREnvelope.RELEASE_LEVEL, 100);
		envFilter1.setTime(ADSREnvelope.RELEASE_TIME, 1f);

		envFilter2.setLevel(ADSREnvelope.ATTACK_LEVEL, 80);
		envFilter2.setTime(ADSREnvelope.ATTACK_TIME, 0.1f);
		envFilter2.setLevel(ADSREnvelope.DECAY_LEVEL, 1600);
		envFilter2.setTime(ADSREnvelope.DECAY_TIME, 1.5f);
		envFilter2.setLevel(ADSREnvelope.SUSTAIN_LEVEL, 80);
		envFilter2.setLevel(ADSREnvelope.RELEASE_LEVEL, 10000);
		envFilter2.setTime(ADSREnvelope.RELEASE_TIME, 2f);

		// 2 envelopes for controlling the 2 tone generator's frequency
		ADSREnvelope envTone1 = new ADSREnvelope(context);
		ADSREnvelope envTone2 = new ADSREnvelope(context);
		envTone1.connectTo(tone1.ctrFreq);
		envTone2.connectTo(tone2.ctrFreq);

		envTone1.setLevel(ADSREnvelope.ATTACK_LEVEL, 10f);
		envTone1.setTime(ADSREnvelope.ATTACK_TIME, 0.01f);
		envTone1.setLevel(ADSREnvelope.DECAY_LEVEL, 100);
		envTone1.setTime(ADSREnvelope.DECAY_TIME, 0.5f);
		envTone1.setLevel(ADSREnvelope.SUSTAIN_LEVEL, 70);
		envTone1.setLevel(ADSREnvelope.RELEASE_LEVEL, 61);
		envTone1.setTime(ADSREnvelope.RELEASE_TIME, 1f);

		envTone2.setLevel(ADSREnvelope.ATTACK_LEVEL, 80f);
		envTone2.setTime(ADSREnvelope.ATTACK_TIME, 0.03f);
		envTone2.setLevel(ADSREnvelope.DECAY_LEVEL, 101);
		envTone2.setTime(ADSREnvelope.DECAY_TIME, 0.5f);
		envTone2.setLevel(ADSREnvelope.SUSTAIN_LEVEL, 71);
		envTone2.setLevel(ADSREnvelope.RELEASE_LEVEL, 30);
		envTone2.setTime(ADSREnvelope.RELEASE_TIME, 2f);

		// Trigger envelopes
		envFilter1.trigger(true);
		envFilter2.trigger(true);
		envTone1.trigger(true);
		envTone2.trigger(true);

		// Play some sound for 5000 * buffer size samples
		for (int i = 0; i < 5000 * 32; i++) {
			// update all modules
			context.update();

			// update PWM of a tone generator in real time
			float pwm = i / (50f * 32f);
			tone1.setPWM(pwm);
			tone2.setPWM(pwm);

			// control triggers
			boolean keyOn = (i < (32000));
			envFilter1.trigger(keyOn);
			envFilter2.trigger(keyOn);
			envTone1.trigger(keyOn);
			envTone2.trigger(keyOn);

			// update panning in real time
			float pan = (i - 2500 * 32) / (25 * 32 * 100);
			pan1.setPanning(pan);
			pan2.setPanning(-pan);
		}

		// Stop play back
		master.stop();
		System.exit(0);
	}
}
