/*
 * Created on 20-jun-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.midi;

import java.util.ArrayList;

import org.modsyn.Context;

/**
 * @author DU1381
 *
 * Sample accurate MIDI player
 */
public class Player implements Runnable {
	
	private static final int DEFAULT_MICROSECS_PER_QN = 0x07A120; // = 120 BPM
	
	private MIDIFile midiFile;
	private MIDIEventListener[] channels;
	private ArrayList<Event> midiEvents;
	
	private double microsPerSample;
	private double samplesPerTick;
	private int sampleFreq;
	
	private boolean playing;

	private Context context;

	public Player(Context context, MIDIFile midiFile, int sampFreq) {
		this.context = context;
		this.midiFile = midiFile;
		this.midiEvents = midiFile.getAllOrderedEvents();
		this.channels = new MIDIEventListener[16];
		this.sampleFreq = sampFreq;
		this.microsPerSample = 1000000d / (double)sampleFreq;
		calcSamplesPerTick(DEFAULT_MICROSECS_PER_QN);
	}
	
	private void calcSamplesPerTick(int microSecondsPerQN) {
		double microSecondsPerTick = microSecondsPerQN / (double)midiFile.division;
		samplesPerTick =  microSecondsPerTick / (double)microsPerSample;
	}
	
	public void setEventListener(int channel, MIDIEventListener mel) {
		channels[channel] = mel;
	}
	
	public void setAllEventListeneres(MIDIEventListener mel) {
		for (int i = 0; i < 16; i++) {
			channels[i] = mel;
		}
	}
    
    public void stop() {
        playing = false;
    }
	
	public void run() {
		playing = true;
		int iEvent = 0;
		long tick = 0, nextTick = 0, lastTick = ((Event)midiEvents.get(midiEvents.size()-1)).getOffset();

        System.out.println("MIDI player start, lastTick=" + lastTick);

		while (playing) {
			
			// next event?
			if (tick >= nextTick) {
				Event event = (Event)midiEvents.get(iEvent++);
				nextTick = event.getOffset();
				
				fireEvent(event);
			}
			
			// play one tick
			for (int i = 0; i < samplesPerTick; i++) {
				context.update();
			}
			
			tick++;
			
			// end of sequence?
			if (tick >= lastTick) {
				playing = false;
			}
		}
        
        System.out.println("MIDI player finished");
	}

	/**
	 * @param event
	 */
	private void fireEvent(Event event) {
		
		if (event instanceof EventMIDI) {
			EventMIDI midi = (EventMIDI) event;
			
			int chn = midi.channel;
			if (channels[chn] != null) {
				channels[chn].play(midi);
			}
			
		} else if (event instanceof EventMETA) {
			EventMETA meta = (EventMETA) event;
			
			if (meta.getType() == EventMETA.TYPE_TEMPO) {
				int tempo = (meta.data[0] << 16) | (meta.data[1] << 8) | meta.data[2];
				calcSamplesPerTick(tempo);
			} else {
				// ignore all other META events
			}
			
		} else {
			// ignore SYSEX
		}
	}
}
