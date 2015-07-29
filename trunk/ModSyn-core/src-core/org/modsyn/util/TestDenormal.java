package org.modsyn.util;

public class TestDenormal {

	private static final int COUNT = 10;
	private static final float MULTIPLY = 0.999999f;
	private static final int ITERATIONS = 20000000;
	static float DC_OFFSET = Float.MIN_NORMAL * 2;

	public static void main(String[] args) {
		System.out.println("DC_OFFSET = " + DC_OFFSET);
		for (int i = 0; i < 10; i++) {
			testDenormal1();
			testDenormal2();
			testDenormal3();
			testDenormal4();
			testDenormal5();
		}

	}

	private static void testDenormal1() {
		System.out.println("testDenormal1 (denormalizing, multiply)-----------------");
		float d = 1;

		for (int count = 1; count < COUNT; count++) {
			long start = System.nanoTime();
			for (int i = ITERATIONS; i > 0; i--) {
				d *= MULTIPLY;
			}
			System.out.println(count + ": " + (System.nanoTime() - start) + "   " + d + " \t" + (d < Float.MIN_NORMAL));
		}
		System.out.println("--------------------------------------------------------");
	}

	private static void testDenormal2() {
		System.out.println("testDenormal2 (add offset, multiply)--------------------");
		float d = 1;

		for (int count = 1; count < COUNT; count++) {
			long start = System.nanoTime();
			for (int i = ITERATIONS; i > 0; i--) {
				d += DC_OFFSET;
				d *= MULTIPLY;
			}
			System.out.println(count + ": " + (System.nanoTime() - start) + "   " + d + " \t" + (d < Float.MIN_NORMAL));
		}
		System.out.println("--------------------------------------------------------");
	}

	private static void testDenormal4() {
		System.out.println("testDenormal4 (branch, multiply)------------------------");
		float d = 1;

		for (int count = 1; count < COUNT; count++) {
			long start = System.nanoTime();
			for (int i = ITERATIONS; i > 0; i--) {
				d *= MULTIPLY;
				if (Math.abs(d) < DC_OFFSET) {
					d = 0;
				}
			}
			System.out.println(count + ": " + (System.nanoTime() - start) + "   " + d + " \t" + (d < Float.MIN_NORMAL));
		}
		System.out.println("--------------------------------------------------------");
	}

	private static void testDenormal5() {
		System.out.println("testDenormal5 (nudge, multiply)-------------------------");
		float d = 1;

		for (int count = 1; count < COUNT; count++) {
			long start = System.nanoTime();
			for (int i = ITERATIONS; i > 0; i--) {
				d = NUDGE(d * MULTIPLY);
			}
			System.out.println(count + ": " + (System.nanoTime() - start) + "   " + d + " \t" + (d < Float.MIN_NORMAL));
		}
		System.out.println("--------------------------------------------------------");
	}

	public static final float NUDGE(float d) {
		if (Math.abs(d) < DC_OFFSET) {
			d = 0;
		}
		return d;
	}

	private static void testDenormal3() {
		System.out.println("testDenormal3 (add offset)------------------------------");
		float d = 0;

		for (int count = 1; count < COUNT; count++) {
			long start = System.nanoTime();
			for (int i = ITERATIONS; i > 0; i--) {
				d += DC_OFFSET;
			}
			System.out.println(count + ": " + (System.nanoTime() - start) + "   " + d + " \t" + (d < Float.MIN_NORMAL));
		}
		System.out.println("--------------------------------------------------------");
	}
}
