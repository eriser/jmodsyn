package org.modsyn.editor.io;

import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.editor.io.XmlExportMeta.MetaInputFactory;
import org.modsyn.editor.io.XmlExportMeta.MetaOutputFactory;

public class MetaInOutFactory implements MetaInputFactory, MetaOutputFactory {

	@Override
	public OutputModel createOutputModel(DspBlockComponent block, OutputModel source) {
		// TODO Auto-generated method stub
		return source;
	}

	@Override
	public InputModel createInputModel(DspBlockComponent block, InputModel source) {
		// TODO Auto-generated method stub
		return source;
	}

}
