package org.modsyn.editor.blocks;

import javax.swing.SwingUtilities;

import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;

public class ScopeModel extends DspBlockModel<ScopeModel.Scope> {

	Runnable updater = new Runnable() {
		@Override
		public void run() {
			if (component != null) {
				component.repaint();
			}
		}
	};

	public static class Scope implements SignalInsert {
		SignalInput connected = NullInput.INSTANCE;
		public final float[] wave = new float[1000];
		private final float[] buffer = new float[1000];
		public int ptr = 0;
		final int interval = 1000;
		int count = 0;
		public float amp = 1;

		public final SignalInput ctrlAmp = new SignalInput() {
			@Override
			public void set(float signal) {
				amp = signal;
			}
		};

		private ScopeModel model;

		@Override
		public void set(float signal) {
			if (count == 0) {
				count = interval;
				System.arraycopy(buffer, 0, wave, 0, wave.length);
				SwingUtilities.invokeLater(model.updater);
			}
			count--;

			ptr %= buffer.length;
			buffer[ptr++] = signal;

			connected.set(signal);
		}

		@Override
		public void connectTo(SignalInput input) {
			connected = input;

		}
	}

	public ScopeModel() {
		super(new Scope());
		getDspObject().model = this;
		add(new InputModel(this, getDspObject(), "IN", 0, -1, 1));
		add(new InputModel(this, getDspObject().ctrlAmp, "amp", 1, 0, 2));

		add(new OutputModel(this, getDspObject(), "OUT"));
	}

	@Override
	public String getName() {
		return "Scope";
	}
}
