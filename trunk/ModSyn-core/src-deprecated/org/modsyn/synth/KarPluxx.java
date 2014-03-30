package org.modsyn.synth;

import org.modsyn.Context;
import org.modsyn.Device;
import org.modsyn.DeviceControl;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;
import org.modsyn.Synth;
import org.modsyn.modules.FxLoop;
import org.modsyn.modules.KarplusStrong;
import org.modsyn.modules.PanPot;
import org.modsyn.modules.fx.Chorus;
import org.modsyn.modules.fx.Phaser;
import org.modsyn.modules.fx.Reverb242;

/**
 * @author DU1381
 * 
 */
public class KarPluxx implements Synth, Device {

	KarplusStrong karplus;
	Chorus chorus;
	Reverb242 rev;
	FxLoop fx1, fx2;
	PanPot pan;
	Phaser phaser;

	SignalOutput[] outputs;
	DeviceControl[] controls;
	Device[] subDevices;

	/**
	 * Constructor
	 */
	public KarPluxx(Context context) {
		karplus = new KarplusStrong(context);
		chorus = new Chorus(context);
		rev = new Reverb242(context);
		//phaser = new Phaser();
		//rev1 = new Reverb(1,0.97f);
		//rev2 = new Reverb(.95f,0.97f);
		//fx1 = new FxLoop(rev1);
		//fx2 = new FxLoop(rev2);
		chorus.mix.setPanning(0.5f);
		pan = new PanPot();

		karplus.connectTo(chorus);
		chorus.connectTo(pan);
		pan.outputL.connectTo(rev.left);
		pan.outputR.connectTo(rev.right);

		outputs = new SignalOutput[2];
		controls = new DeviceControl[1];
		subDevices = new Device[3];

		outputs[0] = pan.outputL;
		outputs[1] = pan.outputR;
		subDevices[0] = karplus;
		subDevices[1] = chorus;
		subDevices[2] = rev;
	}

	@Override
	public SignalInput getInput(int index) {
		return null;
	}

	@Override
	public SignalOutput getOutput(int channel) {
		return channel == LEFT ? rev.left : rev.right;
	}

	@Override
	public void keyOff() {
		karplus.trigger(false);
	}

	@Override
	public void keyOn(float freq, float velo) {
		karplus.trigger(true);
		pan.setPanning((freq - 440f) / 1000f);
		karplus.setFrequency(freq);
		karplus.setOutputAmp(velo / 127f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getName()
	 */
	@Override
	public String getName() {
		return "KarPluxx";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getSignalInputs()
	 */
	@Override
	public SignalInput[] getSignalInputs() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getSignalOutputs()
	 */
	@Override
	public SignalOutput[] getSignalOutputs() {
		return outputs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getParameterControls()
	 */
	@Override
	public DeviceControl[] getDeviceControls() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getSubDevices()
	 */
	@Override
	public Device[] getSubDevices() {
		return subDevices;
	}

}
