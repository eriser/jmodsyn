package org.modsyn.vst;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;

public class ToVSTAudioModel extends DspBlockModel<VSTPluginAudioSupport> {

	public ToVSTAudioModel(Context context, VSTPluginAudioSupport dsp) {
		super(dsp);
		dsp.start(context);
		add(new InputModel(this, dsp.inputL, "IN.L", 0, -1, 1));
		add(new InputModel(this, dsp.inputR, "IN.R", 0, -1, 1));
	}

	@Override
	public String getName() {
		return "VST OUT";
	}
}