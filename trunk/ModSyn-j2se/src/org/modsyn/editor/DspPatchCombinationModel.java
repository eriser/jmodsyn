package org.modsyn.editor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

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
		pcs.firePropertyChange(EVENT_ADD_SUBMODEL, null, model);
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

		pcs.firePropertyChange(EVENT_CLEAR, null, this);
	}
}
