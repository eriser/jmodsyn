package org.modsyn.editor.blocks;

import org.modsyn.MetaDspObject;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;

public class MetaModel extends DspBlockModel<MetaDspObject> {

	private String name;

	public MetaModel(MetaDspObject dsp, String name) {
		super(dsp);
		this.name = name;
	}

	public void addMetaInput(InputModel input) {
		add(input);
	}

	public void addMetaOutput(OutputModel output) {
		add(output);
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String newName) {
		this.name = newName;
	}
}
