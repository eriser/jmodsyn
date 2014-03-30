package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInputValue;
import org.modsyn.util.WaveTables;

public class Octaver extends DefaultSignalOutput implements SignalInput {
	private static final int len = 1024 * 2;

	public final SignalInputValue mixUpDown = new SignalInputValue();

	private final float[] ring = new float[len];
	private int idxRU0 = 0, idxRU1 = ring.length / 2;
	private int idxRD0 = 0, idxRD1 = ring.length / 2;
	private int idxW = 0;

	private final float[] mixtable = WaveTables.COSINUS;
	private int idxMU = 0, idxMD = 0;
	private int incD = 0;
	private final int stepU = mixtable.length / ring.length;
	private final int stepD = (mixtable.length / ring.length) / 2;

	@Override
	public void set(float signal) {
		final float octU0 = (ring[idxRU0++] + ring[idxRU0++]) / 2f;
		final float octU1 = (ring[idxRU1++] + ring[idxRU1++]) / 2f;

		final float octD0 = ring[idxRD0];
		final float octD1 = ring[idxRD1];

		idxRD0 += incD;
		idxRD1 += incD;
		incD ^= 1;

		ring[idxW++] = signal;

		idxRU0 %= len;
		idxRU1 %= len;
		idxRD0 %= len;
		idxRD1 %= len;
		idxW %= len;

		final float mixU = (1 - mixtable[idxMU]) / 2f;
		final float mixD = (1 - mixtable[idxMD]) / 2f;
		idxMU = (idxMU + stepU) % WaveTables.WAVE_LENGTH;
		idxMD = (idxMD + stepD) % WaveTables.WAVE_LENGTH;

		final float octUp = (octU0 * mixU + octU1 * (1 - mixU));
		final float octDown = (octD0 * mixD + octD1 * (1 - mixD));

		float ampUp = (1 + mixUpDown.value) / 2f;
		float ampDown = 1 - ampUp;

		connectedInput.set(octDown * ampDown + octUp * ampUp);
	}
}
