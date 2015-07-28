package org.modsyn.util;

public class FFT {

	/**
	 * 
	 * @param wave
	 *            The wave table
	 * @param oct
	 *            0=do nothing, 1=remove upper half harmonics, 2=remove upper
	 *            75% harmonics, etc
	 * @return
	 */
	public float[] createBandLimitedWave(float[] wave, int oct) {
		int length = wave.length;

		float[] freqWaveIm = new float[length];
		float[] freqWaveRe = new float[length];

		// convert to frequency domain
		for (int idx = 0; idx < length; idx++) {
			freqWaveIm[idx] = wave[idx];
			freqWaveRe[idx] = 0;
		}
		fft(freqWaveRe, freqWaveIm);

		// zero DC offset and Nyquist
		freqWaveRe[0] = freqWaveIm[0] = 0.0f;
		freqWaveRe[length >> 1] = freqWaveIm[length >> 1] = 0.0f;

		// remove harmonics
		for (int idx = length >> oct; idx < length; idx++) {
			freqWaveIm[idx] = freqWaveRe[idx] = 0;
		}

		return createWave(length, freqWaveRe, freqWaveIm);
	}

	public float[] createWave(int len, float[] ar, float[] ai) {
		fft(ar, ai);

		// calc normal
		float max = 0;
		for (int idx = 0; idx < len; idx++) {
			float temp = Math.abs(ai[idx]);
			if (max < temp)
				max = temp;
		}
		float scale = 1.0f / max * .999f;

		// normalize
		float[] wave = new float[len];
		for (int idx = 0; idx < len; idx++)
			wave[idx] = ai[idx] * scale;

		return wave;
	}

	/**
	 * 
	 * @param ar
	 *            wave-table real
	 * @param ai
	 *            wave-table ima
	 */
	public void fft(float[] ar, float[] ai)
	/*
	 * in-place complex fft
	 * 
	 * After Cooley, Lewis, and Welch; from Rabiner & Gold (1975)
	 * 
	 * program adapted from FORTRAN by K. Steiglitz (ken@princeton.edu) Computer
	 * Science Dept. Princeton University 08544
	 */
	{
		final int len = ar.length;
		final int nv2 = len >> 1;
		final int nm1 = len - 1;

		int temp = len; /* get M = log N */
		int m = 0;
		while ((temp >>= 1) != 0)
			++m;

		/* shuffle */
		for (int i = 1, j = 1; i <= nm1; i++) {
			if (i < j) { /* swap a[i] and a[j] */
				float t = ar[j - 1];
				ar[j - 1] = ar[i - 1];
				ar[i - 1] = t;
				t = ai[j - 1];
				ai[j - 1] = ai[i - 1];
				ai[i - 1] = t;
			}

			int k = nv2; /* bit-reversed counter */
			while (k < j) {
				j -= k;
				k /= 2;
			}

			j += k;
		}

		for (int l = 1, le = 1; l <= m; l++) { // stage L
			int le1 = le; // (LE1 = LE/2)
			le *= 2; // (LE = 2^L)
			float ur = 1.0f;
			float ui = 0.f;
			float wr = (float) Math.cos(Math.PI / le1);
			float wi = (float) -Math.sin(Math.PI / le1); // Cooley, Lewis, and
															// Welch have "+"
															// here
			for (int j = 1; j <= le1; j++) {
				for (int i = j; i <= len; i += le) { // butterfly
					int ip = i + le1;
					float tr = ar[ip - 1] * ur - ai[ip - 1] * ui;
					float ti = ar[ip - 1] * ui + ai[ip - 1] * ur;
					ar[ip - 1] = ar[i - 1] - tr;
					ai[ip - 1] = ai[i - 1] - ti;
					ar[i - 1] = ar[i - 1] + tr;
					ai[i - 1] = ai[i - 1] + ti;
				}
				float urOld = ur;
				ur = urOld * wr - ui * wi;
				ui = urOld * wi + ui * wr;
			}
		}
	}
}
