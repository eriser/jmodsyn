/*
 * Created on 6-apr-2005
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.util.AudioClip;

/**
 * @author Erik Duijs
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class Recorder {

	public final SignalInput inputL;
	public final SignalInput inputR;

	int channels;
	private final Context context;

	public Recorder(Context context, int channels) {
		this.context = context;
		
		inputL = new SampleRecorder(context);
		if (channels >= 2) {
			inputR = new SampleRecorder(context);
		} else {
			inputR = null;
		}
	}

	public AudioClip createAudioClip() {
		switch (channels) {
		case 1:
			return new AudioClip(context.getSampleRate(),
					((SampleRecorder) inputL).getAudioClip().getDataLeft());
		case 2:
			return new AudioClip(context.getSampleRate(), ((SampleRecorder) inputL)
					.getAudioClip().getDataLeft(), ((SampleRecorder) inputR)
					.getAudioClip().getDataLeft());
		default:
			return null;
		}
	}

}
