package org.modsyn.editor.blocks;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.ext.AsioSupport;

public class FromAsioModel extends DspBlockModel<AsioSupport> {

	public FromAsioModel(Context c, AsioSupport o) {
		super(o);

		add(new OutputModel(this, o, "OUT"));
	}

	public FromAsioModel(Context c) {
		this(c, AsioSupport.INSTANCE);
	}

	@Override
	public String getName() {
		return "Asio IN";
	}
}