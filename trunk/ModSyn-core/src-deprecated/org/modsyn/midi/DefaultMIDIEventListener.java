package org.modsyn.midi;

import org.modsyn.PolyphonicSynth;

public class DefaultMIDIEventListener implements MIDIEventListener {
    
    PolyphonicSynth synth;
    
    public DefaultMIDIEventListener(PolyphonicSynth synth) {
        this.synth = synth;
    }

    public void play(EventMIDI midiEvent) {
        int status = midiEvent.getStatus();
        int type = status & 0xf0;
        if (type == 0x90) {
            if (midiEvent.data2 == 0) {
                synth.keyOff(midiEvent.data1);
            } else {
                synth.keyOn(midiEvent.data1, midiEvent.data2);
            }
        } else if (type == 0x80) {
            synth.keyOff(midiEvent.data1);
        }
    }
}
