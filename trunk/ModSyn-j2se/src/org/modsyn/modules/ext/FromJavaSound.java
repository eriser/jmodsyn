/*
 * Created on Apr 27, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.modsyn.modules.ext;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.modsyn.Context;
import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;

/**
 * @author Erik Duijs
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class FromJavaSound implements SignalSource {
	private final AudioInputStream audioInputStream;
	private final TargetDataLine targetLine;
	// private boolean running = false;

	private SignalInput connectedDevice = NullInput.INSTANCE;

	byte[] buffer;
	float[] fbuffer2;
	int fbuffer2Index;
	float[] fbuffer1;
	int fbuffer1Index;

	/**
	 * Starts the recording.
	 */
	public void start() {
		// running = true;
		targetLine.start();
		System.out.println("SoundGrabber inited");
	}

	public void stop() {
		// targetLine.drain();
		targetLine.stop();
		targetLine.close();
	}

	/**
	 *  
	 */
	public FromJavaSound(Context context, int readBufferSize, int soundBufferSize) {

		buffer = new byte[readBufferSize * 2];
		fbuffer1 = new float[readBufferSize];
		fbuffer2 = new float[readBufferSize];

		AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, context.getSampleRate(), 16, 1,// Sys.channels,
				/* Sys.channels */1 * 2, context.getSampleRate(), false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
		TargetDataLine targetDataLine = null;

		try {

			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
			targetDataLine.open(audioFormat, soundBufferSize/* * Sys.channels */);
		} catch (LineUnavailableException e) {

			System.err.println("unable to get a recording line");
			e.printStackTrace();
			System.exit(1);
		}

		this.targetLine = targetDataLine;

		audioInputStream = new AudioInputStream(targetLine);

		context.addSignalSource(this);
		start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioGenerator#updateAudio()
	 */
	@Override
	public void updateSignal() {
		// System.out.println("UpdateAudio();");
		try {
			// System.out.println("UpdateAudio();");

			if (fbuffer1Index == 0) {
				audioInputStream.read(buffer, 0, buffer.length);
				for (int i = 0; i < fbuffer1.length; i++) {
					int ii = i << 1; // Sys.channels;

					fbuffer1[i] = ((buffer[ii + 1] << 8) + ((buffer[ii] + 256) & 0xff)) / 32768f;

					// if (Sys.channels == 2) {
					// fbuffer1[i] = (int)(buffer[ii + 2] & 255) + buffer[ii +
					// 3] << 8;
					// }
				}
			}
			connectedDevice.set(fbuffer1[fbuffer1Index++]);
			fbuffer1Index = fbuffer1Index % fbuffer1.length;
			// System.out.println("UpdateAudio();");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioOutput#connectTo(org.modsyn.AudioInput)
	 */
	@Override
	public void connectTo(SignalInput input) {
		connectedDevice = input;
	}
}
