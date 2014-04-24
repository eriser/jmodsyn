package org.modsyn.editor.io;

import javax.swing.JOptionPane;

import org.modsyn.NullInput;
import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.editor.io.XmlExportMeta.MetaInputFactory;
import org.modsyn.editor.io.XmlExportMeta.MetaOutputFactory;

public class MetaInOutFactory implements MetaInputFactory, MetaOutputFactory {

	private final boolean skipUnconnected;

	public MetaInOutFactory(boolean skipUnconnected) {
		this.skipUnconnected = skipUnconnected;
	}

	@Override
	public OutputModel createOutputModel(DspBlockComponent block, OutputModel source) {
		boolean connected = source.getTarget() != null && source.getTarget().getInput() != NullInput.INSTANCE;
		if (!connected && skipUnconnected) {
			return null;
		}

		String to = connected ? " (to " + source.getTarget().getSoundBlockModel().getName() + "/" + source.getTarget().getName() + ")" : "";

		String name = JOptionPane.showInputDialog("Rename output " + source.getSoundBlockModel().getName() + "/" + source.getName() + to, source.getName());
		if (name == null) {
			return null;
		}
		source.setName(name);
		return source;
	}

	@Override
	public InputModel createInputModel(DspBlockComponent block, InputModel source) {
		boolean connected = source.getSource() != null;
		if (!connected && skipUnconnected) {
			return null;
		}

		String from = connected ? " (from " + source.getSource().getName() + "/" + source.getSource().getName() + ")" : "";
		String name = JOptionPane.showInputDialog("Rename input " + source.getSoundBlockModel().getName() + "/" + source.getName() + from, connected ? source
				.getSource().getName() : source.getName());
		if (name == null) {
			return null;
		}
		source.setName(name);
		return source;
	}

}
