package org.modsyn.editor.io;

import java.awt.Rectangle;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.modsyn.Context;
import org.modsyn.MetaDspObject;
import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.DspBlockComponentFactory;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.DspConnection;
import org.modsyn.editor.DspPatchModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.editor.blocks.MetaModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlImportMeta {

	private final Document doc;
	public final DspBlockComponent importedMetaBlock;

	public XmlImportMeta(File f, Context c, DspPatchModel pm) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		this.doc = dBuilder.parse(f);
		doc.getDocumentElement().normalize();

		NodeList nlMeta = doc.getElementsByTagName("meta");
		if (nlMeta.getLength() == 0) {
			throw new IllegalArgumentException(f.getName() + " is not a meta-patch.");
		}
		Element eMeta = (Element) nlMeta.item(0);
		if (!eMeta.getAttribute("type").equals("generic-meta-dspobject")) {
			throw new IllegalArgumentException(f.getName() + " is not a meta-patch.");
		}

		String metaName = f.getName().replace(".dsp-patch", "");
		MetaDspObject metaDsp = new MetaDspObject();
		MetaModel metaModel = new MetaModel(metaDsp, metaName);

		Map<String, InputModel> mapInputs = new HashMap<String, InputModel>();
		Map<String, OutputModel> mapOutputs = new HashMap<String, OutputModel>();

		NodeList nlDspObjects = doc.getElementsByTagName("dspobject");
		for (int i = 0; i < nlDspObjects.getLength(); i++) {
			Element e = (Element) nlDspObjects.item(i);

			String type = e.getAttribute("type");
			String oname = e.getAttribute("name");
			String ochannels = e.getAttribute("channels");

			// legacy
			if (type.equals("org.modsyn.editor.blocks.SplitterModel")) {
				type = "org.modsyn.editor.blocks.MultiSplitterModel";
				ochannels = "2";
			}
			if (type.equals("org.modsyn.editor.blocks.FromMidi4Model")) {
				type = "org.modsyn.editor.blocks.FromMidiPolyModel";
				ochannels = "4";
			}

			int channels = -1;
			if (ochannels != null && ochannels.length() > 0) {
				channels = Integer.parseInt(ochannels);
			}

			DspBlockComponent dbc = DspBlockComponentFactory.create(type, oname, c, pm, channels);
			dbc.getModel().setSubModel(true);

			metaDsp.add(dbc.getModel().getDspObject());

			String[] b = e.getAttribute("bounds").split(",");
			Rectangle r = new Rectangle(Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2]), Integer.parseInt(b[3]));
			dbc.setBounds(r);

			pm.addDspComponent(dbc, r.x, r.y);

			NodeList nlInputs = e.getElementsByTagName("input");
			for (int j = 0; j < nlInputs.getLength(); j++) {
				Element eInput = (Element) nlInputs.item(j);
				String id = eInput.getAttribute("id");
				String name = eInput.getAttribute("name");
				float value = Float.parseFloat(eInput.getAttribute("value"));

				String sMin = eInput.getAttribute("min");
				String sMax = eInput.getAttribute("max");
				String sDec = eInput.getAttribute("decimals");

				for (InputModel inputModel : dbc.getModel().getInputs()) {
					if (inputModel.getName().equals(name)) {
						inputModel.setValue(value);
						if (!"".equals(sMin)) {
							inputModel.setMin(Float.parseFloat(sMin));
						}
						if (!"".equals(sMax)) {
							inputModel.setMax(Float.parseFloat(sMax));
						}
						if (!"".equals(sDec)) {
							inputModel.setDecimals(Integer.parseInt(sDec));
						}
						mapInputs.put(id, inputModel);
						break;
					}
				}
			}

			NodeList nlOutputs = e.getElementsByTagName("output");
			for (int j = 0; j < nlOutputs.getLength(); j++) {
				Element eOutput = (Element) nlOutputs.item(j);
				String id = eOutput.getAttribute("id");
				String name = eOutput.getAttribute("name");

				for (OutputModel outputModel : dbc.getModel().getOutputs()) {
					if (outputModel.getName().equals(name)) {
						mapOutputs.put(id, outputModel);
						break;
					}
				}
			}
		}
		NodeList nlConnections = doc.getElementsByTagName("connection");
		for (int i = 0; i < nlConnections.getLength(); i++) {
			Element e = (Element) nlConnections.item(i);
			String fromId = e.getAttribute("from-id");
			String toId = e.getAttribute("to-id");

			try {
				DspBlockModel<?> from = mapOutputs.get(fromId).getSoundBlockModel();
				DspBlockModel<?> to = mapInputs.get(toId).getSoundBlockModel();

				pm.addDspConnection(new DspConnection(from.component, from.getOutputs().indexOf(mapOutputs.get(fromId)), to.component, to.getInputs().indexOf(
						mapInputs.get(toId))));
			} catch (NullPointerException npe) {
				// ignore -> dangling connection
			}
		}

		NodeList nlInputs = eMeta.getElementsByTagName("input");
		for (int i = 0; i < nlInputs.getLength(); i++) {
			Element e = (Element) nlInputs.item(i);
			String id = e.getAttribute("id");
			String name = e.getAttribute("name");
			float value = Float.parseFloat(e.getAttribute("value"));

			float min = Float.parseFloat(e.getAttribute("min"));
			float max = Float.parseFloat(e.getAttribute("max"));
			int dec = Integer.parseInt(e.getAttribute("decimals"));

			InputModel target = mapInputs.get(id);

			metaModel.addMetaInput(new InputModel(metaModel, target.getInput(), name, value, min, max, dec));
		}
		NodeList nlOutputs = eMeta.getElementsByTagName("output");
		for (int i = 0; i < nlOutputs.getLength(); i++) {
			Element e = (Element) nlOutputs.item(i);

			String id = e.getAttribute("id");
			String name = e.getAttribute("name");

			OutputModel target = mapOutputs.get(id);
			metaModel.addMetaOutput(new OutputModel(metaModel, target.getOutput(), name));
		}

		this.importedMetaBlock = new DspBlockComponent(metaModel, pm, 0, 0, 100, -1);
		pm.addDspComponent(importedMetaBlock, 0, 0);
	}
}
