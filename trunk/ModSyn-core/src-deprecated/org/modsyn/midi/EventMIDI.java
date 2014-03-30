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
public class EventMIDI extends Event {

	int data1, data2, channel;
	
	/**
	 * @param offset
	 * @param type
	 */
	public EventMIDI(long offset, int type, int data1, int data2) {
		super(offset, type);
		this.channel = type & 0x0f;
		this.data1 = data1;
		this.data2 = data2;
		
		if (data1 >= 0x80 || data2 >= 0x80) {
			throw new RuntimeException("Invalid data byte(s) " + Integer.toHexString(data1) + ", " + Integer.toHexString(data2));
		}
	}
	
	public int getStatus() {
		return type;
	}
	
	public int getChannel() {
		return channel;
	}
	
	public int getData1() {
		return data1;
	}
	
	public int getData2() {
		return data2;
	}
    
    public String toString() {
        return "MIDI Event $" + Integer.toHexString(type) + ", " + data1 + ", " + data2;
    }

}
