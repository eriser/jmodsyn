package org.modsyn.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.SwingPropertyChangeSupport;

import org.modsyn.Context;
import org.modsyn.DspObject;
import org.modsyn.MetaDspObject;
import org.modsyn.SignalSource;
import org.modsyn.editor.blocks.MetaModel;
import org.modsyn.modules.ext.FromJavaSound;
import org.modsyn.modules.ext.ToJavaSound;

public class DspPatchModel {
	public static final String EVENT_ADD_BLOCK = "add-block";
	public static final String EVENT_ADD_CONNECTION = "add-connection";
	public static final String EVENT_REMOVE_BLOCK = "remove-block";
	public static final String EVENT_REMOVE_ALL = "remove-all";
	public static final String EVENT_REMOVE_CONNECTION = "remove-connection";
	public static final String EVENT_SELECT_INPUT = "select-input";
	public static final String EVENT_SELECT_SINGLE_COMPONENT = "select-single-component";
	public static final String EVENT_SELECTION_CHANGED = "selection-changed";

	protected final PropertyChangeEvent CHANGED_EVENT = new PropertyChangeEvent(this, "CHANGED_EVENT", false, true);
	protected final PropertyChangeSupport pcs = new SwingPropertyChangeSupport(this, true);

	final List<DspBlockComponent> dspBlocks = new ArrayList<DspBlockComponent>();
	final List<DspConnection> dspConnections = new ArrayList<DspConnection>();
	private final Context context;

	public DspPatchModel(Context context) {
		this.context = context;
	}

	public List<DspBlockComponent> getDspBlocks() {
		return dspBlocks;
	}

	public List<DspConnection> getDspConnections() {
		return dspConnections;
	}

	/**
	 * Get either the selected DspBlockComponents, or all DspBlockComponents if there is no selection.
	 * 
	 * @return
	 */
	public List<DspBlockComponent> getSelectedOrAllDspBlocks() {
		List<DspBlockComponent> result = new ArrayList<>();
		for (DspBlockComponent b : dspBlocks) {
			if (b.isSelected()) {
				result.add(b);
			}
		}

		if (result.isEmpty()) {
			return dspBlocks;
		}
		return result;
	}

	/**
	 * Get either the connections of all selected DspComponents, or all connections if there is no selection.
	 * 
	 * @return
	 */
	public List<DspConnection> getSelectedOrAllDspConnections() {
		List<DspConnection> result = new ArrayList<>();
		for (DspConnection b : dspConnections) {
			if (b.from.isSelected() && b.to.isSelected()) {
				result.add(b);
			}
		}

		if (result.isEmpty()) {
			return dspConnections;
		}
		return result;
	}

