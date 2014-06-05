package org.modsyn.modules;

import static java.lang.Math.abs;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInputValue;
import org.modsyn.util.WaveTables;

public class PitchShifter extends DefaultSignalOutput implements SignalInput {
	private static final int len = 1024 * 2;

	public final SignalInputValue ctrlPitch = new SignalInputValue(1);

	private final float[] ring = new float[len];
	private float idxRU0 = 0;
	private float idxRU1 = ring.length / 2;
	private int idxW = 0;

	private final float[] mixtable = WaveTables.COSINUS;

	@Override
	public void set(float signal) {
		final float octU0 = ring[(int) idxRU0];
		final float octU1 = ring[(int) idxRU1];

		ring[idxW++] = signal;

		idxRU0 += ctrlPitch.value;
		idxRU1 += ctrlPitch.value;

		idxRU0 %= len;
		idxRU1 %= len;
		idxW %= len;

		float ph = (idxW - idxRU0) / ring.length;
		int idxMU = (int) (abs(ph) * WaveTables.WAVE_LENGTH);
		final float mixU = (1 - mixtable[idxMU]) / 2f;

		final float pitched = (octU0 * mixU + octU1 * (1 - mixU));

		connectedInput.set(pitched);
	}
}
