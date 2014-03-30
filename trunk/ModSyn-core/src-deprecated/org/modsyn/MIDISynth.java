/*
 * Created on Jun 16, 2007
 */
package org.modsyn;

public interface MIDISynth {

	public static final int OUTPUT_LEFT = 0;
	public static final int OUTPUT_RIGHT = 1;

	public void keyOn(float freq, float velo);

	public void keyOff();

	public SignalOutput getOutput(int channel);

	public SignalInput getInput(int index);
}
