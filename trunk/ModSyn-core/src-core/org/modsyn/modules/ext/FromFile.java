package org.modsyn.modules.ext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;
import org.modsyn.util.IOTransferTool;

public class FromFile implements SignalSource {

	private static final int HEADER_SIZE = 44;

	private boolean loop;
	private byte[] wav = new byte[0];
	private int idx;

	public final DefaultSignalOutput outL = new DefaultSignalOutput();
	public final DefaultSignalOutput outR = new DefaultSignalOutput();

	public void open(File f, boolean loop) throws IOException {
		this.loop = loop;
		this.wav = new IOTransferTool().loadBinary(new FileInputStream(f));
		idx = HEADER_SIZE;
	}

	public FromFile(Context c) {
		c.addSignalSource(this);
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
			outL.connectedInput.set(((short) ((wav[idx++] & 0xff) | ((wav[idx++] & 0xff) << 8))) / (float) 0x8000);
			outR.connectedInput.set(((short) ((wav[idx++] & 0xff) | ((wav[idx++] & 0xff) << 8))) / (float) 0x8000);
		}
	}
}
