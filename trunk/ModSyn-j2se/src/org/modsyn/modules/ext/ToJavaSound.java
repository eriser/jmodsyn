/*
 * Created on Apr 25, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.modsyn.modules.ext;

import static java.lang.Math.abs;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;

import org.modsyn.Context;
import org.modsyn.DspObject;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;
import org.modsyn.util.ContextUpdateThread;

/**
 * @author Erik Duijs
 * 
 *         Steams its AudioInputs to JavaSound. If you set it up to mono, use
 *         inputL.
 */
public class ToJavaSound implements SignalSource, DspObject {

	public static final int MONO = 1;
	public static final int STEREO = 2;

	private AudioFormat format;
	private SourceDataLine line;

	private byte[] buffer;

	private float[] fbuffer1;
	private int fbuffer1Index = 0;
	private float[] fbuffer2;
	private int fbuffer2Index = 0;

	public SignalInput inputL;
	public SignalInput inputR;

	private float gain = 32767f;
	private final Context context;

	private ContextUpdateThread updater;

	public ToJavaSound(Context context, int channels, int soundBufferSize) {
		this.context = context;
		init(channels, soundBufferSize, 1);
	}

	public ToJavaSound(Context context, int channels, int soundBufferSize, int overSampling) {
		this.context = context;
		init(channels, soundBufferSize, overSampling);
	}

	private void init(int channels, int soundBufferSize, int overSampling) {
		// this.overSampling = overSampling;
		// this.channels = channels;
		inputL = new InputL();
		int bufferSize = soundBufferSize;
		fbuffer1 = new float[bufferSize * overSampling];

		if (channels == 2) {
			inputR = new InputR();
			fbuffer2 = new float[bufferSize * overSampling];
		} else {
			inputR = null;
		}

		Info[] minfo = AudioSystem.getMixerInfo();
		int idx = 1;
		for (int i = 0; i < minfo.length; i++) {
			System.out.println(minfo[i].getName());
			if (minfo[i].getName().toLowerCase().indexOf("java sound") >= 0) {
				idx = i;
			}
		}

		Mixer mixer = AudioSystem.getMixer(minfo[idx]);

		buffer = new byte[bufferSize * 2 * channels];
		// format = new AudioFormat(Sys.sampleRate, 16, 1, true, false);
		// DataLine.Info info = new DataLine.Info( SourceDataLine.class, format,
		// bufferSize * 2 * channels );
		format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, context.getSampleRate(), 16, channels, channels * 2, context.getSampleRate(), false);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format, soundBufferSize * channels);
		System.out.println("Getting Line");
		try {
			line = (SourceDataLine) mixer.getLine(info);
			System.out.println("Opening Line " + minfo[idx]);
			line.open(format, buffer.length);
			System.out.println("Starting Line");
			line.start();
			context.addMaster(this);
			System.out.println("Sound initialized successfully.");
			System.out.println("buffer    : " + line.getBufferSize());
			System.out.println("Sound initialized successfully.");

			updater = new ContextUpdateThread(context);
			updater.start();
		} catch (LineUnavailableException lue) {
			System.err.println("Unavailable data line");
		}

	}

	public void reset() {
		fbuffer1Index = fbuffer2Index = 0;
		for (int i = 0; i < fbuffer1.length; i++) {
			fbuffer1[i] = 0;
			if (fbuffer2 != null) {
				fbuffer2[i] = 0;
			}
		}
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.Input#in(byte[])
	 */
	@Override
	public void updateSignal() {
		// convert floats to bytes
		if (fbuffer1Index == 0) {
			if (inputR != null) {
				if (fbuffer2Index != 0) {
					// RESET the buffers if indices are not as expected.
					fbuffer2Index = 0;
					for (int i = 0; i < fbuffer2.length; i++) {
						fbuffer2[i] = 0;
					}
				} else {
					convertStereo();
				}
			} else {
				convertMono();
			}
			line.write(buffer, 0, buffer.length);
		}
	}

	private void convertMono() {
		for (int i = 0, ii = 0; i < fbuffer1.length; i++) {
			int sample = (int) (fbuffer1[i] * gain);
			float abs = abs(sample);
			if (abs > 32767) {
				sample -= (abs - sample);
			}
			buffer[ii++] = (byte) (sample & 255);
			buffer[ii++] = (byte) (sample >> 8);
		}
	}

	private void convertStereo() {
		for (int i = 0, ii = 0; i < fbuffer1.length; i++) {
			int sampleL = (int) (fbuffer1[i] * gain);
			int sampleR = (int) (fbuffer2[i] * gain);

			buffer[ii++] = (byte) (sampleL & 255);
			buffer[ii++] = (byte) (sampleL >> 8);

			buffer[ii++] = (byte) (sampleR & 255);
			buffer[ii++] = (byte) (sampleR >> 8);
		}
	}

	public void stop() {
		// line.drain();
		updater.interrupt();
		line.stop();
		line.close();
	}

	class InputL implements SignalInput {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.modsyn.AudioInput#write(float[])
		 */
		@Override
		public synchronized void set(float data) {
			fbuffer1[fbuffer1Index++] = data;
			fbuffer1Index = fbuffer1Index % fbuffer1.length;
		}
	}

	class InputR implements SignalInput {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.modsyn.AudioInput#write(float[])
		 */
		@Override
		public synchronized void set(float data) {
			fbuffer2[fbuffer2Index++] = data;
			fbuffer2Index = fbuffer2Index % fbuffer2.length;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.AudioOutput#connectTo(org.modsyn.AudioInput)
	 */
	@Override
	public void connectTo(SignalInput input) {
		// dummy, this is an end point
	}

	/**
	 * Default gain = 32767 (=max amplitude of the signal for 16 bit)
	 * 
	 * @param gain
	 */
	public void setGain(float gain) {
		this.gain = gain;
	}

}
