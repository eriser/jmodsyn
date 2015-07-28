/*
 * Created on Apr 28, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.example;

import org.modsyn.Context;
import org.modsyn.modules.Amplifier;
import org.modsyn.modules.LPF;
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
public class TestFrameAudioThread extends Thread {

	public float osc1freq = 100;
	public float osc2freq = 100;
	public boolean running = true;

	public float p1 = -50;
	public float p2 = +50;
	public float q1 = 1;
	public float q2 = 1;
	public float co1 = 2000;
	public float co2 = 2000;
	public float a1 = 25;
	public float a2 = 25;
	public float pwm1 = 50;
	public float pwm2 = 50;

	public float lfo1f = 0;
	public float lfo2f = 0;
	public float lfo1a = 0;
	public float lfo2a = 0;
	private final Context context;

	public TestFrameAudioThread(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void run() {
		super.run();

		Oscillator osc1 = new Oscillator(context, WaveTables.SAWTOOTH);
		Oscillator osc2 = new Oscillator(context, WaveTables.SQUARE);
		LPF lpf1 = new LPF(context);
		LPF lpf2 = new LPF(context);
		LFO lfo1 = new LFO(context, WaveTables.SINUS);
		LFO lfo2 = new LFO(context, WaveTables.SINUS);
		Amplifier amp1 = new Amplifier();
		Amplifier amp2 = new Amplifier();
		PanPot pan1 = new PanPot();
		PanPot pan2 = new PanPot();
		Mixer mixL = new Mixer(context);
		Mixer mixR = new Mixer(context);
		ToJavaSound out = new ToJavaSound(context, ToJavaSound.STEREO, 1024);

		mixL.connectTo(out.inputL);
		mixR.connectTo(out.inputR);
		mixL.addChannel(pan1.outputL);
		mixL.addChannel(pan2.outputL);
		mixR.addChannel(pan1.outputR);
		mixR.addChannel(pan2.outputR);
		amp1.connectTo(pan1);
		amp2.connectTo(pan2);
		lfo1.connectTo(lpf1.cutOffControl);
		lfo2.connectTo(lpf2.cutOffControl);
		lpf1.connectTo(amp1);
		lpf2.connectTo(amp2);
		osc1.connectTo(lpf1);
		osc2.connectTo(lpf2);

		while (running) {
			osc1.setFrequency(osc1freq);
			osc2.setFrequency(osc2freq);
			osc1.setPWM(pwm1);
			osc2.setPWM(pwm2);
			amp1.lvl.set(a1);
			amp2.lvl.set(a2);
			pan1.setPanning(p1);
			pan2.setPanning(p2);
			// lpf1.setCutOff(co1);
			// lpf2.setCutOff(co2);
			lpf1.setResonance(q1);
			lpf2.setResonance(q2);
			lfo1.setAmplitude(lfo1a);
			lfo2.setAmplitude(lfo2a);
			lfo1.setFrequency(lfo1f);
			lfo2.setFrequency(lfo2f);
			lfo1.setOffset(co1);
			lfo2.setOffset(co2);
			context.update();
		}

	}

}
