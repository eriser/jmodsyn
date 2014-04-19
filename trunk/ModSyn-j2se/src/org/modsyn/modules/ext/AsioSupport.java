package org.modsyn.modules.ext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modsyn.Context;
import org.modsyn.NullInput;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;

import com.synthbot.jasiohost.AsioChannel;
import com.synthbot.jasiohost.AsioDriver;
import com.synthbot.jasiohost.AsioDriverListener;
import com.synthbot.jasiohost.AsioDriverState;

public enum AsioSupport implements SignalSource {

	INSTANCE;

	/**
	 * Optional multi-threading support. In this case, the Context is updated from a different Thread. This is disabled
	 * for now; The ASIO thread itself will update the Context.
	 */
	private final boolean MULTI_THREAD = false;
	private final Object SYNC = new Object();

	private boolean started;
	private AsioDriver asio;
	private Set<AsioChannel> activeChannels;

	private float[] inputBuffer;
	private float[][] outputBuffers;
	private int bufferSize;
	private int bufferIndex;

	public final SignalInput inputL = new SignalInput() {
		@Override
		public void set(float data) {
			outputBuffers[0][bufferIndex] = data;
		}
	};

	public final SignalInput inputR = new SignalInput() {
		@Override
		public void set(float data) {
			outputBuffers[1][bufferIndex] = data;
		}
	};

	public SignalInput connectedInput = NullInput.INSTANCE;

	public boolean isSupported() {
		try {
			boolean isSupported = AsioDriver.getDriverNames().size() > 0;
			if (!isSupported) {
				System.out.println("No Asio support found.");
			}
			return isSupported;
		} catch (Throwable t) {
			// this will happen in non-windows environments or if otherwise Asio could not initialize.
			System.err.println(t.getMessage());
			return false;
		}
	}

	public void start(final Context context) {

		if (MULTI_THREAD) {
			context.addSignalSource(this);
		}

		if (!started) {
			List<String> driverNames = AsioDriver.getDriverNames();
			if (driverNames.size() == 0) {
				System.err.println("No ASIO support found - can't start AsioSupport");
				bufferSize = 1;
				outputBuffers = new float[2][bufferSize];
				inputBuffer = new float[bufferSize];
				return;
			}

			System.out.println(AsioDriver.getDriverNames());
			int select = Integer.getInteger("asio.driver", AsioDriver.getDriverNames().size() - 1);
			System.out.println("selected ASIO driver = " + AsioDriver.getDriverNames().get(select));
			this.asio = AsioDriver.getDriver(AsioDriver.getDriverNames().get(select));

			activeChannels = new HashSet<AsioChannel>();
			activeChannels.add(asio.getChannelOutput(0));
			activeChannels.add(asio.getChannelOutput(1));
			activeChannels.add(asio.getChannelInput(0));

			bufferSize = asio.getBufferPreferredSize();
			System.out.println("buffer size = " + bufferSize + " (" + asio.getBufferMinSize() + "," + asio.getBufferMaxSize() + ")");

			outputBuffers = new float[2][bufferSize];
			inputBuffer = new float[bufferSize];

			asio.addAsioDriverListener(new AsioDriverListener() {
				double lowest = Double.MAX_VALUE;
				double highest = 0;

				@Override
				public void bufferSwitch(long systemTime, long samplePosition, Set<AsioChannel> channels) {

					long start = System.nanoTime();
					int i = 0;
					for (AsioChannel asioChannel : channels) {
						if (asioChannel.isInput()) {
							asioChannel.read(inputBuffer);
						} else {
							asioChannel.write(outputBuffers[i++]);
						}
					}

					if (MULTI_THREAD) {
						// wake up waiting Context
						synchronized (SYNC) {
							SYNC.notifyAll();
						}
					} else {

						while (bufferIndex < bufferSize) {
							connectedInput.set(inputBuffer[bufferIndex]);
							context.update();
							bufferIndex++;
						}
						bufferIndex = 0;
					}

					double perc = ((System.nanoTime() - start) / 2902494.33106575963718820861678) * 100;
					if (perc > 100) {
						// System.out.println(perc + "%");
					}
					if (perc > highest) {
						highest = perc;
						System.out.println("HIGHEST: " + perc + "%");
					} else if (perc < lowest) {
						lowest = perc;
						System.out.println("LOWEST: " + perc + "%");
					}
				}

				@Override
				public void sampleRateDidChange(double sampleRate) {
					System.out.println("sampleRateDidChange " + sampleRate);
				}

				@Override
				public void resetRequest() {
					/*
					 * This thread will attempt to shut down the ASIO driver. However, it will block on the AsioDriver
					 * object at least until the current method has returned.
					 */
					new Thread() {
						@Override
						public void run() {
							System.out.println("resetRequest() callback received. Returning driver to INITIALIZED state.");
							asio.returnToState(AsioDriverState.INITIALIZED);
						}
					}.start();
				}

				@Override
				public void resyncRequest() {
					System.out.println("resyncRequest");
				}

				@Override
				public void bufferSizeChanged(int bufferSize) {
					System.out.println("bufferSizeChanged " + bufferSize);

				}

				@Override
				public void latenciesChanged(int inputLatency, int outputLatency) {
					System.out.println("latenciesChanged " + inputLatency + "(in), " + outputLatency + "(out)");

				}
			});
			asio.createBuffers(activeChannels);
			asio.start();
			started = true;
		}
	}

	public boolean isStarted() {
		return started;
	}

	public void stop() {
		if (started) {
			asio.shutdownAndUnloadDriver();
			started = false;
		}
	}

	@Override
	public void connectTo(SignalInput input) {
		connectedInput = input;

	}

	/**
	 * Called from the Context, but this only happens when multi-threading is enabled.
	 */
	@SuppressWarnings("unused")
	@Override
	public synchronized void updateSignal() {
		if (MULTI_THREAD && started) {
			synchronized (SYNC) {
				if (bufferIndex == bufferSize - 1) {
					// buffer is full:
					// wait until the ASIO driver triggers next buffer-swap
					try {
						SYNC.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					bufferIndex = 0;
				} else {
					bufferIndex++;
				}
			}
			connectedInput.set(inputBuffer[bufferIndex]);
		}
	}
}
