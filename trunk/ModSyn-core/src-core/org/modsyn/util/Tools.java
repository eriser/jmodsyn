/*
 * Created on Jun 16, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.util;

import static java.lang.Math.pow;

public class Tools {

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

	public static void main(String[] args) {
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
