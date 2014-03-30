/*
 * Created on Apr 26, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.util;

/**
 * @author Erik Duijs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Interpolator {
	
	private float start;
	private float step;
	private int steps;
	
	public Interpolator(int steps) {
		this.steps = steps;
	}
	
	public void init(float start, float end) {
		this.start = start;
		this.step = (end - start) / steps;
		//System.out.println("Interpolator.init(" + start + ", " + end + ");");
	}
	
	public float getValue(int offset) {
		//System.out.println("Interpolator.getValue(" + offset + ") = " + (start + offset * step) + ";");
		return start + offset * step;
	}

}
