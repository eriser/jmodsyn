package org.modsyn.editor.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.modsyn.NullInput;
import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.DspConnection;
import org.modsyn.editor.DspPatchModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.w3c.dom.Element;

public class XmlExportMeta extends XmlExport {

	public static interface MetaInputFactory {
		InputModel createInputModel(DspBlockComponent block, InputModel source);
	}

	public static interface MetaOutputFactory {
		OutputModel createOutputModel(DspBlockComponent block, OutputModel source);
	}

	private final MetaInputFactory defInputFactory = new MetaInputFactory() {
		@Override
		public InputModel createInputModel(DspBlockComponent block, InputModel source) {
			if (source.isMetaRename()) {
				return source;
			}
			return null;

		}
	};
	private final MetaOutputFactory defOutputFactory = new MetaOutputFactory() {

		@Override
		public OutputModel createOutputModel(DspBlockComponent block, OutputModel source) {
			if (source.isMetaRename()) {
				return source;
			}
			return null;
		}

	};
	private final MetaInputFactory cmi;
	private final MetaOutputFactory cmo;

	public XmlExportMeta(List<DspBlockComponent> blocks, List<DspConnection> connections) throws ParserConfigurationException {
		super(blocks, connections);
		this.cmi = defInputFactory;
		this.cmo = defOutputFactory;
		createMetaExport();
	}

	public XmlExportMeta(List<DspBlockComponent> blocks, List<DspConnection> connections, MetaInputFactory cmi, MetaOutputFactory cmo)
			throws ParserConfigurationException {
		super(blocks, connections);
		this.cmi = cmi;
		this.cmo = cmo;
		createMetaExport();
	}

	public XmlExportMeta(DspPatchModel model, boolean skipUnconnected) throws ParserConfigurationException {
		this(model.getSelectedOrAllDspBlocks(), model.getSelectedOrAllDspConnections(), new MetaInOutFactory(skipUnconnected), new MetaInOutFactory(
				skipUnconnected));
	}

	private void createMetaExport() {
		Element meta = dom.createElement("meta");
		meta.setAttribute("type", "generic-meta-dspobject");
		root.insertBefore(meta, root.getFirstChild());

		Element eInputs = dom.createElement("inputs");
		meta.appendChild(eInputs);
		Element eOutputs = dom.createElement("outputs");
		meta.appendChild(eOutputs);

		Collections.sort(blocks, new Comparator<DspBlockComponent>() {
			@Override
			public int compare(DspBlockComponent o1, DspBlockComponent o2) {
				if (o1.getY() == o2.getY()) {
					return o1.getX() - o2.getX();
				} else {
					return o1.getY() - o2.getY();
				}
			}
		});

		for (DspBlockComponent block : blocks) {
			List<InputModel> metaInputs = new ArrayList<>();
			for (InputModel im : block.getModel().getInputs()) {
				if (cmi == defInputFactory || (!im.isConnected() || !im.getSource().getSoundBlockModel().component.isSelected())) {
					InputModel metaInput = cmi.createInputModel(block, im);
					if (metaInput != null) {
						Element eInput = dom.createElement("input");
						eInputs.appendChild(eInput);
						setAttributes(metaInput, eInput, metaInput.getMetaRename());

						metaInputs.add(metaInput);
					}
				}
			}

			List<OutputModel> metaOutputs = new ArrayList<>();
			for (OutputModel output : block.getModel().getOutputs()) {
				InputModel target = output.getTarget();
				if (cmo == defOutputFactory
						|| (target == null || (target.getInput() instanceof NullInput) || !target.getSoundBlockModel().component.isSelected())) {

					OutputModel metaOutput = cmo.createOutputModel(block, output);

					if (metaOutput != null) {
						Element eOutput = dom.createElement("output");
						eOutputs.appendChild(eOutput);
						setAttributes(metaOutput, eOutput, metaOutput.getMetaRename());

						metaOutputs.add(metaOutput);
					}
				}
			}
		}
	}
}
