package org.modsyn;

public class SignalInsertValue extends DefaultSignalOutput implements SignalInsert {

	public float value;

	public SignalInsertValue(float def) {
		value = def;
	}

	public SignalInsertValue() {
		value = 0;
	}

	@Override
	public void set(float signal) {
		value = signal;
		connectedInput.set(signal);
	}
}
