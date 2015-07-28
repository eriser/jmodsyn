package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.KarplusStrong;

public class KarplusStrongModel extends DspBlockModel<KarplusStrong> {

	public KarplusStrongModel(Context c, KarplusStrong o) {
		super(o);

		add(new InputModel(this, o.frequencyControl, "frq", 440f, 0, 48000));
		add(new InputModel(this, o.trigger, "trg", 0, 0, 1, 0));
		add(new InputModel(this, o.fbControl, "fb", 0.995f, 0.95f, 1f));
		add(new InputModel(this, o.cutoffControl, "cut", 6000f, 20, 20000));
		add(new InputModel(this, o.pluckControl, "plu", 0.01f, 0, 0.1f));
		add(new InputModel(this, o.pluckEnvControl, "env", 1f, 0, 1f));

		add(new OutputModel(this, o, "OUT"));
	}

	public KarplusStrongModel(Context c) {
		this(c, new KarplusStrong(c));
	}

	@Override
	public String getName() {
		return "K-Str";
	}
}
