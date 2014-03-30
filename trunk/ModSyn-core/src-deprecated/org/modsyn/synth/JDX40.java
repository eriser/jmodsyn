package org.modsyn.synth;

import org.modsyn.Context;
import org.modsyn.Device;
import org.modsyn.DeviceControl;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;
import org.modsyn.SignalSource;
import org.modsyn.Synth;
import org.modsyn.modules.FMOperator;
import org.modsyn.modules.PanPot;
import org.modsyn.modules.ctrl.LFO;
import org.modsyn.util.WaveTables;

/**
 * @author DU1381
 * 
 *         4-Operator FM Synth
 */
public class JDX40 implements Synth, Device, SignalSource {

	FMOperator op1c, op2, op3, op4;
	Algo algo;

	final Algo[] algos = { new Algo_1_2_3_4(), new Algo_1_234(), new Algo_1_24_3(), new Algo_1_2_34(), new Algo_1_23_4(), new Algo_13_24(), new Algo_1234() };
	static final String[] ALGO_LABELS = { "4-> 3-> 2-> 1", "2+3+4 -> 1", "3->2->1, 4->1", "3+4->2->1", "4->2+3->1", "3->1, 4->2", "1+2+3+4" };
	PanPot pan;
	LFO vibrato;

	SignalOutput[] outputs;
	DeviceControl[] controls;
	Device[] subDevices;

	public final SignalInput ctrlAlgo = new SignalInput() {
		@Override
		public void set(float data) {
			setAlgo((int) data);
		}
	};

	public JDX40(Context context) {
		context.addSignalSource(this);
		op1c = new FMOperator(context, WaveTables.SINUS, "Op1");
		op2 = new FMOperator(context, WaveTables.SINUS, "Op2");
		op3 = new FMOperator(context, WaveTables.SINUS, "Op3");
		op4 = new FMOperator(context, WaveTables.SINUS, "Op4");

		pan = new PanPot();
		vibrato = new LFO(context, WaveTables.SINUS);
		vibrato.setOffset(1.0f);

		outputs = new SignalOutput[2];
		controls = new DeviceControl[3];
		subDevices = new Device[4];

		outputs[0] = pan.outputL;
		outputs[1] = pan.outputR;

		controls[0] = new DeviceControl("Algorithm", ctrlAlgo, 1, JDX40.ALGO_LABELS);
		controls[1] = new DeviceControl("Vibrato speed", vibrato.frequencyControl, 5.1f, 0, 20);
		controls[2] = new DeviceControl("Vibrato amount", vibrato.amplitudeControl, 0.009f, 0, 0.1f);
		subDevices[0] = op1c;
		subDevices[1] = op2;
		subDevices[2] = op3;
		subDevices[3] = op4;

		op2.getDeviceControls()[FMOperator.CTRL_FREQ_MULTIPLIER].setControlValue(2);
		op2.getDeviceControls()[FMOperator.CTRL_MODULATION_INDEX].setControlValue(10);
		op3.getDeviceControls()[FMOperator.CTRL_FREQ_MULTIPLIER].setControlValue(3);
		op3.getDeviceControls()[FMOperator.CTRL_MODULATION_INDEX].setControlValue(5);
		op4.getDeviceControls()[FMOperator.CTRL_FREQ_MULTIPLIER].setControlValue(4);
		op4.getDeviceControls()[FMOperator.CTRL_MODULATION_INDEX].setControlValue(5);

	}

	/**
	 * @param i
	 */
	protected void setAlgo(int i) {
		algo = algos[i];
		op1c.setModulatorInput(0);
		op2.setModulatorInput(0);
		op3.setModulatorInput(0);
		op4.setModulatorInput(0);
	}

	@Override
	public void updateSignal() {
		float vib = vibrato.process();

		op1c.setDetune(vib);
		op2.setDetune(vib);
		op3.setDetune(vib);
		op4.setDetune(vib);

		pan.set(algo.process());

	}

	@Override
	public SignalInput getInput(int index) {
		return null;
	}

	@Override
	public SignalOutput getOutput(int channel) {
		return channel == LEFT ? pan.outputL : pan.outputR;
	}

	@Override
	public void keyOff() {
		op1c.envelope.trigger(false);
		op2.envelope.trigger(false);
		op3.envelope.trigger(false);
		op4.envelope.trigger(false);
	}

	@Override
	public void keyOn(float freq, float velo) {
		op1c.setFrequency(freq);
		op2.setFrequency(freq);
		op3.setFrequency(freq);
		op4.setFrequency(freq);

		op1c.envelope.setScale(velo * 0.9f);
		op2.envelope.setScale(velo * 0.9f);
		op3.envelope.setScale(velo * 0.9f);
		op4.envelope.setScale(velo * 0.9f);

		op1c.envelope.trigger(true);
		op2.envelope.trigger(true);
		op3.envelope.trigger(true);
		op4.envelope.trigger(true);
	}

	@Override
	public String getName() {
		return "JDX-40";
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
		return outputs;
	}

	@Override
	public Device[] getSubDevices() {
		return subDevices;
	}

	@Override
	public void connectTo(SignalInput input) {
		// TODO Auto-generated method stub

	}

	abstract class Algo {
		abstract float process();
	}

	/*
	 * [4] [3] [2] [1]
	 */
	class Algo_1_2_3_4 extends Algo {
		@Override
		float process() {
			op3.setModulatorInput(op4.process());
			op2.setModulatorInput(op3.process());
			op1c.setModulatorInput(op2.process());
			return op1c.process();
		}
	}

	/*
	 * [2][3][4] [1]
	 */
	class Algo_1_234 extends Algo {
		@Override
		float process() {
			op1c.setModulatorInput(op2.process() + op3.process() + op4.process());
			return op1c.process();
		}
	}

	/*
	 * [3] [2] [4] [1]
	 */
	class Algo_1_24_3 extends Algo {
		@Override
		float process() {
			op2.setModulatorInput(op3.process());
			op1c.setModulatorInput(op2.process() + op4.process());
			return op1c.process();
		}
	}

	/*
	 * [3] [4] [2] [1]
	 */
	class Algo_1_2_34 extends Algo {
		@Override
		float process() {
			op2.setModulatorInput(op3.process() + op4.process());
			op1c.setModulatorInput(op2.process());
			return op1c.process();
		}
	}

	/*
	 * [4] [2] [3] [1]
	 */
	class Algo_1_23_4 extends Algo {
		@Override
		float process() {
			float sig4 = op4.process();
			op2.setModulatorInput(sig4);
			op3.setModulatorInput(sig4);
			op1c.setModulatorInput(op2.process() + op3.process());
			return op1c.process();
		}
	}

	/*
	 * [2] [4] [1] [3]
	 */
	class Algo_13_24 extends Algo {
		@Override
		float process() {
			op3.setModulatorInput(op4.process());
			op1c.setModulatorInput(op2.process());
			return op1c.process() + op3.process();
		}
	}

	/*
	 * [1][2][3][4]
	 */
	class Algo_1234 extends Algo {
		@Override
		float process() {
			return op1c.process() + op2.process() + op3.process() + op4.process();
		}
	}

}
