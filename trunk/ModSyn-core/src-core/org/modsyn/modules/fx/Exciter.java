package org.modsyn.modules.fx;

import static java.lang.Math.tanh;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;
import org.modsyn.modules.FilterXPole;

public class Exciter implements SignalSource {

	public final SignalInput inL = new SignalInput() {
		@Override
		public void set(float signal) {
			left = signal;
		}
	};
	public final SignalInput inR = new SignalInput() {
		@Override
		public void set(float signal) {
			right = signal;
		}
	};
	public final SignalInput ctrlDrive = new SignalInput() {
		@Override
		public void set(float signal) {
			drive = signal;
		}
	};
	public final SignalInput ctrlDry = new SignalInput() {
		@Override
		public void set(float signal) {
			dry = signal;
		}
	};
	public final SignalInput ctrlEffect = new SignalInput() {
		@Override
		public void set(float signal) {
			effect = signal;
		}
	};
	public final SignalInput ctrlRange = new SignalInput() {
		@Override
		public void set(float signal) {
			hpfL.ctrlCutoff.set(signal);
			hpfR.ctrlCutoff.set(signal);
		}
	};
	public final SignalInput ctrlBias = new SignalInput() {
		@Override
		public void set(float signal) {
			bias = signal;
		}
	};
	public final DefaultSignalOutput outL = new DefaultSignalOutput();
	public final DefaultSignalOutput outR = new DefaultSignalOutput();

	private final FilterXPole hpfL = new FilterXPole(16);
	private final FilterXPole hpfR = new FilterXPole(16);

	private float left, right;
	private float dry, effect, drive, bias;

	public Exciter(Context c) {
		ctrlDry.set(1);
		ctrlEffect.set(1);
		hpfL.ctrlMode.set(FilterXPole.MODE_HPF);
		hpfR.ctrlMode.set(FilterXPole.MODE_HPF);

		hpfL.connectTo(new SignalInput() {
			@Override
			public void set(float signal) {
				if (signal > 0) {
					signal = (float) tanh(signal * (drive * (1 + bias)));
				} else {
					signal = (float) tanh(signal * (drive * (1 - bias)));
				}
				outL.connectedInput.set(signal * effect + left * dry);
			}
		});
		hpfR.connectTo(new SignalInput() {
			@Override
			public void set(float signal) {
				if (signal > 0) {
					signal = (float) tanh(signal * (drive * (1 + bias)));
				} else {
					signal = (float) tanh(signal * (drive * (1 - bias)));
				}
				outR.connectedInput.set(signal * effect + right * dry);
			}
		});
		c.addSignalSource(this);

	}

	@Override
	public void connectTo(SignalInput input) {
	}

	@Override
	public void updateSignal() {
		hpfL.set(left);
		hpfR.set(right);
	}
}
