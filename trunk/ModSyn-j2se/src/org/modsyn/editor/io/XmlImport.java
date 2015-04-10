package org.modsyn.editor.io;

import java.awt.Rectangle;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.modsyn.Context;
import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.DspConnection;
import org.modsyn.editor.DspPalette;
import org.modsyn.editor.DspPatchModel;
import org.modsyn.editor.InputModel;
import org.modsyn.editor.OutputModel;
import org.modsyn.editor.blocks.MetaModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlImport {

	private final Document doc;

	public XmlImport(File f, Context c, DspPatchModel pm) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		this.doc = dBuilder.parse(f);
		doc.getDocumentElement().normalize();

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
			// ../legacy

			int channels = -1;
			if (ochannels != null && ochannels.length() > 0) {
				channels = Integer.parseInt(ochannels);
			}

			DspBlockComponent dbc = create(type, oname, c, pm, channels);

			String[] b = e.getAttribute("bounds").split(",");
			Rectangle r = new Rectangle(Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2]), Integer.parseInt(b[3]));
			dbc.setBounds(r);

			if (!(dbc.getModel() instanceof MetaModel)) {
				// meta import already adds a meta model
				pm.addDspComponent(dbc);
			}

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
	}

	private DspBlockComponent create(String className, String name, Context c, DspPatchModel pm, int channels) {
		if (className.equals(MetaModel.class.getName())) {
			try {
				XmlImportMeta im = new XmlImportMeta(new File(FileSys.dirMeta, name + ".dsp-patch"), c, pm);
				return im.importedMetaBlock;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			return DspPalette.createFromModelName(className, c, pm, channels);
		}
	}
}
