/*
 * Created on 26-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.modules;

import static java.lang.Math.round;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.WaveChangeListener;
import org.modsyn.WaveChangeObservable;
import org.modsyn.util.WaveTables;

/**
 * Wave shaper
 * 
 * @author Erik Duijs
 */
public class WaveTableShaper extends DefaultSignalOutput implements SignalInsert, WaveChangeObservable {

	private float[] wave;
	private WaveChangeListener wcl;
	private float ampIn, ampOut;

	public final SignalInput ctrShape = new SignalInput() {
		@Override
		public void set(float signal) {
			setShape(WaveTables.getWaveForm(round(signal)));
		}
	};

	public final SignalInput ctrMix = new SignalInput() {
		@Override
		public void set(float signal) {
			setMix(signal);
		}
	};

	public WaveTableShaper() {
		this(WaveTables.SINUS);
	}

	public WaveTableShaper(float[] wave) {
		setShape(wave);
		setMix(1);
	}

	public void setShape(float[] waveTable) {
		if (wcl != null && this.wave != waveTable) {
			wcl.waveChanged(waveTable);
		}
		this.wave = waveTable;
	}

	public void setMix(float mix) {
		ampIn = 1 - mix;
		ampOut = mix;
	}

	@Override
	public void set(float in) {
		float out = wave[(int) ((((in % 1) + 1) / 2f) * wave.length)];
		connectedInput.set((out * ampOut) + (in * ampIn));
	}

	@Override
	public void setWaveChangeListener(WaveChangeListener wcl) {
		this.wcl = wcl;
		wcl.waveChanged(wave);
	}
}
