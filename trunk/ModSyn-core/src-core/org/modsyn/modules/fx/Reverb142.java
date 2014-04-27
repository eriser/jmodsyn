package org.modsyn.modules.fx;

import org.modsyn.Context;
import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.modules.AllPassFilter;
import org.modsyn.modules.Delay;

/**
 * Reverb using Schroeder's algorithm. Uses 4 comb filters, and 2 all-pass filters
 * 
 * FLOAT fvgc_time[FVCombs] = { > 1116, > 1188, > 1277, > 1356, > 1422, > 1491, > 1557, > 1617 > }; > > FLOAT
 * fvgc_gain[FVCombs] = { > .5f, > .5f, > .5f, > .5f, > .5f, > .5f, > .5f, > .5f > }; > > FLOAT fvga_time[FVAlpas] = { >
 * 556, > 441, > 341, > 225 > }; > > FLOAT fvga_gain[FVAlpas] = { > .5f, > .5f, > .5f, > .5f > };
 * 
 * @author Erik Duijs
 */
public class Reverb142 implements SignalInsert {
	private SignalInput connectedDevice = NullInput.INSTANCE;
	Delay delay0, delay1, delay2, delay3;
	AllPassFilter apf0, apf1;
	float dry, wet;

	float time, decay1, decay2;
	private final Context context;

	public Reverb142(Context context, float time, float decay, float density, float dry_wet) {
		this.context = context;
		init(time, decay, density, dry_wet);
	}

	public Reverb142(Context context) {
		this.context = context;
		init(1, 0.5f, 0.5f, 0.25f);
	}

	public void init(float time, float fbDelay, float fbAPF, float dry_wet) {
		setDelayTimes(time, fbDelay, fbAPF);
		setDryWet(dry_wet);
	}

	public void setDelayTimes(float time, float fbDelay, float fbAPF) {
		delay0 = new Delay(context, (1116f / 44100f) * time, fbDelay);
		delay1 = new Delay(context, (1188f / 44100f) * time, fbDelay);
		delay2 = new Delay(context, (1277f / 44100f) * time, fbDelay);
		delay3 = new Delay(context, (1356f / 44100f) * time, fbDelay);
		apf0 = new AllPassFilter(context, (556f / 44100f) * time, fbAPF);
		apf1 = new AllPassFilter(context, (441f / 44100f) * time, fbAPF);
	}

	public void setDryWet(float dry_wet) {
		this.dry = 1f - dry_wet;
		this.wet = dry_wet;
	}

	/**
	 * Process the reverb.
	 * 
	 * @param input
	 *            is the sample to feed the reverb
	 * @return the output from the reverb
	 */
	public float process(float input) {
		float rev;

		// run through 4 parallel delays
		rev = delay0.process(input);
		rev += delay1.process(input);
		rev += delay2.process(input);
		rev += delay3.process(input);

		// run mixed output of the 4 delays through 2 all-pass filters
		rev = apf0.process(rev);
		rev = apf1.process(rev);

		rev *= 0.25f;

		return (rev * wet) + (input * dry);
	}

	@Override
	public void set(float input) {
		connectedDevice.set(process(input));
	}

	@Override
	public void connectTo(SignalInput input) {
		this.connectedDevice = input;
		;
	}

}
