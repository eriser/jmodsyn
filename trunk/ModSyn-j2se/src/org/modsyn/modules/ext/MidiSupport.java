package org.modsyn.modules.ext;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public enum MidiSupport {

	INSTANCE;

	private Transmitter transmitter;
	private final MidiListener[] listeners = new MidiListener[16];

	private MidiSupport() {
		System.out.println("Initializing MIDI...");
		try {
			MidiSystem.getTransmitter().setReceiver(new MidiReceiver());
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setListener(int channel, MidiListener listener) {
		listeners[channel] = listener;
	}

	public MidiListener getListener(int channel) {
		return listeners[channel];
	}

	public void removeListeners() {
		for (int i = 0; i < listeners.length; i++) {
			listeners[i] = null;
		}
	}

	public void close() {
		transmitter.close();
	}

	private final class MidiReceiver implements Receiver {

		@Override
		public void close() {
			// TODO Auto-generated method stub

		}

		@Override
		public void send(MidiMessage m, long arg1) {

			byte[] bytes = m.getMessage();

			int status = bytes[0] & 0xff;
			int type = status & 0xf0;
			int channel = status & 0x0f;

			// System.out.println("Status: " + status + " " + channel);

			MidiListener listener = listeners[channel + 1];
			if (listener == null || type == 0xf0) {
				// no listener or SYSEX message
				return;
			}

			int b1 = bytes[1] & 0xff;
			int b2 = bytes[2] & 0xff;

			switch (type) {
			case 0x80: // key off
				listener.keyOff(b1, b2);
				break;
			case 0x90: // key on
				if (bytes[2] == 0) {
					listener.keyOff(b1, b2);
				} else {
					listener.keyOn(b1, b2);
				}
				break;
			case 0xb0: // control change / channel mode msg
				listener.controlChange(b1, b2);
				break;
			case 0xe0: // pitch bend
				int val = (b1 | (b2 << 7)) - 0x2000;
				listener.pitchBend(val);
				break;
			}

		}

	}
}
