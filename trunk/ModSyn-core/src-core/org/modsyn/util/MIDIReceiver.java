/*
 * Created on 23-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.util;

import org.modsyn.PolyphonicSynth;

/**
 * @author DU1381
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MIDIReceiver {

	PolyphonicSynth synth;
	int channel;
	
	public MIDIReceiver(int channel, PolyphonicSynth synth) {
		this.channel = channel;
		this.synth = synth;
	}
}