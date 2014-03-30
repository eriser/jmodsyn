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
public class EventMETA extends Event {
	
	public static final int TYPE_TEMPO = 0x51;
	public static final int TYPE_SMPTE_OFFSET = 0x54;
	public static final int TYPE_TIME_SIGNATURE = 0x58;
	
	
	int[] data;

	/**
	 * @param offset
	 * @param type
	 */
	public EventMETA(long offset, int type, int[] data) {
		super(offset, type);
		this.data = data;
	}
	
	public int[] getData() {
		return data;
	}

}
