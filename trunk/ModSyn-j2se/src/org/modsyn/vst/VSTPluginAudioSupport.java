package org.modsyn.vst;

import org.modsyn.Context;
import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;

public class VSTPluginAudioSupport implements SignalSource {

	float inputBuffer;
	float outputBuffer0, outputBuffer1;

	public final SignalInput inputL = new SignalInput() {
		@Override
		public void set(float data) {
			outputBuffer0 = data;
		}
	};

	public final SignalInput inputR = new SignalInput() {
		@Override
		public void set(float data) {
			outputBuffer1 = data;
		}
	};

	public SignalInput connectedInput = NullInput.INSTANCE;

	public static boolean isSupported() {
		return VSTPluginSupport.VST_STEREO_OUT;
	}

	@Override
	public void connectTo(SignalInput input) {
		connectedInput = input;
	}

	@Override
	public void updateSignal() {
	}

	public void start(Context context) {
		context.addSignalSource(this);
	}
}
