package org.modsyn.editor.io;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.editor.io.XmlExportMeta.MetaInputFactory;
import org.modsyn.editor.io.XmlExportMeta.MetaOutputFactory;

public class MetaInOutFactory implements MetaInputFactory, MetaOutputFactory {

	private final Map<String, Integer> outputs = new HashMap<>();
	private final Map<String, Integer> inputs = new HashMap<>();

	@Override
	public OutputModel createOutputModel(DspBlockComponent block, OutputModel source) {

		String name = JOptionPane.showInputDialog("Rename output " + source.getSoundBlockModel().getName() + "/" + source.getName(), source.getName());
		source.setName(name);
		return source;
	}

	@Override
	public InputModel createInputModel(DspBlockComponent block, InputModel source) {
		String name = JOptionPane.showInputDialog("Rename input " + source.getSoundBlockModel().getName() + "/" + source.getName(), source.getName());
		source.setName(name);
		return source;
	}

}
