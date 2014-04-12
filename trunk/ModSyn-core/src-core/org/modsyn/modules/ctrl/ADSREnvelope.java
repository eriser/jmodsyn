package org.modsyn.modules.ctrl;

import org.modsyn.Context;
import org.modsyn.Device;
import org.modsyn.DeviceControl;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;
import org.modsyn.SignalSource;

/**
 * Envelope generator of with Attack, Decay, Sustain and Release stages. You can attach multiple devices to it.
 * 
 * @author Erik Duijs
 */
public class ADSREnvelope implements SignalOutput, SignalSource, Device {

	/*
	 * 
	 * | level | 1 | / D | A 2-S-2 |/ R 0___________\3___ time
	 * 
	 * levels 0 = attack level 1 = decay level 2 = sustain level 3 = release level
	 * 
	 * times 0 = start 1 = attack time 2 = decay time 3 = release time
	 */
	public String name = "Envelope";
	SignalInput[] controlInputs;
	int curControlInput;

	public static final int ATTACK_LEVEL = 0;
	public static final int DECAY_LEVEL = 1;
	public static final int SUSTAIN_LEVEL = 2;
	private static final int SUSTAIN_LEVEL2 = 3; // always the same as
													// SUSTAIN_LEVEL
	public static final int RELEASE_LEVEL = 4;

	public static final int START_TIME = 0;
	public static final int ATTACK_TIME = 1;
	public static final int DECAY_TIME = 2;
	public static final int SUSTAIN_TIME = 3;
	public static final int RELEASE_TIME = 4;

	public static final int ATTACK_STAGE = 1;
	public static final int DECAY_STAGE = 2;
	public static final int SUSTAIN_STAGE = 3;
	public static final int RELEASE_STAGE = 4;

	private final float[] times = new float[5];
	private final float[] levels = new float[5];

	private boolean running;
	private boolean triggered;

	private float runningTime;
	private float stageTime;
	private float curStartLevel;
	private float curEndLevel;
	private float curLevel;
	private float curLevelStep;
	private int curStage;

	private float scale;
	private final float sampRateDiv1;

	public final SignalInput ctrlAtkTime = new SignalInput() {
		@Override
		public void set(float data) {
			setTime(ATTACK_TIME, data);
		}
	};
	public final SignalInput ctrlDcyTime = new SignalInput() {
		@Override
		public void set(float data) {
			setTime(DECAY_TIME, data);
		}
	};
	public final SignalInput ctrlRlsTime = new SignalInput() {
		@Override
		public void set(float data) {
			setTime(RELEASE_TIME, data);
		}
	};
	public final SignalInput ctrlAtkLevel = new SignalInput() {
		@Override
		public void set(float data) {
			setLevel(ATTACK_LEVEL, data);
		}
	};
	public final SignalInput ctrlDcyLevel = new SignalInput() {
		@Override
		public void set(float data) {
			setLevel(DECAY_LEVEL, data);
		}
	};
	public final SignalInput ctrlSusLevel = new SignalInput() {
		@Override
		public void set(float data) {
			setLevel(SUSTAIN_LEVEL, data);
		}
	};
	public final SignalInput ctrlRlsLevel = new SignalInput() {
		@Override
		public void set(float data) {
			setLevel(RELEASE_LEVEL, data);
		}
	};

	public final SignalInput trigger = new SignalInput() {
		@Override
		public void set(float sample) {
			trigger(sample != 0);
		}
	};

	private final DeviceControl[] controls = new DeviceControl[7];

	public ADSREnvelope(Context context) {
		controlInputs = new SignalInput[32];
		curControlInput = 0;

		scale = 1.0f;
		sampRateDiv1 = 1.0f / context.getSampleRate();
		times[SUSTAIN_TIME] = Float.MAX_VALUE;

		controls[0] = new DeviceControl("Attack Level", ctrlAtkLevel, 0, 0, 1);
		controls[1] = new DeviceControl("Decay Level", ctrlDcyLevel, 1, 0, 1);
		controls[2] = new DeviceControl("Sustain Level", ctrlSusLevel, 0.5f, 0, 1);
		controls[3] = new DeviceControl("Release Level", ctrlRlsLevel, 0, 0, 1);
		controls[4] = new DeviceControl("Attack Time", ctrlAtkTime, 0.1f, 0, 5);
		controls[5] = new DeviceControl("Decay Time", ctrlDcyTime, 0.2f, 0, 5);
		controls[6] = new DeviceControl("Release Time", ctrlRlsTime, 0.1f, 0, 5);
		context.addSignalSource(this);
	}

