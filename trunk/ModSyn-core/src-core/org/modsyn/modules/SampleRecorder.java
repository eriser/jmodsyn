/*
 * Created on Apr 23, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.modsyn.modules;

import java.util.ArrayList;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.util.AudioClip;

/**
 * @author edy
 * 
 * Records an audio signal.
 */
public class SampleRecorder implements SignalInput {

	private boolean recording = false;
	private ArrayList<Float> data = new ArrayList<Float>();
	private final Context context;
	

	public SampleRecorder(Context context) {
		this.context = context;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioInput#write(float[])
	 */
	@Override
	public void set(float data) {
		if (recording) {
			this.data.add(new Float(data));
		}
		
	}

	/**
	 * Empty buffer and start recording.
	 */
	public void start() {
		recording = true;
		data = new ArrayList<Float>();
	}

	/**
	 * Stop recording.
	 */
	public void stop() {
		recording = false;
	}

	/**
	 * Get the recorded audio signal.
	 * 
	 * @return recorded audio or <code>null</code> if no audio is recorded
	 */
	private float[] getData() {
		if (data == null)
			return null;
		float[] array = new float[data.size()];
		for (int i = 0; i < data.size(); i++) {
			Float f = data.get(i);
			array[i] = f.floatValue();
		}
		return array;
	}
	
	public AudioClip getAudioClip() {
		if (data.size() == 0) return null;
		AudioClip clip = new AudioClip(context.getSampleRate(), getData());
		
		return clip;
	}

}
