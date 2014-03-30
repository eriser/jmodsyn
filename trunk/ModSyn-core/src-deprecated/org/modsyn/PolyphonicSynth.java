package org.modsyn;

import org.modsyn.util.Tools;

/**
 * Distributes polyphonic events across the available voices.
 * 
 * @author Erik Duijs
 */
public class PolyphonicSynth {

	public final Synth[] voices;
	VoiceStatus[] status;
	long timer;

	public PolyphonicSynth(Synth[] patch) {
		this.voices = patch;
		this.status = new VoiceStatus[patch.length];
		for (int i = 0; i < status.length; i++) {
			status[i] = new VoiceStatus();
		}
	}

	public void keyOn(int keyNr, float velo) {
		// System.out.println(keyNr);
		int v = -1;
		long firstTime = Long.MAX_VALUE;

		for (int i = 0; i < status.length; i++) {
			if (status[i].keyOn && status[i].keyNr == keyNr)
				return;
		}

		// first find the oldest inactive
		for (int i = 0; i < status.length; i++) {
			if (!status[i].keyOn) {
				if (status[i].time < firstTime) {
					firstTime = status[i].time;
					v = i;
				}
			}
		}

		if (v == -1) {
			// if all active, find oldest active
			for (int i = 0; i < status.length; i++) {
				if (status[i].keyOn) {
					if (status[i].time < firstTime) {
						firstTime = status[i].time;
						v = i;
					}
				}
			}
		}

		if (v >= 0) {
			// System.out.println(velo);
			status[v].keyOn(keyNr);
			voices[v].keyOn(Tools.getFreq(keyNr), velo);
			return;
		}

	}

	public void keyOff(int keyNr) {
		for (int i = 0; i < status.length; i++) {
			if (status[i].keyOn && status[i].keyNr == keyNr) {
				status[i].keyOff();
				voices[i].keyOff();
				return;
			}
		}
	}

	public void setControl(int control, float value) {
		for (int i = 0; i < voices.length; i++) {
			voices[i].getInput(control).set(value);
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
