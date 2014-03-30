package org.modsyn;

import java.util.ArrayList;

/**
 * A MetaDspObject is a collection of other DspObjects. It's your responsibility
 * to not register this to itself because that will lead to infinite recursion.
 * 
 * @author Erik Duijs
 */
public class MetaDspObject extends ArrayList<DspObject> implements DspObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4823256299101276468L;
}
