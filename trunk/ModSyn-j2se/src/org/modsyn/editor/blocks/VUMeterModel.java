package org.modsyn.editor.blocks;

import javax.swing.SwingUtilities;

import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;

public class VUMeterModel extends DspBlockModel<VUMeterModel.VUMeter> {

	Runnable updater = new Runnable() {

		@Override
		public void run() {
			if (component != null) {
				component.repaint();
			}
		}
	};

	public static class VUMeter implements SignalInsert {
		public float value;
		SignalInput connected = NullInput.INSTANCE;
		int size = 2000;
		int count = 0;
		float max;
		private VUMeterModel model;

		@Override
		public void set(float signal) {
			if (count == 0) {
				count = size;
				value = max;
				max = 0;
				SwingUtilities.invokeLater(model.updater);
			}
			if (signal > max) {
				max = signal;
			}
			count--;
			connected.set(signal);
		}

		@Override
		public void connectTo(SignalInput input) {
			connected = input;

		}
	}

	public VUMeterModel() {
		super(new VUMeter());
		getDspObject().model = this;
		add(new InputModel(this, getDspObject(), "IN", 0, -1, 1));

		add(new OutputModel(this, getDspObject(), "OUT"));
	}

	@Override
	public String getName() {
		return "Meter";
	}
}
