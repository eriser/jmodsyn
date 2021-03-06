package org.modsyn.editor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.SwingPropertyChangeSupport;

import org.modsyn.Context;
import org.modsyn.editor.blocks.MetaModel;

/**
 * Model for a combined patch (i.e. the 'main' DspPatch and sub-models for used MetaModels)
 * 
 * @author ed52874
 * 
 */
public class DspPatchCombinationModel {

	static final String EVENT_ADD_SUBMODEL = "add-submodel";
	static final String EVENT_REMOVE_SUBMODEL = "remove-submodel";
	static final String EVENT_CLEAR = "clear";

	private final PropertyChangeSupport pcs = new SwingPropertyChangeSupport(this, true);

	final List<DspPatchModel> subModels = new ArrayList<DspPatchModel>();
	final Map<String, List<DspPatchModel>> linkedSubModels = new HashMap<>();

	final DspPatchModel mainModel;

	public DspPatchCombinationModel(Context context) {
		this.mainModel = new DspPatchModel(context, this);
	}

	public DspPatchModel getMainModel() {
		return mainModel;
	}

	public List<DspPatchModel> getSubModels() {
		return subModels;
	}

	public void addSubModel(DspPatchModel model) {
		if (model.isMainModel) {
			throw new IllegalArgumentException("Can't add a main model as sub model");
		}

		subModels.add(model);
		if (!linkedSubModels.containsKey(model.name)) {
			linkedSubModels.put(model.name, new ArrayList<DspPatchModel>());
		}
		linkedSubModels.get(model.name).add(model);
		pcs.firePropertyChange(EVENT_ADD_SUBMODEL, null, model);
	}

	public void removeSubModel(DspPatchModel model) {
		int idx = subModels.indexOf(model);
		subModels.remove(model);
		pcs.firePropertyChange(EVENT_REMOVE_SUBMODEL, idx, model);
	}

	/**
	 * Get the sub-models that belong to the same meta-patch
	 * 
	 * @param name
	 * @return
	 */
	public List<DspPatchModel> getLinkedSubModels(String name) {
		return linkedSubModels.get(name);
	}

	public void renameLinkedSubModels(String name, String newName) {
		if (newName.endsWith(".dsp-patch")) {
			newName = newName.substring(0, newName.length() - ".dsp-patch".length());
		}

		for (DspPatchModel lpm : getLinkedSubModels(name)) {
			lpm.name = newName;
			for (DspBlockComponent block : lpm.parent.mainModel.dspBlocks) {
				DspBlockModel<?> model = block.getModel();
				System.out.println(model);
				if (model instanceof MetaModel && model.getName().equals(name)) {
					((MetaModel) model).setName(newName);
					block.refresh();
				}
			}
		}

	}

	public List<DspPatchModel> getLinkedSubModels() {
		List<DspPatchModel> result = new ArrayList<>();

		for (List<DspPatchModel> l : linkedSubModels.values()) {
			if (l.size() > 0) {
				result.add(l.get(0));
			}
		}

		return result;
	}

	public List<DspBlockComponent> getLinkedBlockComponents(DspBlockComponent dbc) {
		List<DspBlockComponent> result = new ArrayList<>();

		if (subModels.size() > 0) {
			DspPatchModel model = null;
			for (int i = 0; i < subModels.size() && model == null; i++) {
				if (subModels.get(i).getDspBlocks().contains(dbc)) {
					model = subModels.get(i);
				}
			}

			if (model != null) {
				int idx = model.getDspBlocks().indexOf(dbc);
				for (DspPatchModel m : linkedSubModels.get(model.name)) {
					result.add(m.getDspBlocks().get(idx));
				}
			} else {
				result.add(dbc);
			}
		}

		return result;
	}

	public List<DspConnection> getLinkedConnections(DspConnection c) {
		List<DspConnection> result = new ArrayList<>();

		if (subModels.size() > 0) {
			DspPatchModel model = null;
			for (int i = 0; i < subModels.size() && model == null; i++) {
				if (subModels.get(i).getDspConnections().contains(c)) {
					model = subModels.get(i);
				}
			}

			if (model != null) {
				int idx = model.getDspConnections().indexOf(c);
				for (DspPatchModel m : linkedSubModels.get(model.name)) {
					result.add(m.getDspConnections().get(idx));
				}
			} else {
				result.add(c);
			}
		}

		return result;
	}

	public void addListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void clear() {
		mainModel.clear();
		for (DspPatchModel m : subModels) {
			m.clear();
		}
		subModels.clear();
		linkedSubModels.clear();

		pcs.firePropertyChange(EVENT_CLEAR, null, this);
	}

}
