package org.modsyn.editor.blocks;

import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.gui.JKnob;

public class KnobModel extends DspBlockModel<KnobModel.KnobOut> {

	public KnobModel(JKnob knob) {
		this(knob, new KnobOut());
	}

	public KnobModel(final JKnob knob, KnobOut dsp) {
		super(dsp);
		add(new OutputModel(this, dsp, " >") {
			@Override
			public void connectTo(InputModel input) {
				super.connectTo(input);
				knob.setName(input.getSoundBlockModel().getName() + " / " + input.getName());
				knob.setValue(input.getValue());
				knob.setMin(input.getMin());
				knob.setMax(input.getMax());
				knob.setDecimals(input.getDecimals());
			}

			@Override
			public void disconnect() {
				super.disconnect();
				knob.setName("---");
				knob.setValue(0);
				knob.setMin(0);
				knob.setMax(1);
				knob.setDecimals(2);
			}
		});
	}

	@Override
	public String getName() {
		return "Knob";
	}

	public static class KnobOut implements SignalOutput {
		public SignalInput connected = NullInput.INSTANCE;

		@Override
		public void connectTo(SignalInput input) {
			connected = input;
		}
	}
}
