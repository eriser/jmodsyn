package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.ext.FromJavaSound;

public class FromJavaSoundModel extends DspBlockModel<FromJavaSound> {

	public FromJavaSoundModel(Context c, FromJavaSound o) {
		super(o);

		add(new OutputModel(this, o, "OUT"));
	}

	public FromJavaSoundModel(Context c) {
		this(c, new FromJavaSound(c, 32, 1024 * 7));
	}

	@Override
	public String getName() {
		return "Audio IN";
	}
}