	public void addDspComponent(final DspBlockComponent dspBlockComponent) {
		if (!dspBlockComponent.getModel().isSubModel()) {
			dspBlocks.add(dspBlockComponent);
			addListener(dspBlockComponent);
			dspBlockComponent.addCloseButtonListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					removeDspComponent(dspBlockComponent);
				}
			});
			pcs.firePropertyChange(EVENT_ADD_BLOCK, null, dspBlockComponent);
		}
	}

	public void removeDspComponent(DspBlockComponent dspBlockComponent) {
		checkRemove(dspBlockComponent);
		dspBlocks.remove(dspBlockComponent);
		for (int i = dspConnections.size() - 1; i >= 0; i--) {
			DspConnection dc = dspConnections.get(i);
			if (dc.from == dspBlockComponent || dc.to == dspBlockComponent) {
				removeDspConnection(dc);
			}
		}
		if (dspBlockComponent.getModel().getDspObject() instanceof SignalSource) {
			context.remove((SignalSource) dspBlockComponent.getModel().getDspObject());
		}

		if (dspBlockComponent.getModel() instanceof MetaModel) {
			MetaDspObject mm = (MetaDspObject) dspBlockComponent.getModel().getDspObject();
			for (DspObject dsp : mm) {
				if (dsp instanceof SignalSource) {
					context.remove((SignalSource) dsp);
				}
			}
		}

		for (DspBlockComponent dsp : dspBlocks) {
			if (dsp.getModel().getDspObject() instanceof ToJavaSound) {
				((ToJavaSound) dsp.getModel().getDspObject()).reset();
			}
		}
		pcs.firePropertyChange(EVENT_REMOVE_BLOCK, null, dspBlockComponent);
	}

	public void clear() {
		for (DspBlockComponent dspBlockComponent : dspBlocks) {

			if (dspBlockComponent.getModel().getDspObject() instanceof SignalSource) {
				context.remove((SignalSource) dspBlockComponent.getModel().getDspObject());
			}
			if (dspBlockComponent.getModel() instanceof MetaModel) {
				MetaDspObject mm = (MetaDspObject) dspBlockComponent.getModel().getDspObject();
				for (DspObject dsp : mm) {
					if (dsp instanceof SignalSource) {
						context.remove((SignalSource) dsp);
					}
				}
			}
			pcs.firePropertyChange(EVENT_REMOVE_BLOCK, null, dspBlockComponent);
			checkRemove(dspBlockComponent);
		}
		dspConnections.clear();
		dspBlocks.clear();
		context.clear();
		pcs.firePropertyChange(EVENT_REMOVE_ALL, null, this);
	}

	private void checkRemove(DspBlockComponent c) {
		Object o = c.getModel().getDspObject();
		if (o instanceof FromJavaSound) {
			((FromJavaSound) o).stop();
		} else if (o instanceof ToJavaSound) {
			((ToJavaSound) o).stop();
		}/*
		 * else if (o instanceof ToAsio) { ((ToAsio) o).stop(); }
		 */
	}

	public void removeDspConnection(OutputModel output) {
		DspConnection toRemove = null;
		for (DspConnection c : dspConnections) {
			if (output == c.fromSignal) {
				toRemove = c;
			}
		}
		if (toRemove != null) {
			removeDspConnection(toRemove);
		}
	}

	public void removeDspConnection(DspConnection connection) {
		connection.fromSignal.disconnect();
		dspConnections.remove(connection);
		pcs.firePropertyChange(EVENT_REMOVE_CONNECTION, null, connection);
	}

	public void addDspConnection(DspConnection connection) {
		if (!connection.isInMeta()) {
			checkToRemove(connection);
			dspConnections.add(connection);
		}
		connection.fromSignal.connectTo(connection.toSignal);
		pcs.firePropertyChange(EVENT_ADD_CONNECTION, null, connection);
	}

	private void checkToRemove(DspConnection newConnection) {
		DspConnection toRemove1 = null;
		DspConnection toRemove2 = null;
		for (DspConnection c : dspConnections) {
			if (newConnection.toSignal == c.toSignal) {
				toRemove1 = c;
			}
			if (newConnection.fromSignal == c.fromSignal) {
				toRemove2 = c;
			}
		}
		if (toRemove1 != null) {
			toRemove1.fromSignal.disconnect();
			dspConnections.remove(toRemove1);
		}
		if (toRemove2 != null) {
			toRemove2.fromSignal.disconnect();
			dspConnections.remove(toRemove2);
		}
	}

	public void addListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
		fireChangeEvent();
	}

	protected void fireChangeEvent() {
		pcs.firePropertyChange(CHANGED_EVENT);
	}

	public void selectInputValue(InputModel selectedValue) {
		pcs.firePropertyChange(EVENT_SELECT_INPUT, null, selectedValue);
	}

	public void selectSingleComponent(DspBlockComponent dspBlockComponent) {
		pcs.firePropertyChange(EVENT_SELECT_SINGLE_COMPONENT, null, dspBlockComponent);
	}

	public void selectionChanged(List<DspBlockComponent> selComponents) {
		pcs.firePropertyChange(EVENT_SELECTION_CHANGED, null, selComponents);
	}
}
