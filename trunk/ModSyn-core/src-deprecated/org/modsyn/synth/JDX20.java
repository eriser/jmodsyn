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
 *         Simple 2-operator FM Synthesizer.
 * 
 *         +-----------+ +-----------+ +-----------+ | Modulator +--->| Carrier
 *         +--->| Amplifier +---> LEFT OUT, RIGHT OUT +--o-----o--+
 *         +---o-------+ +--o--------+ mod| Hz|-----+-----|Hz |amp +--+--+
 *         +--+--+ +--+--+ | E.G.| | LFO | | E.G.| +-----+ +-----+ +-----+
 */
public class JDX20 implements Synth, Device, SignalSource {

	FMOperator carrier;
	FMOperator modulator;
	// ADSREnvelope envMod, envAmp;
	PanPot pan;
	LFO vibrato;

	SignalOutput[] outputs;
	DeviceControl[] controls;
	Device[] subDevices;

	public JDX20(Context context) {
		context.addSignalSource(this);
		carrier = new FMOperator(context, WaveTables.SINUS, "Carrier");
		modulator = new FMOperator(context, WaveTables.SINUS, "Modulator");
		pan = new PanPot();
		vibrato = new LFO(context, WaveTables.SINUS);
		vibrato.setOffset(1.0f);

		outputs = new SignalOutput[2];
		controls = new DeviceControl[2];
		subDevices = new Device[2];

		outputs[0] = pan.outputL;
		outputs[1] = pan.outputR;

		controls[0] = new DeviceControl("Vibrato speed", vibrato.frequencyControl, 5.1f, 0, 20);
		controls[1] = new DeviceControl("Vibrato amount", vibrato.amplitudeControl, 0.009f, 0, 0.1f);
		subDevices[0] = carrier;
		subDevices[1] = modulator;
	}

	@Override
	public void updateSignal() {
		float vib = vibrato.process();

		carrier.setDetune(vib);
		modulator.setDetune(vib);

		carrier.setModulatorInput(modulator.process());

		pan.set(carrier.process());

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
		carrier.envelope.trigger(false);
		modulator.envelope.trigger(false);
	}

	@Override
	public void keyOn(float freq, float velo) {
		carrier.envelope.trigger(false);
		modulator.envelope.trigger(false);
		carrier.envelope.trigger(true);
		modulator.envelope.trigger(true);
		carrier.setFrequency(freq);
		modulator.setFrequency(freq);

		velo /= 127f;

		System.out.println(freq + ", " + velo);

		// carrier.envelope.setScale(75 + velo * 25f);
		// modulator.envelope.setScale(75 + velo * 25f);

	}

	@Override
	public String getName() {
		return "FM2Op";
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

}
