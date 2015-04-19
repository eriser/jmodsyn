package org.modsyn;

import java.util.ArrayList;

/**
 * A MetaDspObject is a collection of other DspObjects.
 * 
 * @author Erik Duijs
 */
public class MetaDspObject extends ArrayList<DspObject> implements DspObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4823256299101276468L;

	@Override
	public boolean add(DspObject dsp) {
		if (dsp == this) {
			throw new IllegalArgumentException("Cannot add a MetaDspObject to itself");
		}
		return super.add(dsp);
	}
}
