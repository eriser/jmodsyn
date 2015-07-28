package org.modsyn.synth;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;
import org.modsyn.SignalSource;
import org.modsyn.Synth;
import org.modsyn.modules.Amplifier;
import org.modsyn.modules.LPF;
import org.modsyn.modules.Oscillator;
import org.modsyn.modules.ctrl.ADSREnvelope;
import org.modsyn.modules.ctrl.LFO;
import org.modsyn.util.WaveTables;

/**
 * @author DU1381
 * 
 *         Subtractive synthesis with 2 oscillators; 1 left, 1 right channel.
 *         Both oscillators' pulse width is modulated by an LFO, and they are
 *         slightly detuned to add overall warmth and 'life' to the sound. The
 *         oscillators have an LFO attached to their frequency control for
 *         vibrato. The oscillators go through an envelope controlled LPF, and
 *         an envelope controlled amplifier. Both envelope generators respond to
 *         note-on velocity.
 * 
 * 
 *         +------------+ +-------+ +-------+ | Oscillator +------->| LPF +--->|
 *         Amp +----> LEFT OUT +------------+ +-------+ +-------+ |pwm |freq
 *         |cutoff |amp +--+--+ +--+--+ +---+---+ +---+---+ | LFO | | LFO | |
 *         E.G. | | E.G. | +-----+ +--+--+ +---+---+ +---+---+ |freq |cutoff
 *         |amp +------------+ +-------+ +-------+ | Oscillator +------->| LPF
 *         +--->| Amp +----> RIGHT OUT +------------+ +-------+ +-------+ |pwm
 *         +--+--+ | LFO | +-----+
 */
public class Subtractive1 implements Synth, SignalSource {

	static final boolean IS_UPDATED_SIGNAL = true;

	public static final int CONTROL_FILTER_RESONANCE = 0;
	public static final int CONTROL_AMP_ATTACK_LEVEL = 1;
	public static final int CONTROL_AMP_ATTACK_TIME = 2;
	public static final int CONTROL_AMP_DECAY_LEVEL = 3;
	public static final int CONTROL_AMP_DECAY_TIME = 4;
	public static final int CONTROL_AMP_SUSTAIN_LEVEL = 5;
	public static final int CONTROL_AMP_RELEASE_LEVEL = 6;
	public static final int CONTROL_AMP_RELEASE_TIME = 7;
	public static final int CONTROL_FILTER_ATTACK_LEVEL = 8;
	public static final int CONTROL_FILTER_ATTACK_TIME = 9;
	public static final int CONTROL_FILTER_DECAY_LEVEL = 10;
	public static final int CONTROL_FILTER_DECAY_TIME = 11;
	public static final int CONTROL_FILTER_SUSTAIN_LEVEL = 12;
	public static final int CONTROL_FILTER_RELEASE_LEVEL = 13;
	public static final int CONTROL_FILTER_RELEASE_TIME = 14;
	public static final int CONTROL_VIBRATO_AMOUNT = 15;
	public static final int CONTROL_VIBRATO_SPEED = 16;
	public static final int CONTROL_WAVEFORM_SELECT = 17;

	Oscillator oscL, oscR;
	Amplifier outL, outR;
	LFO lfoPwmL, lfoPwmR, vibrato;
	LPF lpfL, lpfR;
	ADSREnvelope envLPF, envAmp;

	SignalInput[] controls = new SignalInput[128];

	boolean state = false;