	public void trigger(boolean b) {
		if (b) {
			running = true;
			if (!triggered) {
				setStage(ATTACK_STAGE);
			}
		} else {
			if (triggered) {
				setStage(RELEASE_STAGE);
			}
		}
		triggered = b;
	}

	/**
	 * @param ATTACK_STAGE
	 */
	private void setStage(int stage) {
		curStage = stage;
		stageTime = times[stage];

		if (stage == RELEASE_STAGE || stage == ATTACK_STAGE) {
			// when the RELEASE_STAGE starts, the start level is the current
			// level so that it won't for example pop from somewhere in the
			// ATTACK_STAGE to the SUSTAIN_LEVEL
			curStartLevel = curLevel;
		} else {
			curStartLevel = levels[stage - 1];
		}

		curEndLevel = levels[stage];
		curLevel = curStartLevel;
		curLevelStep = (curEndLevel - curStartLevel) * (sampRateDiv1 / stageTime);
		runningTime = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.ControlSource#updateControl()
	 */
	@Override
	public void updateSignal() {
		if (running) {
			runningTime += sampRateDiv1;
			curLevel += curLevelStep;

			// has the current envelope stage expired?
			if (runningTime > stageTime) {
				// if the RELEASE_STAGE has expired, stop processing
				if (curStage == RELEASE_STAGE) {
					running = false;
					toControllers(levels[RELEASE_LEVEL]);
					return;
				}
				// go to next envelope stage
				setStage(++curStage);
			}

			toControllers(curLevel * scale);
		}
	}

	public float process() {
		if (running) {
			runningTime += sampRateDiv1;
			curLevel += curLevelStep;

			// has the current envelope stage expired?
			if (runningTime > stageTime) {
				// if the RELEASE_STAGE has expired, stop processing
				if (curStage == RELEASE_STAGE) {
					running = false;
					toControllers(levels[RELEASE_LEVEL]);
					return levels[RELEASE_LEVEL];
				}
				// go to next envelope stage
				setStage(++curStage);
			}

		}
		return (curLevel * scale);

	}

	/**
	 * Set level for a certain stage
	 * 
	 * @param id
	 *            ATTACK, DECAY, SUSTAIN, RELEASE
	 * @param value
	 *            level
	 */
	public void setLevel(int level_id, float value) {
		levels[level_id] = value;
		if (level_id == SUSTAIN_LEVEL) {
			levels[SUSTAIN_LEVEL2] = value;
		}
	}

	/**
	 * Set the time for a certain stage
	 * 
	 * @param id
	 *            ATTACK, DECAY, SUSTAIN, RELEASE
	 * @param value
	 *            time in sec
	 */
	public void setTime(int id, float value) {
		times[id] = value;
	}

	public void setScale(float perc) {
		this.scale = perc / 100f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modsyn.ControlOutput#connectTo(org.modsyn.SignalInput)
	 */
	@Override
	public void connectTo(SignalInput input) {
		controlInputs[curControlInput++] = input;
		// connectedDevices.add(input);
	}

	/**
	 * This updates all attached Control inputs
	 */
	private void toControllers(float f) {
		for (int i = 0; i < curControlInput; i++) {
			controlInputs[i].set(f);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DeviceControl[] getDeviceControls() {
		return controls;
	}

	@Override
	public SignalInput[] getSignalInputs() {
		return null;
	}

	@Override
	public SignalOutput[] getSignalOutputs() {
		return null;
	}

	@Override
	public Device[] getSubDevices() {
		return null;
	}

}
