package org.modsyn;
/*
 * Created on Jun 16, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */


public interface Synth extends KeyboardListener {

	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	public SignalOutput getOutput(int channel);

	public SignalInput getInput(int index);
}