	public Subtractive1(Context context) {
		if (IS_UPDATED_SIGNAL) {
			context.addSignalSource(this);
		}
		controls[CONTROL_FILTER_RESONANCE] = new ResoControl();
		controls[CONTROL_AMP_ATTACK_LEVEL] = new AmpAttackLevelControl();
		controls[CONTROL_AMP_ATTACK_TIME] = new AmpAttackTimeControl();
		controls[CONTROL_AMP_DECAY_LEVEL] = new AmpDecayLevelControl();
		controls[CONTROL_AMP_DECAY_TIME] = new AmpDecayTimeControl();
		controls[CONTROL_AMP_SUSTAIN_LEVEL] = new AmpSustainLevelControl();
		controls[CONTROL_AMP_RELEASE_LEVEL] = new AmpReleaseLevelControl();
		controls[CONTROL_AMP_RELEASE_TIME] = new AmpReleaseTimeControl();
		controls[CONTROL_FILTER_ATTACK_LEVEL] = new LPFAttackLevelControl();
		controls[CONTROL_FILTER_ATTACK_TIME] = new LPFAttackTimeControl();
		controls[CONTROL_FILTER_DECAY_LEVEL] = new LPFDecayLevelControl();
		controls[CONTROL_FILTER_DECAY_TIME] = new LPFDecayTimeControl();
		controls[CONTROL_FILTER_SUSTAIN_LEVEL] = new LPFSustainLevelControl();
		controls[CONTROL_FILTER_RELEASE_LEVEL] = new LPFReleaseLevelControl();
		controls[CONTROL_FILTER_RELEASE_TIME] = new LPFReleaseTimeControl();
		controls[CONTROL_VIBRATO_AMOUNT] = new VibratoAmountControl();
		controls[CONTROL_VIBRATO_SPEED] = new VibratoSpeedControl();
		controls[CONTROL_WAVEFORM_SELECT] = new WaveFormSelect();

		oscL = new Oscillator(context, WaveTables.SAWTOOTH);
		oscR = new Oscillator(context, WaveTables.SAWTOOTH);
		outL = new Amplifier();
		outR = new Amplifier();
		lpfL = new LPF(context);
		lpfR = new LPF(context);
		envLPF = new ADSREnvelope(context);
		envAmp = new ADSREnvelope(context);
		// Sys.enableRegistration(false);
		lfoPwmL = new LFO(context, WaveTables.SINUS);
		lfoPwmR = new LFO(context, WaveTables.SINUS);
		vibrato = new LFO(context, WaveTables.SINUS);
		// Sys.enableRegistration(false);
		outL.lvl.set(0);
		outR.lvl.set(0);
		vibrato.setFrequency(5f);
		vibrato.setAmplitude(0);
		vibrato.setOffset(1.0f);
		lfoPwmL.setFrequency(0.143f);
		lfoPwmR.setFrequency(0.1f);
		lfoPwmL.setAmplitude(40);
		lfoPwmR.setAmplitude(40);
		lfoPwmL.setOffset(45);
		lfoPwmR.setOffset(50);

		envLPF.setLevel(ADSREnvelope.ATTACK_LEVEL, 100);
		envLPF.setTime(ADSREnvelope.ATTACK_TIME, 0.5f);
		envLPF.setLevel(ADSREnvelope.DECAY_LEVEL, 4000);
		envLPF.setTime(ADSREnvelope.DECAY_TIME, 1.5f);
		envLPF.setLevel(ADSREnvelope.SUSTAIN_LEVEL, 500);
		envLPF.setLevel(ADSREnvelope.RELEASE_LEVEL, 100);
		envLPF.setTime(ADSREnvelope.RELEASE_TIME, 1f);

		envAmp.setLevel(ADSREnvelope.ATTACK_LEVEL, 0);
		envAmp.setTime(ADSREnvelope.ATTACK_TIME, 0.1f);
		envAmp.setLevel(ADSREnvelope.DECAY_LEVEL, .1f);
		envAmp.setTime(ADSREnvelope.DECAY_TIME, 0.5f);
		envAmp.setLevel(ADSREnvelope.SUSTAIN_LEVEL, .08f);
		envAmp.setLevel(ADSREnvelope.RELEASE_LEVEL, 0);
		envAmp.setTime(ADSREnvelope.RELEASE_TIME, 0.5f);

		lpfL.setResonance(2);
		lpfR.setResonance(2);

		lfoPwmL.connectTo(oscL.ctrPWM);
		lfoPwmR.connectTo(oscR.ctrPWM);
		vibrato.connectTo(oscL.ctrDetune);
		vibrato.connectTo(oscR.ctrDetune);

		envLPF.connectTo(lpfL.cutOffControl);
		envLPF.connectTo(lpfR.cutOffControl);
		envAmp.connectTo(outL);
		envAmp.connectTo(outR);
		oscL.connectTo(lpfL);
		oscR.connectTo(lpfR);
		lpfL.connectTo(outL);
		lpfR.connectTo(outR);
	}

	@Override
	public void updateSignal() {
		float egLPF = envLPF.process();
		float egAmp = envAmp.process();
		float ctrlVib = vibrato.process();
		float ctrlPwmL = lfoPwmL.process();
		float ctrlPwmR = lfoPwmR.process();

		oscL.setDetune(ctrlVib);
		oscR.setDetune(ctrlVib);
		oscL.setPWM(ctrlPwmL);
		oscR.setPWM(ctrlPwmR);
		lpfL.setCutOff(egLPF);
		lpfR.setCutOff(egLPF);
		outL.lvl.set(egAmp);
		outR.lvl.set(egAmp);

		float sigL = oscL.process();
		float sigR = oscR.process();
		sigL = lpfL.process(sigL);
		sigR = lpfR.process(sigR);

		outL.set(sigL);
		outR.set(sigR);

	}

