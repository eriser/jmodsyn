package org.modsyn.modules.ext;

import java.io.IOException;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.DspObject;
import org.modsyn.SignalInput;
import org.modsyn.util.WavWriter;

public class ToFile implements DspObject {

	private final WavWriter wav;
	private String path;

	public final SignalInput inL = new SignalInput() {
		@Override
		public void set(float sample) {
			try {
				wav.write(sample);
				outL.connectedInput.set(sample);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public final SignalInput inR = new SignalInput() {
		@Override
		public void set(float sample) {
			try {
				wav.write(sample);
				outR.connectedInput.set(sample);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	public final DefaultSignalOutput outL = new DefaultSignalOutput();
	public final DefaultSignalOutput outR = new DefaultSignalOutput();

	public ToFile(Context c) {
		wav = new WavWriter(c.getSampleRate(), 2);
		path = "./default.wav";
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void start() throws IOException {
		wav.open(path);
	}

	public void stop() throws IOException {
		wav.close();
	}

	public void close() throws IOException {
		wav.close();
	}

}
