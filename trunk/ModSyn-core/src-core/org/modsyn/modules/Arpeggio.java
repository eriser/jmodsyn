package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalSource;

public class Arpeggio implements SignalSource {

	public static class Channel {
		public SignalInputValue freq = new SignalInputValue(0);
		public SignalInputValue velo = new SignalInputValue(0);
		public SignalInput trig = new SignalInput() {
			@Override
			public void set(float signal) {
				a.trigOut.connectedInput.set(signal);
			}
		};
		private final Arpeggio a;

		private Channel(Arpeggio a) {
			this.a = a;
		}
	}

	public final Channel[] channels;

	public final SignalInput ctrlLength = new SignalInput() {
		@Override
		public void set(float signal) {
			length = (int) signal;
			counter = length;
		}
	};

	public final DefaultSignalOutput freqOut = new DefaultSignalOutput();
	public final DefaultSignalOutput veloOut = new DefaultSignalOutput();
	public final DefaultSignalOutput trigOut = new DefaultSignalOutput();

	int length, counter;
	int curChannel;

	public Arpeggio(Context c, int channels) {
		this.channels = new Channel[channels];
		for (int i = 0; i < channels; i++) {
			this.channels[i] = new Channel(this);
		}
		c.addSignalSource(this);
	}

	@Override
	public void connectTo(SignalInput input) {
		freqOut.connectTo(input);
	}

	@Override
	public void updateSignal() {
		float av = 0;
		int count = 0;
		for (int i = 0; i < channels.length; i++) {

			if (channels[i].velo.value > 0) {
				av += channels[i].velo.value;
				count++;
			}
		}
		av /= count;
		veloOut.connectedInput.set(av);

		if (counter-- <= 0) {
			counter = length;

			if (count > 0) {
				curChannel = (curChannel + 1) % count;
				// System.out.println(count);
				int c = -1;
				for (int i = 0; i < channels.length; i++) {
					// System.out.println(i + " - " + channels[i].velo.value);
					if (channels[i].velo.value > 0) {

						c++;
						// System.out.println("  " + c + " (" + curChannel +
						// ")");
						if (curChannel == c) {
							freqOut.connectedInput.set(channels[i].freq.value);
							return;
						}
					}
				}
				if (c >= 0 && c < channels.length) {
					System.out.println(c);
					curChannel = c;
					freqOut.connectedInput.set(channels[c].freq.value);
				}
			}
		}
	}
}
