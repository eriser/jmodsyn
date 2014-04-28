package org.modsyn.util;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Utility class for writing 16bit .wav files.
 * 
 * @author Erik Duijs
 */
public class WavWriter {

	private int bytesPerSec;
	private int blockAlignment;
	private String path;
	private int fileSize;
	private int dataSize;
	private int sampleRate;
	private int channels;
	private int bitsPerSample;
	private float amp;
	private RandomAccessFile wav;

	/**
	 * Constructor for standard CD quality (44.100 KHz, 16 bits, stereo)
	 */
	public WavWriter() {
		init(44100, 2, 16);
	}

	/**
	 * Constructor
	 * 
	 * @param sampleRate
	 * @param channels
	 * @param bitsPerSample
	 */
	public WavWriter(int sampleRate, int channels) {
		init(sampleRate, channels, 16);
	}

	/**
	 * Initialize the wav file's format properties
	 * 
	 * @param sampleRate
	 * @param channels
	 * @param bitsPerSample
	 */
	private void init(int sampleRate, int channels, int bitsPerSample) {
		this.sampleRate = sampleRate;
		this.channels = channels;
		this.bitsPerSample = bitsPerSample;
		this.blockAlignment = channels * bitsPerSample / 8;
		this.bytesPerSec = sampleRate * blockAlignment;
	}

	/**
	 * Open a new stream.
	 * 
	 * @param path
	 * @throws IOException
	 */
	public synchronized void open(String path) throws IOException {
		this.path = path;
		amp = 1;
		fileSize = 0;
		dataSize = 0;
		RandomAccessFile wav = new RandomAccessFile(path, "rw");
		wav.setLength(0);
		writeHeader(wav);

		this.wav = wav;
	}

	/**
	 * Writes the WAV file's header. The file length and data chunk part are not
	 * filled yet; they are updated when the stream is closed.
	 * 
	 * @param wav
	 * 
	 * @throws IOException
	 */
	private synchronized void writeHeader(RandomAccessFile wav) throws IOException {
		fileSize = 36;
		dataSize = 0;

		wav.writeBytes("RIFF");

		wav.write(255); // file length - 8
		wav.write(255);
		wav.write(255);
		wav.write(255);

		wav.writeBytes("WAVE");
		wav.writeBytes("fmt ");

		wav.write(0x10); // length of fmt block
		wav.write(0);
		wav.write(0);
		wav.write(0);

		wav.write(0x01); // 1 = PCM
		wav.write(0);

		wav.write(channels); // stereo
		wav.write(0);

		wav.write(sampleRate & 0xff);
		wav.write((sampleRate >>> 8) & 0xff);
		wav.write((sampleRate >>> 16) & 0xff);
		wav.write((sampleRate >>> 24) & 0xff);

		wav.write(bytesPerSec & 0xff);
		wav.write((bytesPerSec >>> 8) & 0xff);
		wav.write((bytesPerSec >>> 16) & 0xff);
		wav.write((bytesPerSec >>> 24) & 0xff);

		wav.write(blockAlignment & 0xff);
		wav.write((blockAlignment >>> 8) & 0xff);

		wav.write(bitsPerSample & 0xff);
		wav.write((bitsPerSample >>> 8) & 0xff);

		wav.writeBytes("data");

		wav.write(255); // data size
		wav.write(255);
		wav.write(255);
		wav.write(255);

		System.out.println(".wav header written");
	}

	/**
	 * Close the stream. The file size and data chunk size fields in the header
	 * will be updated.
	 * 
	 * @throws IOException
	 */
	public synchronized void close() throws IOException {
		if (wav != null) {
			updateHeader();
			wav.close();
			wav = null;
		}
	}

	/**
	 * Write a float to the sample data of the WAV file. This will be converted
	 * to a 16bit value. A value between -1.0f and 1.0f is assumed.
	 * 
	 * @param f
	 * @throws IOException
	 */
	public synchronized void write(float f) throws IOException {
		f *= amp;

		if (f > 1) {
			amp *= 1f / f;
			f = 1;
		} else if (f < -1) {
			amp *= -1f / f;
			f = -1;
		}

		write((short) (f * 32767f));
	}

	/**
	 * Write a (signed) 16 bit sample.
	 * 
	 * @param s
	 * @throws IOException
	 */
	public synchronized void write(short s) throws IOException {
		if (wav != null) {
			wav.write(s & 0xff);
			wav.write(s >>> 8);
			dataSize += 2;
			fileSize += 2;
		}
	}

	/**
	 * Update the header with file size and size of the data chunk.
	 * 
	 * @throws IOException
	 */
	private synchronized void updateHeader() throws IOException {

		wav.seek(4);
		wav.write(fileSize & 0xff);
		wav.write((fileSize >>> 8) & 0xff);
		wav.write((fileSize >>> 16) & 0xff);
		wav.write((fileSize >>> 24) & 0xff);

		wav.seek(40);
		wav.write(dataSize & 0xff);
		wav.write((dataSize >>> 8) & 0xff);
		wav.write((dataSize >>> 16) & 0xff);
		wav.write((dataSize >>> 24) & 0xff);

		System.out.println(path + " header updated.");
	}
}