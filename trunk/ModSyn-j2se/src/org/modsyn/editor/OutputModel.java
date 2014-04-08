package org.modsyn.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.SwingPropertyChangeSupport;

import org.modsyn.NullInput;
import org.modsyn.SignalOutput;

public class OutputModel {
	protected final PropertyChangeEvent INPUT_CHANGED_EVENT = new PropertyChangeEvent(this, "CHANGED_EVENT", false, true);
	protected final PropertyChangeSupport pcs = new SwingPropertyChangeSupport(this, true);

	private final DspBlockModel<?> soundBlockModel;
	private final SignalOutput output;
	private InputModel target;
	private String name;

	public OutputModel(DspBlockModel<?> soundBlockModel, SignalOutput output, String name) {
		super();
		this.soundBlockModel = soundBlockModel;
		this.output = output;
		this.name = name;
	}

	public void connectTo(InputModel input) {
		output.connectTo(input.getInput());
		input.connectFrom(this);
		target = input;
		fireChangeEvent();
	}

	public void disconnect() {
		output.connectTo(NullInput.INSTANCE);
		target.disconnect();
		target = null;
		fireChangeEvent();
	}

	@Override
	public String toString() {
		return name;
	}

	public InputModel getTarget() {
		return target;
	}

	public SignalOutput getOutput() {
		return output;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DspBlockModel<?> getSoundBlockModel() {
		return soundBlockModel;
	}

	public void addListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
		fireChangeEvent();
	}

	protected void fireChangeEvent() {
		pcs.firePropertyChange(INPUT_CHANGED_EVENT);
	}
}
