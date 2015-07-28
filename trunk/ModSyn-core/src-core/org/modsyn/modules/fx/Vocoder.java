package org.modsyn.modules.fx;

import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.SignalOutput;
import org.modsyn.modules.Amplifier;
import org.modsyn.modules.Filter4Pole;
import org.modsyn.modules.FilterXPole;
import org.modsyn.modules.MultiAdder;
import org.modsyn.modules.Tracker;

public class Vocoder implements SignalOutput {

	/**
	 * Input for Modulator (i.e. a Microphone)
	 */
	public SignalInput inputModulator = new SignalInput() {
		@Override
		public void set(float signal) {
			for (int i = 0; i < vocoderBands.length; i++) {
				vocoderBands[i].modulatorFilter.set(signal);
			}
			hpf.set(signal);
		}
	};

	/**
	 * Input for the Carrier (i.e. a Synth)
	 */
	public SignalInput inputCarrier = new SignalInput() {
		@Override
		public void set(float signal) {
			for (int i = 0; i < vocoderBands.length; i++) {
				vocoderBands[i].carrierFilter.set(signal);
			}
		}
	};

	/**
	 * 
	 */
	public SignalInput ctrlLow = new SignalInput() {
		@Override
		public void set(float signal) {
			for (int i = 0; i < vocoderBands.length; i++) {
				tuneBands(signal, high);
			}
		}
	};
	/**
	 * 
	 */
	public SignalInput ctrlHigh = new SignalInput() {
		@Override
		public void set(float signal) {
			for (int i = 0; i < vocoderBands.length; i++) {
				tuneBands(low, signal);
			}
		}
	};
	/**
	 * 
	 */
	public SignalInput ctrlPoles = new SignalInput() {
		@Override
		public void set(float signal) {
			for (int i = 0; i < vocoderBands.length; i++) {
				vocoderBands[i].ctrlPoles.set(signal);
			}
		}
	};
	/**
	 * 
	 */
	public SignalInput ctrlReso = new SignalInput() {
		@Override
		public void set(float signal) {
			for (int i = 0; i < vocoderBands.length; i++) {
				vocoderBands[i].ctrlReso.set(signal);
			}
		}
	};

	private final VocoderBand[] vocoderBands;
	private final Filter4Pole hpf = new Filter4Pole();

	private float low, high;

	private final MultiAdder adder;

	public Vocoder() {
		this(10);
	}

	public Vocoder(int bands) {
		vocoderBands = new VocoderBand[bands];
		adder = new MultiAdder(bands);
		for (int i = 0; i < vocoderBands.length; i++) {
			vocoderBands[i] = new VocoderBand();
			vocoderBands[i].ctrlPoles.set(8);
			vocoderBands[i].ctrlReso.set(1);
			vocoderBands[i].ctrlMode.set(i == 0 ? 0 : i == vocoderBands.length - 1 ? 2 : 1);
			vocoderBands[i].ctrlTrackSensitivity.set(5);
			vocoderBands[i].ctrlTrackSpeed.set(1000);
			vocoderBands[i].connectTo(i == 0 ? adder : adder.add[i - 1]);
		}
		tuneBands(0.05f, 0.6f);
		hpf.ctrlCutoff.set(0.6f);
		hpf.ctrlMode.set(2);
		hpf.connectTo(adder.add[vocoderBands.length - 1]);
	}

	public void tuneBands(float low, float high) {
		this.low = low;
		this.high = high;
		vocoderBands[0].ctrlCutoff.set(low);
		vocoderBands[vocoderBands.length - 1].ctrlCutoff.set(high);

		float step = (high - low) / vocoderBands.length;
		for (int i = 1; i < vocoderBands.length - 2; i++) {
			vocoderBands[i].ctrlCutoff.set(low + (i * step));
		}
	}

	public static class VocoderBand implements SignalInsert {
		public final FilterXPole modulatorFilter = new FilterXPole();
		public final FilterXPole carrierFilter = new FilterXPole();
		public final Tracker modulatorTracker = new Tracker();
		public final Amplifier output = new Amplifier();

		public final SignalInput ctrlTrackSensitivity = modulatorTracker.ctrlAmp;
		public final SignalInput ctrlTrackSpeed = modulatorTracker.ctrlSpeed;

		public VocoderBand() {
			modulatorFilter.connectTo(modulatorTracker);
			modulatorTracker.connectTo(output.lvl);

			carrierFilter.connectTo(output);
		}

		public final SignalInput ctrlCutoff = new SignalInput() {
			@Override
			public void set(float signal) {
				modulatorFilter.ctrlCutoff.set(signal);
				carrierFilter.ctrlCutoff.set(signal);
			}
		};
		public final SignalInput ctrlReso = new SignalInput() {
			@Override
			public void set(float signal) {
				modulatorFilter.ctrlReso.set(signal);
				carrierFilter.ctrlReso.set(signal);
			}
		};
		public final SignalInput ctrlMode = new SignalInput() {
			@Override
			public void set(float signal) {
				modulatorFilter.ctrlMode.set(signal);
				carrierFilter.ctrlMode.set(signal);
			}
		};
		public final SignalInput ctrlPoles = new SignalInput() {
			@Override
			public void set(float signal) {
				modulatorFilter.ctrlPoles.set(signal);
				carrierFilter.ctrlPoles.set(signal);
			}
		};

		@Override
		public void set(float signal) {
			carrierFilter.set(signal);
		}

		@Override
		public void connectTo(SignalInput input) {
			output.connectTo(input);
		}
	}

	@Override
	public void connectTo(SignalInput input) {
		adder.connectTo(input);
	}
}
