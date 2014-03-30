package org.modsyn.editor.io;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.modsyn.NullInput;
import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.DspConnection;
import org.modsyn.editor.DspPatchModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.w3c.dom.Element;

public class XmlExportMidi extends XmlExport {

	public XmlExportMidi(List<DspBlockComponent> blocks, List<DspConnection> connections) throws ParserConfigurationException {
		super(blocks, connections);
		createMidiExport();
	}

	public XmlExportMidi(DspPatchModel model) throws ParserConfigurationException {
		this(model.getDspBlocks(), model.getDspConnections());
	}

	/**
	 * Add meta information for MIDI export, remove MIDI/Audio components.
	 */
	private void createMidiExport() {
		Element meta = dom.createElement("meta");
		meta.setAttribute("type", "midi");
		root.appendChild(meta);

		DspBlockComponent midiComponent = findMidi();
		if (midiComponent == null) {
			throw new IllegalArgumentException("No MIDI IN component, cannot create MIDI export");
		}

		DspBlockComponent audioComponent = findAudio();
		if (audioComponent == null) {
			throw new IllegalArgumentException("No AUDIO OUT component, cannot create MIDI export");
		}

		blocks.remove(midiComponent);
		blocks.remove(audioComponent);

		removeExternalDspObjectElements();

		Element midiIn = dom.createElement("midi-interface");
		meta.appendChild(midiIn);
		for (OutputModel output : midiComponent.getModel().getOutputs()) {
			InputModel target = output.getTarget();
			if (target != null && !(target.getInput() instanceof NullInput)) {
				Element eInput = dom.createElement("input");
				midiIn.appendChild(eInput);
				setAttributes(target, eInput, output.getName());
				removeConnectionWithId(target.hashCode());
			}
		}

		Element audioOut = dom.createElement("audio-interface");
		meta.appendChild(audioOut);
		for (InputModel input : audioComponent.getModel().getInputs()) {
			OutputModel output = input.getSource();
			if (output != null) {
				Element eOutput = dom.createElement("output");
				audioOut.appendChild(eOutput);

				setAttributes(output, eOutput, input.getName().replace("IN", "OUT"));
				removeConnectionWithId(output.hashCode());
			}
		}
	}
}
