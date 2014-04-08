package org.modsyn.util;

import java.util.Random;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;
import org.modsyn.modules.Adder;
import org.modsyn.modules.Amplifier;
import org.modsyn.modules.Arpeggio;
import org.modsyn.modules.Binaural;
import org.modsyn.modules.Compressor;
import org.modsyn.modules.Delay;
import org.modsyn.modules.Filter4Pole;
import org.modsyn.modules.Filter8Pole;
import org.modsyn.modules.FilterXPole;
import org.modsyn.modules.Karlsen24dB;
import org.modsyn.modules.KarplusStrong;
import org.modsyn.modules.LPF;
import org.modsyn.modules.Mix;
import org.modsyn.modules.MoogVCF;
import org.modsyn.modules.MultiSplitter;
import org.modsyn.modules.Noise;
import org.modsyn.modules.Octaver;
import org.modsyn.modules.Oscillator;
import org.modsyn.modules.OscillatorHQ;
import org.modsyn.modules.PitchShifter;
import org.modsyn.modules.RingModulator;
import org.modsyn.modules.Splitter;
import org.modsyn.modules.Tracker;
import org.modsyn.modules.VarDelayHQ;
import org.modsyn.modules.fx.Vocoder;

public class PerfTest {

	private static final int ITERATIONS = 500000;
	private static final Random RNG = new Random(1);
	private static boolean display;

	public static void main(String[] args) {
		{ // lotsawarmup
			display = false;
			test();
			test();
			test();
			test();
		}

		display = true;
		test();
	}

	private static void test() {
		if (display)
			System.out.println("-------- TEST SignalSources -----------");

		Context c = ContextFactory.create();
		testUpdate(c, new Oscillator(c));

		c.clear();
		OscillatorHQ ohq = new OscillatorHQ(c);
		ohq.ctrlOversampling.set(1);
		testUpdate(c, ohq);

		c.clear();
		ohq = new OscillatorHQ(c);
		ohq.ctrlOversampling.set(4);
		testUpdate(c, ohq);

		c.clear();
		ohq = new OscillatorHQ(c);
		ohq.ctrlOversampling.set(16);
		testUpdate(c, ohq);

		c.clear();
		testUpdate(c, new Noise(c));

		c.clear();
		testUpdate(c, new KarplusStrong(c));

		c.clear();
		testUpdate(c, new Arpeggio(c, 4));

		c.clear();
		testUpdate(c, new Mix(c, 4));

		c.clear();
		testUpdate(c, new RingModulator(c));

		c.clear();

		if (display)
			System.out.println("\n-------- TEST SignalInputss -----------");
		testInput(c, new Adder());
		testInput(c, new Amplifier());
		testInput(c, new Splitter());
		testInput(c, new Binaural(c));
		testInput(c, new MultiSplitter(4));
		testInput(c, new Octaver());
		testInput(c, new PitchShifter());
		testInput(c, new Filter4Pole());
		testInput(c, new Filter8Pole());
		testInput(c, new FilterXPole(4));
		testInput(c, new FilterXPole(8));
		testInput(c, new FilterXPole(16));
		testInput(c, new LPF(c));
		testInput(c, new Karlsen24dB());
		testInput(c, new MoogVCF(c));
		testInput(c, new Tracker());
		testInput(c, new Compressor());
		testInput(c, new Delay(c, 0.5f, 0.5f));
		testInput(c, new VarDelayHQ(c, 1, 0.5f, 0.5f));
		testInput(c, new Vocoder(6).inputCarrier);
	}

	private static final void testInput(Context c, SignalInput o) {
		// warm up
		for (int i = 0; i < ITERATIONS; i++) {
			o.set(RNG.nextFloat());
		}

		long start = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			o.set(RNG.nextFloat());
		}
		long time = System.nanoTime() - start;
		if (display) {
			double millis = (time / 1000000.0);
			int updatesPerMilli = (int) (ITERATIONS / millis);
			double updatesPerSec = (ITERATIONS / (time / 1000000000.0));
			int maxRealTime = (int) (updatesPerSec / c.getSampleRate());
			System.out.println(String.format("%1$25s - updates/ms: %2$6s - max: %3$6s", o.getClass().getSimpleName(), updatesPerMilli, maxRealTime));
		}
	}

	private static final void testUpdate(Context c, SignalSource o) {
		// warm up
		for (int i = 0; i < ITERATIONS; i++) {
			c.update();
		}

		long start = System.nanoTime();
		for (int i = 0; i < ITERATIONS; i++) {
			c.update();
		}
		long time = System.nanoTime() - start;
		if (display) {
			double millis = (time / 1000000.0);
			int updatesPerMilli = (int) (ITERATIONS / millis);
			double updatesPerSec = (ITERATIONS / (time / 1000000000.0));
			int maxRealTime = (int) (updatesPerSec / c.getSampleRate());
			System.out.println(String.format("%1$25s - updates/ms: %2$6s - max: %3$6s", o.getClass().getSimpleName(), updatesPerMilli, maxRealTime));
		}
	}

}
