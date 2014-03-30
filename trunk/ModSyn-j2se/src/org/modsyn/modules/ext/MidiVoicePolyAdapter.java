package org.modsyn.modules.ext;

import java.util.ArrayList;
import java.util.List;

import org.modsyn.DspObject;
import org.modsyn.SignalInput;

public class MidiVoicePolyAdapter implements MidiListener, DspObject {

	public final SignalInput cBendRange = new SignalInput() {
		@Override
		public void set(float signal) {
			for (int i = 0; i < voices.size(); i++) {
				voices.get(i).cBendRange.set(signal);
			}
		}
	};

	public final SignalInput ctrl1nr = new SignalInput() {
		@Override
		public void set(float signal) {
			for (int i = 0; i < voices.size(); i++) {
				voices.get(i).ctrl1nr.set(signal);
			}
		}
	};

	public final List<MidiVoiceAdapter> voices = new ArrayList<>();
	private final List<VoiceStatus> status = new ArrayList<>();

	private long timer;

	@SuppressWarnings("unused")
	private final int c1nr = 16;

	int bendRange = 2;

	public MidiVoicePolyAdapter(int voices) {
		for (int i = 0; i < voices; i++) {
			addListener(new MidiVoiceAdapter());
		}
	}

	private void addListener(MidiVoiceAdapter listener) {
		if (!voices.contains(listener)) {
			voices.add(listener);
			status.add(new VoiceStatus());
		}
	}

	@Override
	public void keyOn(int key, int velo) {
		int v = -1;
		long firstTime = Long.MAX_VALUE;

		for (int i = 0; i < status.size(); i++) {
			if (status.get(i).keyOn && status.get(i).keyNr == key)
				return;
		}

		// first find the oldest inactive
		for (int i = 0; i < status.size(); i++) {
			if (!status.get(i).keyOn) {
				if (status.get(i).time < firstTime) {
					firstTime = status.get(i).time;
					v = i;
				}
			}
		}

		if (v == -1) {
			// if all active, find oldest active
			for (int i = 0; i < status.size(); i++) {
				if (status.get(i).keyOn) {
					if (status.get(i).time < firstTime) {
						firstTime = status.get(i).time;
						v = i;
					}
				}
			}
		}

		if (v >= 0) {
			// System.out.println(velo);
			status.get(v).keyOn(key);
			voices.get(v).keyOn(key, velo);
			return;
		}

	}

	@Override
	public void keyOff(int key, int velo) {
		for (int i = 0; i < status.size(); i++) {
			if (status.get(i).keyOn && status.get(i).keyNr == key) {
				status.get(i).keyOff();
				voices.get(i).keyOff(key, 0);
				return;
			}
		}
	}

	@Override
	public void controlChange(int control, int value) {
		for (int i = 0; i < voices.size(); i++) {
			voices.get(i).controlChange(control, value);
		}
	}

	@Override
	public void pitchBend(int val) {
		for (int i = 0; i < voices.size(); i++) {
			voices.get(i).pitchBend(val);
		}
	}

	class VoiceStatus {
		boolean keyOn;
		int keyNr;
		long time;

		void keyOn(int key) {
			keyOn = true;
			keyNr = key;
			time = timer++;
		}

		void keyOff() {
			keyOn = false;
			time = timer++;
		}
	}
}
