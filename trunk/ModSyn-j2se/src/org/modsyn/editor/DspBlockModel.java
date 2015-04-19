package org.modsyn.editor;

import java.util.ArrayList;
import java.util.List;

import org.modsyn.DspObject;

/**
 * A DspBlockModel wraps a DspObject. It is used as the model for visual representations of a DspObject and its inputs
 * and outputs, and for XML import/export.
 * 
 * @author Erik Duijs
 * 
 * @param <T>
 */
public abstract class DspBlockModel<T extends DspObject> {

	protected final List<InputModel> inputs = new ArrayList<InputModel>();
	protected final List<OutputModel> outputs = new ArrayList<OutputModel>();
	private final T dsp;
	private final String name;
	private boolean fixedSize;

	public final int channels;

	/*
	 * FIXME: this reference to a DspBlockComponent is a design flaw that should be fixed. Ideally, all classes that
	 * extend DspBlockModel should be moved to ModSyn-core (which does not depend on J2SE).
	 */
	public DspBlockComponent component;

	private boolean isSubModel;

	public DspBlockModel(T dsp) {
		this(dsp, -1);
	}

	public DspBlockModel(T dsp, int channels) {
		this.dsp = dsp;
		this.channels = channels;
		name = dsp.getClass().getSimpleName();
	}

	public String getName() {
		return name;
	}

	public T getDspObject() {
		return dsp;
	}

	public void setSubModel(boolean b) {
		this.isSubModel = b;
	}

	public boolean isSubModel() {
		return isSubModel;
	}

	protected void add(InputModel input) {
		inputs.add(input);
	}

	protected void add(OutputModel output) {
		outputs.add(output);
	}

	public List<InputModel> getInputs() {
		return inputs;
	}

	public List<OutputModel> getOutputs() {
		return outputs;
	}

	public boolean isFixedSize() {
		return fixedSize;
	}

	public void setFixedSize() {
		this.fixedSize = true;
	}
}
