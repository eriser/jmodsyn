package org.modsyn.editor.blocks;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.modules.FormantFilter;

public class FormantFilterModel extends DspBlockModel<FormantFilter> {

	public FormantFilterModel(FormantFilter o) {
		super(o);

		add(new InputModel(this, o, "IN", 0, -1, 1));
		add(new InputModel(this, o.ctrlVowel, "vwl", 0, 0, 4));
		add(new InputModel(this, o.ctrlMixVowel, "mxvwl", 0, 0, 5, 2));

		add(new OutputModel(this, o, "OUT"));
	}

	public FormantFilterModel() {
		this(new FormantFilter());
	}

	@Override
	public String getName() {
		return "Formant";
	}
}
