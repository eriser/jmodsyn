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
		public final float[] wave = new float[250];
		private final float[] buffer = new float[wave.length * 8];
		private final int startIdx = buffer.length - wave.length;
		public int ptr = 0;
		final int interval = buffer.length;
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
		public synchronized void set(float signal) {
			ptr %= buffer.length;
			if (count == 0) {
				count = interval;

				try {
					System.arraycopy(buffer, startIdx, wave, 0, wave.length);
				} catch (Exception e) {
					e.printStackTrace();
				}
				SwingUtilities.invokeLater(model.updater);
			}
			count--;

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
		setFixedSize();
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
