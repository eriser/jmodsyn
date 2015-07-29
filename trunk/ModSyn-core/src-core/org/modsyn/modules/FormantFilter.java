package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

public class FormantFilter extends DefaultSignalOutput implements SignalInsert {

	public static final int A = 0;
	public static final int E = 1;
	public static final int I = 2;
	public static final int O = 3;
	public static final int U = 4;

	public static final int VOWELS = 5;

	private static final double[][] VOWEL_COEFFS = new double[5][11];
	static {
		VOWEL_COEFFS[A] = new double[] { 8.11044e-06, 8.943665402, -36.83889529, 92.01697887, -154.337906, 181.6233289, -151.8651235, 89.09614114,
				-35.10298511, 8.388101016, -0.923313471 };
		VOWEL_COEFFS[E] = new double[] { 4.36215e-06, 8.90438318, -36.55179099, 91.05750846, -152.422234, 179.1170248, -149.6496211, 87.78352223, -34.60687431,
				8.282228154, -0.914150747 };
		VOWEL_COEFFS[I] = new double[] { 3.33819e-06, 8.893102966, -36.49532826, 90.96543286, -152.4545478, 179.4835618, -150.315433, 88.43409371,
				-34.98612086, 8.407803364, -0.932568035 };
		VOWEL_COEFFS[O] = new double[] { 1.13572e-06, 8.994734087, -37.2084849, 93.22900521, -156.6929844, 184.596544, -154.3755513, 90.49663749, -35.58964535,
				8.478996281, -0.929252233 };
		VOWEL_COEFFS[U] = new double[] { 4.09431e-07, 8.997322763, -37.20218544, 93.11385476, -156.2530937, 183.7080141, -153.2631681, 89.59539726,
				-35.12454591, 8.338655623, -0.910251753 };
	}

	private final double[] memory = new double[10];

	private double[] curVowel = VOWEL_COEFFS[A];

	public final SignalInput ctrlVowel = new SignalInput() {
		@Override
		public void set(float signal) {
			setVowel(Math.round(signal));
		}
	};

	public void setVowel(int i) {
		curVowel = VOWEL_COEFFS[i];
	}

	@Override
	public void set(float signal) {
		double res = (curVowel[0] * signal + curVowel[1] * memory[0] + curVowel[2] * memory[1] + curVowel[3] * memory[2] + curVowel[4] * memory[3]
				+ curVowel[5] * memory[4] + curVowel[6] * memory[5] + curVowel[7] * memory[6] + curVowel[8] * memory[7] + curVowel[9] * memory[8] + curVowel[10]
				* memory[9]);

		memory[9] = memory[8];
		memory[8] = memory[7];
		memory[7] = memory[6];
		memory[6] = memory[5];
		memory[5] = memory[4];
		memory[4] = memory[3];
		memory[3] = memory[2];
		memory[2] = memory[1];
		memory[1] = memory[0];
		memory[0] = res;

		connectedInput.set((float) res);
	}
}
