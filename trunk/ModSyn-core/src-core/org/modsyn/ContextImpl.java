package org.modsyn;

/**
 * The default implementation of a Context.
 * 
 * @author Erik Duijs
 */
class ContextImpl implements Context {

	private static final boolean DEBUG = true;

	private final int sampleRate;
	private final SignalSource[] updatedSignals = new SignalSource[1024];
	private final SignalSource[] masters = new SignalSource[32];

	private int iUpdatedSignals = 0;
	private int iMasters = 0;

	public ContextImpl(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	@Override
	public int getSampleRate() {
		return sampleRate;
	}

	@Override
	public void remove(SignalSource source) {
		if (DEBUG) {
			System.out.println("Removing " + source + " from context (" + iUpdatedSignals + "/" + iMasters + ")");
		}
		for (int i = 0; i < iUpdatedSignals; i++) {
			if (updatedSignals[i] == source) {
				iUpdatedSignals--;
				updatedSignals[i].connectTo(NullInput.INSTANCE);
				for (int j = i; j < iUpdatedSignals; j++) {
					updatedSignals[i] = updatedSignals[i + 1];
				}
				return;
			}
		}

		for (int i = 0; i < iMasters; i++) {
			if (masters[i] == source) {
				iMasters--;
				masters[i].connectTo(NullInput.INSTANCE);
				for (int j = i; j < iMasters; j++) {
					masters[i] = masters[i + 1];
				}
				return;
			}
		}

		if (DEBUG) {
			System.out.println("Could not remove " + source);
		}
	}

	@Override
	public void addSignalSource(SignalSource sg) {
		updatedSignals[iUpdatedSignals] = (sg);
		iUpdatedSignals++;
	}

	@Override
	public void addMaster(SignalSource master) {
		masters[iMasters] = (master);
		iMasters++;
	}

	@Override
	public void update(float seconds) {
		int samples = (int) (seconds * sampleRate);
		while (samples-- > 0) {
			update();
		}
	}

	@Override
	public void update() {
		for (int i = 0; i < iUpdatedSignals; i++) {
			updatedSignals[i].updateSignal();
		}
		for (int i = 0; i < iMasters; i++) {
			masters[i].updateSignal();
		}
	}

	@Override
	public void clear() {
		for (int i = 0; i < iUpdatedSignals; i++) {
			updatedSignals[i] = null;
		}
		for (int i = 0; i < iMasters; i++) {
			masters[i] = null;
		}
		iUpdatedSignals = iMasters = 0;
	}
}
