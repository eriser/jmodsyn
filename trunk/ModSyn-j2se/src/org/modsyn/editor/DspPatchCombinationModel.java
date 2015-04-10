package org.modsyn.editor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.SwingPropertyChangeSupport;

import org.modsyn.Context;

/**
 * Model for a combined patch (i.e. the 'main' DspPatch and sub-models for used
 * MetaModels)
 * 
 * @author ed52874
 * 
 */
public class DspPatchCombinationModel {

	static final String EVENT_ADD_SUBMODEL = "add-submodel";
	static final String EVENT_CLEAR = "clear";

	private final PropertyChangeSupport pcs = new SwingPropertyChangeSupport(this, true);

	final List<DspPatchModel> subModels = new ArrayList<DspPatchModel>();
	final Map<String, List<DspPatchModel>> linkedSubModels = new HashMap<>();

	final DspPatchModel mainModel;

	public DspPatchCombinationModel(Context context) {
		this.mainModel = new DspPatchModel(context);
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

	/**
	 * Get the sub-models that belong to the same meta-patch
	 * 
	 * @param name
	 * @return
	 */
	public List<DspPatchModel> getLinkedSubModels(String name) {
		return linkedSubModels.get(name);
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
