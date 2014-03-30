/*
 * Created on 20-jun-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.midi;

/**
 * @author DU1381
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EventSYSEX extends Event {

	int[] data;

	public EventSYSEX(long offset, int type, int[] data) {
		super(offset, type);
		this.data = data;
	}
	
	public int[] getData() {
		return data;
	}

}
