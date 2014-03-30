/*
 * Created on 27-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.modules.fx;

import org.modsyn.Context;
import org.modsyn.Device;
import org.modsyn.DeviceControl;
import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;

/**
 * @author DU1381
 * 
 *         Stereo version of Reverb142
 */
public class Reverb242 implements SignalInput, Device {

	public final Reverb142 left, right;
	private DeviceControl[] controls;

	private float time = 1.00f, decay = 0.9f, density = 0.5f;

	public final SignalInput mixControl = new SignalInput() {
		@Override
		public void set(float data) {
			left.setDryWet(data);
			right.setDryWet(data);
		}
	};
	public final SignalInput timeControl = new SignalInput() {
		@Override
		public void set(float data) {
			time = data;
			left.setDelayTimes(data, decay, density);
			right.setDelayTimes(data * 1.01f, decay, density);
		}
	};
	public final SignalInput decayControl = new SignalInput() {
		@Override
		public void set(float data) {
			decay = data;
			left.setDelayTimes(time, data, density);
			right.setDelayTimes(time * 1.01f, data, density);
		}
	};
	public final SignalInput densityControl = new SignalInput() {
		@Override
		public void set(float data) {
			density = data;
			left.setDelayTimes(time, decay, data);
			right.setDelayTimes(time * 1.01f, decay, data);
		}
	};

	public Reverb242(Context context) {
		left = new Reverb142(context, 1.00f, 0.9f, 0.5f, 0.25f);
		right = new Reverb142(context, 1.01f, 0.9f, 0.5f, 0.25f);

		controls = new DeviceControl[4];
		controls[0] = new DeviceControl("Mix (dry-wet)", mixControl, 0.05f, 0, 1);
		controls[1] = new DeviceControl("Room Size", timeControl, 1f, 0, 10);
		controls[2] = new DeviceControl("Decay", decayControl, 0.9f, 0, 1);
		controls[3] = new DeviceControl("Density", densityControl, 0.5f, 0, 1);
	}

	public Reverb242(Context context, float time, float decay, float density, float dry_wet) {
		left = new Reverb142(context, time, decay, density, dry_wet);
		right = new Reverb142(context, time * 1.01f, decay, density, dry_wet);
	}

	/**
	 * This is the MONO input of the reverb. If you need stereo input, write to
	 * revL and revR separately.
	 * 
	 * @see org.modsyn.SignalInput#set(float)
	 */
	@Override
	public void set(float sample) {
		left.set(sample);
		right.set(sample);
	}

	@Override
	public String getName() {
		return "Reverb242";
	}

	@Override
	public DeviceControl[] getDeviceControls() {
		return controls;
	}

	@Override
	public SignalInput[] getSignalInputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SignalOutput[] getSignalOutputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Device[] getSubDevices() {
		// TODO Auto-generated method stub
		return null;
	}

}
