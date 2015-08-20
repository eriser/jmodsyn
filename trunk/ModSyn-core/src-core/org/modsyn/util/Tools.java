/*
 * Created on Jun 16, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.util;

import static java.lang.Math.pow;

import java.util.Random;

public class Tools {

	public static final boolean FAST_MATH = true;

	public static final float[] FREQUENCY = new float[128];
	public static final double LOG2 = Math.log(2);

	static {
		for (int key = 0; key < 128; key++) {
			FREQUENCY[key] = (float) (440f * pow(2, (key - 69) / 12f));
		}
	}

	public static float getFreq(int MIDInote) {
		return FREQUENCY[MIDInote];
	}

	public static int getOctave(float freq) {
		return (int) (Math.log(freq) / LOG2);
	}

	public static float tanh(float f) {
		if (FAST_MATH) {
			float xa = Math.abs(f);
			float x2 = xa * xa;
			float x3 = xa * x2;
			float x4 = x2 * x2;
			float x7 = x3 * x4;
			float res = (1.0f - 1.0f / (1.0f + xa + x2 + 0.58576695f * x3 + 0.55442112f * x4 + 0.057481508f * x7));
			return (f > 0 ? res : -res);
		} else {
			return (float) Math.tanh(f);
		}
	}

	public static void main(String[] args) {
		for (float f = 0; f < 10; f += 0.25f) {
			System.out.println("tanh(" + f + ") = \t" + ((float) Math.tanh(f)) + " \t~ " + tanh(f));
			System.out.println(" tanh(" + (-f) + ") = \t" + ((float) Math.tanh(-f)) + " \t~ " + tanh(-f));
		}

		Random rng = new Random(1);

		float f = 0;
		long start = System.nanoTime();
		for (int i = 0; i < 10000000; i++) {
			f += Math.tanh(rng.nextFloat());
		}
		System.out.println(((System.nanoTime() - start) / 1000000f) + " time " + f);
		f = 0;
		start = System.nanoTime();
		for (int i = 0; i < 10000000; i++) {
			f += tanh(rng.nextFloat());
		}
		System.out.println(((System.nanoTime() - start) / 1000000f) + " time " + f);

		testOctaves();
	}

	private static void testOctaves() {
		System.out.println(getOctave(1));
		System.out.println(getOctave(2));
		System.out.println(getOctave(4));
		System.out.println(getOctave(8));
		System.out.println(getOctave(16));
		System.out.println(getOctave(32));
		System.out.println(getOctave(64));
		System.out.println(getOctave(128));
		System.out.println(getOctave(256));
		System.out.println(getOctave(512));
		System.out.println(getOctave(1024));
		System.out.println(getOctave(2048));
		System.out.println(getOctave(4096));
		System.out.println(getOctave(8192));
		System.out.println(getOctave(16384));
	}
}
