package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInsert;

public class Butterworth24db extends DefaultSignalOutput implements SignalInsert {

	private static final float Q_SCALE = 6.f;

	private float t0, t1, t2, t3;
	private float coef0, coef1, coef2, coef3;
	private float history1, history2, history3, history4;
	private float gain;
	private float min_cutoff, max_cutoff;

	public Butterworth24db(Context c) {
		setSampleRate(c.getSampleRate());
	}

	public void setSampleRate(int sampleRate) {
		float fs = sampleRate;
		float pi = (float) (4.f * Math.atan(1.f));

		this.t0 = 4.f * fs * fs;
		this.t1 = 8.f * fs * fs;
		this.t2 = 2.f * fs;
		this.t3 = pi / fs;

		this.min_cutoff = fs * 0.01f;
		this.max_cutoff = fs * 0.45f;
	}

	public void set(float cutoff, float q) {
		if (cutoff < this.min_cutoff)
			cutoff = this.min_cutoff;
		else if (cutoff > this.max_cutoff)
			cutoff = this.max_cutoff;

		if (q < 0.f)
			q = 0.f;
		else if (q > 1.f)
			q = 1.f;

		float wp = (float) (this.t2 * Math.tan(this.t3 * cutoff));
		float bd, bd_tmp, b1, b2;

		q *= Q_SCALE;
		q += 1.f;

		b1 = (0.765367f / q) / wp;
		b2 = 1.f / (wp * wp);

		bd_tmp = this.t0 * b2 + 1.f;

		bd = 1.f / (bd_tmp + this.t2 * b1);

		this.gain = bd * 0.5f;

		this.coef2 = (2.f - this.t1 * b2);

		this.coef0 = this.coef2 * bd;
		this.coef1 = (bd_tmp - this.t2 * b1) * bd;

		b1 = (1.847759f / q) / wp;

		bd = 1.f / (bd_tmp + this.t2 * b1);

		this.gain *= bd;
		this.coef2 *= bd;
		this.coef3 = (bd_tmp - this.t2 * b1) * bd;
	}

	@Override
	public void set(float signal) {
		connectedInput.set(process(signal));
	}

	public float process(float input) {
		float output = input * this.gain;
		float new_hist;

		output -= this.history1 * this.coef0;
		new_hist = output - this.history2 * this.coef1;

		output = new_hist + this.history1 * 2.f;
		output += this.history2;

		this.history2 = this.history1;
		this.history1 = new_hist;

		output -= this.history3 * this.coef2;
		new_hist = output - this.history4 * this.coef3;

		output = new_hist + this.history3 * 2.f;
		output += this.history4;

		this.history4 = this.history3;
		this.history3 = new_hist;

		return output;
	}
}
