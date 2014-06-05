/*
 * Created on Apr 23, 2004
 */
package org.modsyn.util;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

import java.util.Random;

/**
 * @author Erik Duijs
 * 
 *         Tables of some useful waveforms.
 */
public class WaveTables {

	public static final int WAVE_LENGTH = 1024 * 8;
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

	public static final int SHAPE_ID_MAX = SHAPE_ID_NOISE;

	public static final float[] SINUS = new float[WAVE_LENGTH];
	public static final float[] COSINUS = new float[WAVE_LENGTH];
	public static final float[] TRIANGLE = new float[WAVE_LENGTH];
	public static final float[] SAWTOOTH = new float[WAVE_LENGTH];
	public static final float[] SQUARE = new float[WAVE_LENGTH];
	public static final float[] HALF_COS = new float[WAVE_LENGTH];
	public static final float[] HALF_SIN = new float[WAVE_LENGTH];
	public static final float[] NOISE = new float[WAVE_LENGTH];

	static {
		float max = 1f;
		float min = -1f;
		float rc1 = (max - min) / WAVE_LENGTH;
		float rc2 = (max - min) / (WAVE_LENGTH / 2);
		Random rnd = new Random();

		for (int i = 0; i < WAVE_LENGTH; i++) {
			float r = (PI2 / WAVE_LENGTH) * i;
			SINUS[i] = (float) (sin(r) * max);
			COSINUS[i] = cos(r) * max;

			float sv = min + rc1 * i;
			while (sv > 1) {
				sv -= 2f;
			}
			SAWTOOTH[i] = sv;

			NOISE[i] = (rnd.nextFloat() * 2f) - 1;

			if (i < WAVE_LENGTH / 2) {
				TRIANGLE[i] = (min + rc2 * i);
				TRIANGLE[WAVE_LENGTH - 1 - i] = TRIANGLE[i];

				HALF_COS[i] = cos(r * 2) * max;
				HALF_COS[WAVE_LENGTH - 1 - i] = min;

				HALF_SIN[i] = (float) (sin(r) * max * 2f) + min;
				HALF_SIN[WAVE_LENGTH - 1 - i] = min;

				SQUARE[i] = min;
				SQUARE[WAVE_LENGTH - 1 - i] = max;
			}
		}
	}

	public static float[] getWaveForm(int shape_id) {
		switch (shape_id) {
		case SHAPE_ID_SINUS:
			return SINUS;
		case SHAPE_ID_COSINUS:
			return COSINUS;
		case SHAPE_ID_SAWTOOTH:
			return SAWTOOTH;
		case SHAPE_ID_SQUARE:
			return SQUARE;
		case SHAPE_ID_TRIANGLE:
			return TRIANGLE;
		case SHAPE_ID_HALF_COS:
			return HALF_COS;
		case SHAPE_ID_HALF_SIN:
			return HALF_SIN;
		case SHAPE_ID_NOISE:
			return NOISE;
		default:
			return SINUS;
		}
	}

	public static float cos(float rad) {
		return COSINUS[(int) (rad * div)];
	}

}
