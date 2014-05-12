package org.modsyn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;

public class WavReader implements SignalSource {

	private static final int HEADER_SIZE = 44;

	private final boolean loop;
	private final byte[] wav;
	private int idx;

	public final DefaultSignalOutput outL = new DefaultSignalOutput();
	public final DefaultSignalOutput outR = new DefaultSignalOutput();

	public WavReader(File f, boolean loop) throws IOException {
		this.loop = loop;
		this.wav = new IOTransferTool().loadBinary(new FileInputStream(f));
		idx = HEADER_SIZE;
	}

	@Override
	public void connectTo(SignalInput input) {
	}

	@Override
	public void updateSignal() {
		if (loop && idx >= wav.length) {
			idx = HEADER_SIZE;
		}

		if (idx < wav.length) {
			outL.connectedInput.set(((wav[idx++] & 0xff) | ((wav[idx++] & 0xff) << 8)) / (float) 0x8000);
			outR.connectedInput.set(((wav[idx++] & 0xff) | ((wav[idx++] & 0xff) << 8)) / (float) 0x8000);
		}
	}

}
