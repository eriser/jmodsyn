package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.modules.ext.AsioSupport;

public class ToAsioModel extends DspBlockModel<AsioSupport> {

	public ToAsioModel(Context context, AsioSupport dsp) {
		super(dsp);
		dsp.start(context);
		add(new InputModel(this, dsp.inputL, "IN.L", 0, -1, 1));
		add(new InputModel(this, dsp.inputR, "IN.R", 0, -1, 1));
	}

	@Override
	public String getName() {
		return "Asio OUT";
	}
}
