package org.modsyn.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.event.SwingPropertyChangeSupport;

import org.modsyn.SignalInput;

public class InputModel {
	protected final PropertyChangeEvent INPUT_CHANGED_EVENT = new PropertyChangeEvent(this, "CHANGED_EVENT", false, true);
	protected final PropertyChangeSupport pcs = new SwingPropertyChangeSupport(this, true);

	private final DspBlockModel<?> soundBlockModel;
	private final SignalInput input;
	private OutputModel source;
	private float value;
	private float min;
	private float max;
	private final String name;
	private int decimals;

	public InputModel(DspBlockModel<?> soundBlockModel, SignalInput input, String name, float value, float min, float max) {
		this.soundBlockModel = soundBlockModel;
		this.input = input;
		this.source = null;
		this.name = name;
		this.value = value;
		this.min = min;
		this.max = max;

		int dmin = 5 - Integer.toString((int) min).length();
		int dmax = 5 - Integer.toString((int) max).length();
		this.decimals = Math.max(2, Math.min(dmin, dmax));

		input.set(value);
	}

	public InputModel(DspBlockModel<?> soundBlockModel, SignalInput input, String name, float value, float min, float max, int decimals) {
		this.soundBlockModel = soundBlockModel;
		this.input = input;
		this.source = null;
		this.name = name;
		this.value = value;
		this.min = min;
		this.max = max;
		this.decimals = decimals;

		input.set(value);
	}

	public SignalInput getInput() {
		return input;
	}

	public boolean isConnected() {
		return source != null;
	}

	public void disconnect() {
		connectFrom(null);
	}

	public OutputModel getSource() {
		return source;
	}

	public void connectFrom(OutputModel source) {
		this.source = source;
		if (source == null) {
			input.set(value);
		}
		fireChangeEvent();
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
		input.set(value);
		fireChangeEvent();
	}

	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}

	public void setMin(float min) {
		this.min = min;
		fireChangeEvent();
	}

	public void setMax(float max) {
		this.max = max;
		fireChangeEvent();
	}

	public int getDecimals() {
		return decimals;
	}

	public void setDecimals(int decimals) {
		this.decimals = decimals;
		fireChangeEvent();
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
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
