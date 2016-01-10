package org.modsyn.editor.io;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.DspConnection;
import org.modsyn.editor.DspPatchModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.editor.blocks.FromAsioModel;
import org.modsyn.editor.blocks.FromJavaSoundModel;
import org.modsyn.editor.blocks.FromMidiModel;
import org.modsyn.editor.blocks.ToAsioModel;
import org.modsyn.editor.blocks.ToJavaSoundModel;
import org.modsyn.vst.ToVSTAudioModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlExport {

	protected final Document dom;
	protected final List<DspBlockComponent> blocks;
	protected final List<DspConnection> connections;
	protected final Element root;

	public XmlExport(List<DspBlockComponent> blocks, List<DspConnection> connections) throws ParserConfigurationException {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		final DocumentBuilder builder = factory.newDocumentBuilder();
		this.dom = builder.newDocument();
		this.blocks = new ArrayList<>(blocks);
		this.connections = new ArrayList<>(connections);
		this.root = dom.createElement("patch");

		dom.appendChild(root);

		buildDspObjects();
		buildConnections();
	}

	public XmlExport(DspPatchModel model) throws ParserConfigurationException {
		this(model.getDspBlocks(), model.getDspConnections());
	}

	protected void removeExternalDspObjectElements() {
		NodeList nl = root.getElementsByTagName("dspobject");
		for (int i = nl.getLength() - 1; i >= 0; i--) {
			Element eDspObject = (Element) nl.item(i);
			String type = eDspObject.getAttribute("type");
			if (type.equals(FromMidiModel.class.getName()) || type.equals(ToJavaSoundModel.class.getName()) || type.equals(ToAsioModel.class.getName())
					|| type.equals(ToVSTAudioModel.class.getName()) || type.equals(FromAsioModel.class.getName())
					|| type.equals(FromJavaSoundModel.class.getName())) {
				eDspObject.getParentNode().removeChild(eDspObject);
			}
		}
	}

	protected void removeConnectionWithId(int hashCode) {
		String id = Integer.toString(hashCode);

		NodeList nl = root.getElementsByTagName("connection");
		for (int i = nl.getLength() - 1; i >= 0; i--) {
			Element eConnection = (Element) nl.item(i);
			String fromId = eConnection.getAttribute("from-id");
			String toId = eConnection.getAttribute("to-id");
			if (toId.equals(id) || fromId.equals(id)) {
				eConnection.getParentNode().removeChild(eConnection);
			}
		}
	}

	protected DspBlockComponent findMidi() {
		for (DspBlockComponent block : blocks) {
			if (block.getModel() instanceof FromMidiModel) {
				return block;
			}
		}
		return null;
	}

	protected DspBlockComponent findAudio() {
		for (DspBlockComponent block : blocks) {
			if ((block.getModel() instanceof ToAsioModel) || (block.getModel() instanceof ToJavaSoundModel) || (block.getModel() instanceof ToVSTAudioModel)) {
				return block;
			}
		}
		return null;
	}

	private void buildDspObjects() {
		final Element dspobjects = dom.createElement("dspobjects");
		root.appendChild(dspobjects);

		for (DspBlockComponent dsp : blocks) {
			final Element eDsp = dom.createElement("dspobject");
			dspobjects.appendChild(eDsp);

			DspBlockModel<?> dspBlockModel = dsp.getModel();
			eDsp.setAttribute("name", dspBlockModel.getName());
			eDsp.setAttribute("type", dspBlockModel.getClass().getName());
			eDsp.setAttribute("id", Integer.toString(dspBlockModel.hashCode()));
			eDsp.setAttribute("bounds", dsp.getBounds().x + "," + dsp.getBounds().y + "," + dsp.getBounds().width + "," + dsp.getBounds().height);
			if (dspBlockModel.channels > 0) {
				eDsp.setAttribute("channels", Integer.toString(dspBlockModel.channels));
			}

			final Element eInputs = dom.createElement("inputs");
			eDsp.appendChild(eInputs);

			for (InputModel input : dspBlockModel.getInputs()) {
				final Element eInput = dom.createElement("input");
				eInputs.appendChild(eInput);

				setAttributes(input, eInput, input.getName());

			}

			final Element eOutputs = dom.createElement("outputs");
			eDsp.appendChild(eOutputs);

			for (OutputModel output : dspBlockModel.getOutputs()) {
				final Element eOutput = dom.createElement("output");
				eOutputs.appendChild(eOutput);

				setAttributes(output, eOutput, output.getName());

			}
		}
	}

	protected void setAttributes(OutputModel output, final Element eOutput, String name) {
		eOutput.setAttribute("name", name);
		eOutput.setAttribute("id", Integer.toString(output.hashCode()));
	}

	protected void setAttributes(InputModel input, final Element eInput, String name) {
		eInput.setAttribute("name", name);
		eInput.setAttribute("id", Integer.toString(input.hashCode()));
		eInput.setAttribute("value", Float.toString(input.getValue()));
		eInput.setAttribute("min", Float.toString(input.getMin()));
		eInput.setAttribute("max", Float.toString(input.getMax()));
		eInput.setAttribute("decimals", Integer.toString(input.getDecimals()));
	}

	private void buildConnections() {
		final Element eConnections = dom.createElement("connections");
		root.appendChild(eConnections);

		for (DspConnection conn : connections) {

			// don't export dangling connections (in case of saving a
			// sub-selection of the complete patch)
			if (blocks.contains(conn.from) && blocks.contains(conn.to)) {
				final Element eConn = dom.createElement("connection");
				eConnections.appendChild(eConn);

				eConn.setAttribute("from-id", Integer.toString(conn.fromSignal.hashCode()));
				eConn.setAttribute("to-id", Integer.toString(conn.toSignal.hashCode()));
			}
		}
	}

	@Override
	public String toString() {
		try {
			final TransformerFactory tf = TransformerFactory.newInstance();
			final Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			final StringWriter writer = new StringWriter();
			t.transform(new DOMSource(dom), new StreamResult(writer));
			return writer.getBuffer().toString();

		} catch (final Exception e) {
			return dom.toString();
		}
	}
}
