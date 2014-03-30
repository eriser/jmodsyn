package org.modsyn.modules.fx;

import org.modsyn.Context;
import org.modsyn.Device;
import org.modsyn.DeviceControl;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.SignalOutput;
import org.modsyn.modules.PanPot;
import org.modsyn.modules.SignalMerge2;
import org.modsyn.modules.VarDelay;
import org.modsyn.modules.VarDelayHQ;
import org.modsyn.modules.ctrl.LFO;
import org.modsyn.util.WaveTables;

/**
 * @author DU1381
 * 
 *         Mono Chorus effect. Can also be used as a flanger if the average
 *         delay is set lower.
 * 
 *         +----------+ | LFO | +-----+----+ | delay time +----------+ L
 *         +----------+ 0+----------+ ----->| PanPot +------| VarDelay +-----|
 *         Merge +------> +--------+-+ +----------+ +----------+ | R |1
 *         +--------------------------+
 * 
 *         PanPot full left = 100% chorus, center = 50% chorus, 50% dry
 *         (default) full right = 0% chorus
 * 
 */
public class Chorus implements SignalInsert, Device {

	public final PanPot mix;
	public final SignalInput wdt;
	public final SignalInput frq;
	public final SignalInput dly;
	public final SignalInput fbk;
	
	private final VarDelay delay;
	private final LFO lfo;
	private final SignalMerge2 merge;

	private final SignalInput[] inputs;
	private final SignalOutput[] outputs;
	private final DeviceControl[] deviceControls;

	public Chorus(Context context) {
		lfo = new LFO(context, WaveTables.SINUS);
		delay = new VarDelayHQ(context, 1, 0.2f, 0.5f);
		mix = new PanPot();
		merge = new SignalMerge2(context);

		inputs = new SignalInput[1];
		outputs = new SignalOutput[1];
		inputs[0] = this;
		outputs[0] = this;

		mix.outputL.connectTo(delay);
		merge.setChannel(0, delay);
		merge.setChannel(1, mix.outputR);

		// set up defaults for Chorus effect
		delay.setFeedback(0.1f);
		lfo.setAmplitude(0.0015f);
		lfo.setFrequency(1f);
		lfo.setOffset(0.04f);
		lfo.connectTo(delay.delayTimeControl);
		
		wdt = lfo.amplitudeControl;
		frq = lfo.frequencyControl;
		dly = lfo.offsetControl;
		fbk = delay.feedbackControl;

		deviceControls = new DeviceControl[5];
		deviceControls[0] = new DeviceControl("Mix (wet-dry)", mix, 0, -1, +1);
		deviceControls[1] = new DeviceControl("Width", lfo.amplitudeControl, 0.0015f, 0, 0.05f);
		deviceControls[2] = new DeviceControl("Rate", lfo.frequencyControl, 1, 0, 10);
		deviceControls[3] = new DeviceControl("Delay", lfo.offsetControl, 0.04f, 0, 0.05f);
		deviceControls[4] = new DeviceControl("Feedback", delay.feedbackControl, 0.1f, 0, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SignalInput#write(float)
	 */
	@Override
	public void set(float sample) {
		mix.set(sample);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SignalOutput#connectTo(org.modsyn.SignalInput)
	 */
	@Override
	public void connectTo(SignalInput input) {
		merge.connectTo(input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getName()
	 */
	@Override
	public String getName() {
		return "Chorus";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getSignalInputs()
	 */
	@Override
	public SignalInput[] getSignalInputs() {
		return inputs;
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
		return deviceControls;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Device#getSubDevices()
	 */
	@Override
	public Device[] getSubDevices() {
		// TODO Auto-generated method stub
		return null;
	}

}
