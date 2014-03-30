/*
 * Created on 3-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInsert;

/**
 * A Wire is the simplest SignalInsert possible: As the name suggests it simply
 * connects a SignalOutput to a SignalInput. Not meant to be useful; it's just
 * an example.
 * 
 * @author Erik Duijs
 */
public class Wire extends DefaultSignalOutput implements SignalInsert {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.SignalInput#write(float)
	 */
	@Override
	public void set(float sample) {
		connectedInput.set(sample);
	}
}
