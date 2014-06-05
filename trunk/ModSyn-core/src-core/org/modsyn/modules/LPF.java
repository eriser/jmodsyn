/*
 * Created on Apr 22, 2004
 */
package org.modsyn.modules;

import static java.lang.Math.PI;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.util.WaveTables;

/**
 * @author Erik Duijs
 */
public class LPF extends DefaultSignalOutput implements SignalInsert {

	public static final int ORDER_12DB = 1;
	public static final int ORDER_24DB = 2;

	private final float pi = (float) PI;
	private final float pi2 = pi * 2f;
	private float vibrapos = 0;
	private float vibraspeed = 0;
	private float resofreq;
	private float resonance;

	public final SignalInput cutOffControl = new CutOffControl();
	public final SignalInput resonanceControl = new ResonanceControl();

	float w;
	float q, r, c;
	private final Context context;

	public LPF(Context context) {
		this.context = context;
		w = pi2 * resofreq / context.getSampleRate();
		setCutOff(20000);
		setResonance(1);
	}

	public float process(float input) {
		vibraspeed += (input - vibrapos) * c;
		vibrapos += vibraspeed;
		vibraspeed *= r;
		return vibrapos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SoundInput#write(byte[])
	 */
	@Override
	public void set(float data) {
		vibraspeed += (data - vibrapos) * c;
		vibrapos += vibraspeed;
		vibraspeed *= r;

		connectedInput.set(vibrapos);
	}

	/**
	 * @param cutoff
	 *            Cutt Off frequency in Hz
	 */
	public void setCutOff(float cutoff) {
		if (cutoff < 20) {
			cutoff = 20;
		}
		resofreq = cutoff;
		w = pi2 * resofreq / context.getSampleRate(); // Pole angle
		q = 1.0f - w / (2.0f * (resonance + 0.5f / (1.0f + w)) + w - 2.0f);
		r = q * q;
		c = (r + 1.0f - 2.0f * WaveTables.cos(w) * q);
	}

	/**
	 * @param reso
	 *            Resonance (1.0 or higher)
	 */
	public void setResonance(float reso) {
		resonance = reso;
		q = 1.0f - w / (2.0f * (resonance + 0.5f / (1.0f + w)) + w - 2.0f);
		r = q * q;
		c = (r + 1.0f - 2.0f * WaveTables.cos(w) * q);
	}

	class CutOffControl implements SignalInput {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.modsyn.ControlInput#setControl(float)
		 */
		@Override
		public void set(float data) {
			setCutOff(data);
		}
	}

	class ResonanceControl implements SignalInput {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.modsyn.ControlInput#setControl(float)
		 */
		@Override
		public void set(float data) {
			setResonance(data);
		}
	}

}
