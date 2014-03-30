/*
 * Created on 19-jun-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.midi;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author DU1381
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MTrk {

	static final boolean TRACE = false;

	BufferedInputStream bis;
	int len;

	ArrayList<Event> events;
	int deltaTime;

	long offset;

	EventMIDI em;

	/**
	 * @param bis
	 * @param len
	 */
	public MTrk(BufferedInputStream bis, int len) {
		this.bis = bis;
		this.len = len;
		this.events = new ArrayList<Event>();
		this.offset = 0;
		try {
			read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	private void read() throws IOException {

		do {
			deltaTime = readVarLen();
			offset += deltaTime;
			if (TRACE)
				System.out.println(" delta time: " + deltaTime);

			int value = bis.read();
			len--;

			switch (value) {
				case 0xff :
					readMETA();
					break;
				default :
					readMIDI(value);
					break;
			}
		} while (len > 0);
	}

	/**
	 * 
	 */
	private void readMIDI(int event) throws IOException {
		boolean runningStatus = false;
		int _event = event;
		if (event < 0x80) {
			if (TRACE) {
				System.out.println("  (running status...)");
			}
			runningStatus = true;
			//event = ((EventMIDI) events.get(events.size() - 1)).type;
			event = em.type;
		}
		if (TRACE)
			System.out.println(" MIDI event " + Integer.toHexString(event));


		switch (event & 0xf0) {
			case 0x80 : // note off
			case 0x90 : // note on
			case 0xa0 : // poly aftertouch
			case 0xb0 : // control change
			case 0xe0 : // pitch wheel change
				if (runningStatus) {
					em = new EventMIDI(offset, event, _event, bis.read());
					len -= 1;
				} else {
					em = new EventMIDI(offset, event, bis.read(), bis.read());
					len -= 2;
				}
				events.add(em);
				if (TRACE)
					System.out.println(
						"  data: " + Integer.toHexString(em.data1) + "," + Integer.toHexString(em.data2));
				break;
				
			case 0xc0 : // prog change
			case 0xd0 : // channel aftertouch
				if (runningStatus) {
					em = new EventMIDI(offset, event, _event, -1);
				} else {
					em = new EventMIDI(offset, event, bis.read(), -1);
					len--;
				}
				events.add(em);
				if (TRACE)
					System.out.println("  data: " + Integer.toHexString(em.data1));
				break;
				
			case 0xf0: // sysex
				int[] data = new int[readVarLen()];
				for (int i = 0; i < data.length; i++) {
					data[i] = bis.read();
					len--;
				}
				int f7 = bis.read();
				len--;
				if (f7 != 0xf7) {
					throw new RuntimeException("Sysex message doesn't end with $f7 but with $" + Integer.toHexString(f7));
				}
				events.add(new EventSYSEX(offset, event, data));
				break;
				
			default :
				System.out.println(" ***WARNING*** unknown MIDI event " + Integer.toHexString(event));
				break;
		}
	}
	/**
	 * 
	 */
	private void readMETA() throws IOException {
		int type = bis.read();
		len--;
		int length = readVarLen();
		if (TRACE)
			System.out.println(" META event " + Integer.toHexString(type) + ", len=" + length);

		int[] data = new int[length];
		for (int i = 0; i < length; i++) {
			data[i] = bis.read();
			len--;
		}
		events.add(new EventMETA(offset, type, data));

		/*switch (type) {
			case 0x51 :
				int value = (bis.read() << 16) | (bis.read() << 8) | bis.read();
				len -= 3;
				if (TRACE)
					System.out.println("  Tempo " + value + " ms/q-note");
				break;
			case 0x54 :
				int hr = bis.read();
				int mn = bis.read();
				int se = bis.read();
				int fr = bis.read();
				int ff = bis.read();
				len -= 5;
				if (TRACE)
					System.out.println("  SMPTE offset " + hr + ":" + mn + ":" + se + ":" + fr + ":" + ff);
				break;
			case 0x58 :
				int nn = bis.read();
				int dd = bis.read();
				int cc = bis.read();
				int bb = bis.read();
				dd *= dd;
				len -= 4;
				if (TRACE)
					System.out.println("  Time signature " + nn + "/" + dd + " " + cc + " " + bb);
				break;
			default :
				while (length-- > 0) {
					bis.read();
					len--;
				}
				break;
		}*/
	}
	private int readVarLen() throws IOException {
		len--;
		int value, c;
		if (((value = bis.read()) & 0x80) != 0) {
			value &= 0x7f;
			do {
				value = (value << 7) + ((c = bis.read()) & 0x7f);
				len--;
			} while ((c & 0x80) != 0);
		}
		return (value);
	}

}