	@Override
	public void keyOn(float freq, float velo) {
		oscL.setFrequency(freq);
		oscR.setFrequency(freq * 1.01f);

		envAmp.setScale(velo);
		envLPF.setScale(velo);

		envAmp.trigger(false);
		envLPF.trigger(false);
		envAmp.trigger(true);
		envLPF.trigger(true);
	}

	@Override
	public void keyOff() {
		envAmp.trigger(false);
		envLPF.trigger(false);
	}

	@Override
	public SignalOutput getOutput(int channel) {
		return channel == LEFT ? outL : outR;
	}

	@Override
	public SignalInput getInput(int index) {
		return controls[index];
	}

	class ResoControl implements SignalInput {
		@Override
		public void set(float data) {
			lpfL.setResonance(data);
			lpfR.setResonance(data);
		}
	}

	class AmpAttackTimeControl implements SignalInput {
		@Override
		public void set(float data) {
			envAmp.setTime(ADSREnvelope.ATTACK_TIME, data);
		}
	}

	class AmpDecayTimeControl implements SignalInput {
		@Override
		public void set(float data) {
			envAmp.setTime(ADSREnvelope.DECAY_TIME, data);
		}
	}

	class AmpReleaseTimeControl implements SignalInput {
		@Override
		public void set(float data) {
			envAmp.setTime(ADSREnvelope.RELEASE_TIME, data);
		}
	}

	class AmpAttackLevelControl implements SignalInput {
		@Override
		public void set(float data) {
			envAmp.setTime(ADSREnvelope.ATTACK_LEVEL, data);
		}
	}

	class AmpDecayLevelControl implements SignalInput {
		@Override
		public void set(float data) {
			envAmp.setTime(ADSREnvelope.DECAY_LEVEL, data);
		}
	}

	class AmpSustainLevelControl implements SignalInput {
		@Override
		public void set(float data) {
			envAmp.setTime(ADSREnvelope.SUSTAIN_LEVEL, data);
		}
	}

	class AmpReleaseLevelControl implements SignalInput {
		@Override
		public void set(float data) {
			envAmp.setTime(ADSREnvelope.RELEASE_LEVEL, data);
		}
	}

	class LPFAttackTimeControl implements SignalInput {
		@Override
		public void set(float data) {
			envLPF.setTime(ADSREnvelope.ATTACK_TIME, data);
		}
	}

	class LPFDecayTimeControl implements SignalInput {
		@Override
		public void set(float data) {
			envLPF.setTime(ADSREnvelope.DECAY_TIME, data);
		}
	}

	class LPFReleaseTimeControl implements SignalInput {
		@Override
		public void set(float data) {
			envLPF.setTime(ADSREnvelope.RELEASE_TIME, data);
		}
	}

	class LPFAttackLevelControl implements SignalInput {
		@Override
		public void set(float data) {
			envLPF.setLevel(ADSREnvelope.ATTACK_LEVEL, data);
		}
	}

	class LPFDecayLevelControl implements SignalInput {
		@Override
		public void set(float data) {
			envLPF.setLevel(ADSREnvelope.DECAY_LEVEL, data);
		}
	}

	class LPFSustainLevelControl implements SignalInput {
		@Override
		public void set(float data) {
			envLPF.setLevel(ADSREnvelope.SUSTAIN_LEVEL, data);
		}
	}

	class LPFReleaseLevelControl implements SignalInput {
		@Override
		public void set(float data) {
			envLPF.setLevel(ADSREnvelope.RELEASE_LEVEL, data);
		}
	}

	class VibratoSpeedControl implements SignalInput {
		@Override
		public void set(float data) {
			vibrato.setFrequency(data);
		}
	}

	class VibratoAmountControl implements SignalInput {
		@Override
		public void set(float data) {
			vibrato.setAmplitude(data);
		}
	}

	class WaveFormSelect implements SignalInput {
		@Override
		public void set(float data) {
			int shape_id = (int) data;
			oscL.setShape(WaveTables.getWaveForm(shape_id));
			oscR.setShape(WaveTables.getWaveForm(shape_id));
		}
	}

	@Override
	public void connectTo(SignalInput input) {
		// TODO Auto-generated method stub

	}

}
