package org.modsyn.editor.io;

import javax.swing.JOptionPane;

import org.modsyn.NullInput;
import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.editor.io.XmlExportMeta.MetaInputFactory;
import org.modsyn.editor.io.XmlExportMeta.MetaOutputFactory;

public class MetaInOutFactory implements MetaInputFactory, MetaOutputFactory {

	@Override
	public OutputModel createOutputModel(DspBlockComponent block, OutputModel source) {
		String to = (source.getTarget() != null && source.getTarget().getInput() != NullInput.INSTANCE) ? " (to "
				+ source.getTarget().getSoundBlockModel().getName() + "/" + source.getTarget().getName() + ")" : "";

		String name = JOptionPane.showInputDialog("Rename output " + source.getSoundBlockModel().getName() + "/" + source.getName() + to, source.getName());
		if (name == null) {
			return null;
		}
		source.setName(name);
		return source;
	}

	@Override
	public InputModel createInputModel(DspBlockComponent block, InputModel source) {
		String from = (source.getSource() != null) ? " (from " + source.getSource().getName() + "/" + source.getSource().getName() + ")" : "";
		String name = JOptionPane.showInputDialog("Rename input " + source.getSoundBlockModel().getName() + "/" + source.getName() + from, source.getName());
		if (name == null) {
			return null;
		}
		source.setName(name);
		return source;
	}

}
