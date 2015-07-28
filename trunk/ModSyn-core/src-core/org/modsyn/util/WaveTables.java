/*
 * Created on Apr 23, 2004
 */
package org.modsyn.util;

import static java.lang.Math.PI;

import java.util.Random;

/**
 * @author Erik Duijs
 * 
 *         Tables of some useful waveforms.
 */
public class WaveTables {

	public static final int WAVE_LENGTH = 1024 * 2;
	static final int MAX_FREQ = 20480;
	static final int FREQ_WINDOWS_SIZE = 20;
	static final int FREQ_WINDOWS = MAX_FREQ / FREQ_WINDOWS_SIZE;
	static final int[] FREQ_TABLE_LOOKUP = new int[FREQ_WINDOWS];
	static {
		int table = 0;
		int c1 = 0, c2 = 1;
		for (int i = 0; i < FREQ_TABLE_LOOKUP.length; i++) {
			if (c1 == c2) {
				table++;
				c2 *= 2;
			}
			c1++;
			FREQ_TABLE_LOOKUP[i] = table;
		}
	}
	private static final float PI2 = 2f * (float) PI;
	private static final float div = WAVE_LENGTH / PI2;

	public static final int SHAPE_ID_SINUS = 0;
	public static final int SHAPE_ID_TRIANGLE = 1;
	public static final int SHAPE_ID_SAWTOOTH = 2;
	public static final int SHAPE_ID_SQUARE = 3;
	public static final int SHAPE_ID_COSINUS = 4;
	public static final int SHAPE_ID_HALF_COS = 5;
	public static final int SHAPE_ID_HALF_SIN = 6;
	public static final int SHAPE_ID_NOISE = 7;

	public static final int SHAPE_ID_SINSAW = 8;
	public static final int SHAPE_ID_SINTRIANGLE = 9;
	public static final int SHAPE_ID_BIPULSE = 10;

	public static final float[] SINUS = new float[WAVE_LENGTH];
	public static final float[] COSINUS = new float[WAVE_LENGTH];
	public static final float[] TRIANGLE = new float[WAVE_LENGTH];
	public static final float[] SAWTOOTH = new float[WAVE_LENGTH];
	public static final float[] SQUARE = new float[WAVE_LENGTH];
	public static final float[] HALF_COS = new float[WAVE_LENGTH];
	public static final float[] HALF_SIN = new float[WAVE_LENGTH];
	public static final float[] NOISE = new float[WAVE_LENGTH];

	public static final float[] SINSAW = new float[WAVE_LENGTH];
	public static final float[] SINTRIANGLE = new float[WAVE_LENGTH];
	public static final float[] BIPULSE = new float[WAVE_LENGTH];

	// @formatter:off
	public static final float[][] WAVES = {
		SINUS,
		COSINUS,
		TRIANGLE,
		SAWTOOTH,
		SQUARE,
		HALF_COS,
		HALF_SIN,
		NOISE,
		
		SINSAW,
		SINTRIANGLE,
		BIPULSE
	};
	// @formatter:on

	/**
	 * [id][freq][wave];
	 */
	public static final float[][][] BAND_LIMITED_WAVES = new float[WAVES.length][FREQ_WINDOWS][];
	static {

	}

	public static final int SHAPE_ID_MAX = WAVES.length - 1;

	static {
		float max = 1f;
		float min = -1f;
		float rc1 = (max - min) / WAVE_LENGTH;
		float rc2 = (max - min) / (WAVE_LENGTH / 2);
		Random rnd = new Random();

		for (int i = 0; i < WAVE_LENGTH; i++) {
			float r = (PI2 / WAVE_LENGTH) * i;
			SINUS[i] = (float) (Math.sin(r) * max);
			COSINUS[i] = (float) (Math.cos(r) * max);

			float sv = min + rc1 * i;
			while (sv > 1) {
				sv -= 2f;
			}
			SAWTOOTH[i] = sv;

			NOISE[i] = (rnd.nextFloat() * 2f) - 1;

			if (i < WAVE_LENGTH / 2) {
				TRIANGLE[i] = (min + rc2 * i);
				TRIANGLE[WAVE_LENGTH - 1 - i] = TRIANGLE[i];

				HALF_COS[i] = (float) (Math.cos(r) * max);
				HALF_COS[WAVE_LENGTH - 1 - i] = min;

				HALF_SIN[i] = (float) (Math.sin(r) * max * 2f) + min;
				HALF_SIN[WAVE_LENGTH - 1 - i] = min;

				SQUARE[i] = min;
				SQUARE[WAVE_LENGTH - 1 - i] = max;
			}
		}

		int half = WAVE_LENGTH / 2;
		int quarter = WAVE_LENGTH / 4;

		for (int i1 = 0; i1 < quarter; i1++) {
			int i2 = quarter + i1;
			int i3 = half + i1;
			int i4 = half + quarter + i1;
			int iDouble = i1 * 2;

			TRIANGLE[i1] = SAWTOOTH[half + iDouble];
			TRIANGLE[i2] = SAWTOOTH[WAVE_LENGTH - 1 - iDouble];
			TRIANGLE[i3] = -TRIANGLE[i1];
			TRIANGLE[i4] = -TRIANGLE[i2];

			BIPULSE[i2] = 1;
			BIPULSE[i4] = -1;
		}

		for (int i1 = 0; i1 < half; i1++) {
			int i2 = half + i1;
			int iDouble = i1 * 2;

			SINSAW[i1] = SINTRIANGLE[i1] = SINUS[i1];
			SINSAW[i2] = SAWTOOTH[iDouble];
			SINTRIANGLE[i2] = -TRIANGLE[iDouble];
		}

		// the waveforms have been created; now create the band-limited versions.
		FFT fft = new FFT();
		for (int i = 0; i < WAVES.length; i++) {
			int curTable = 0;
			float[] curWave = WAVES[i];
			for (int j = 0; j < FREQ_WINDOWS; j++) {
				int table = FREQ_TABLE_LOOKUP[j];
				if (table != curTable) {
					curTable = table;
					curWave = fft.createBandLimitedWave(WAVES[i], curTable);
				}
				BAND_LIMITED_WAVES[i][j] = curWave;
			}
		}
	}

	public static float[] getWaveForm(int shape_id) {
		if (shape_id >= 0 && shape_id < WAVES.length) {
			return WAVES[shape_id];
		} else {
			return SINUS;
		}
	}

	public static float[] getWaveForm(int shape_id, float freq) {
		if (shape_id >= 0 && shape_id < WAVES.length) {
			int freqWindow = (int) ((freq * 2) / FREQ_WINDOWS_SIZE);
			return BAND_LIMITED_WAVES[shape_id][freqWindow];
		} else {
			return SINUS;
		}
	}

	public static final float cos(float rad) {
		return COSINUS[(int) (rad * div)];
	}
}
