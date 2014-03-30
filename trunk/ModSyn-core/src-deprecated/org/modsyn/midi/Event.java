/*
 * Created on 19-jun-07
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
public class Event {
	
	int type;
	long offset;
	
	public Event(long offset, int type) {
		this.offset = offset;
		this.type = type;
	}
	
	public long getOffset() {
		return offset;
	}
	
	public int getType() {
		return type;
	}

}
