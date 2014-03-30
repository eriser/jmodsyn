/*
 * Created on 24-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.modules.fx;

import org.modsyn.Context;
import org.modsyn.DeviceControl;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.SignalOutput;
import org.modsyn.modules.PanPot;
import org.modsyn.modules.SignalMerge2;
import org.modsyn.modules.VarAllPassFilter;
import org.modsyn.modules.ctrl.LFO;
import org.modsyn.util.WaveTables;

/**
 * @author DU1381
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Phaser implements SignalInsert {
	public final PanPot pan;
	public final LFO lfo;
	public final VarAllPassFilter allpass;
	private final SignalMerge2 merge;

	private final SignalInput[] inputs;
	private final SignalOutput[] outputs;
	private final DeviceControl[] deviceControls;

	public Phaser(Context context) {
		lfo = new LFO(context, WaveTables.SINUS);
		allpass = new VarAllPassFilter(context, 1, 0.0015f, 0.9f);
		pan = new PanPot();
		merge = new SignalMerge2(context);

		pan.outputL.connectTo(allpass);
		merge.setChannel(0, allpass);
		merge.setChannel(1, pan.outputR);

		lfo.connectTo(allpass);

		inputs = new SignalInput[1];
		outputs = new SignalOutput[1];
		inputs[0] = this;
		outputs[0] = this;

		deviceControls = new DeviceControl[5];
		deviceControls[0] = new DeviceControl("Mix (wet-dry)", pan, 1, -1, +1);
		deviceControls[1] = new DeviceControl("Width", lfo.amplitudeControl, 0.0015f, 0, 0.05f);
		deviceControls[2] = new DeviceControl("Rate", lfo.frequencyControl, 0.5f, 0, 10);
		deviceControls[3] = new DeviceControl("Delay", lfo.offsetControl, 0.003f, 0, 0.05f);
		deviceControls[4] = new DeviceControl("Feedback", allpass.feedbackControl, 0.9f, 0, 1);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SignalInput#write(float)
	 */
	@Override
	public void set(float sample) {
		pan.set(sample);
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
}
