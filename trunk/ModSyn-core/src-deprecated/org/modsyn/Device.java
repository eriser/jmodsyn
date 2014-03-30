package org.modsyn;

import org.modsyn.SignalInput;
import org.modsyn.SignalOutput;

/**
 * @author DU1381
 * 
 *         A higher level view of DSP devices that can be used for GUI
 *         constructing etc.
 */
public interface Device {

	/**
	 * Get the name of the device
	 * 
	 * @return The device name
	 */
	public String getName();

	/**
	 * Get the SignalInputs (1 for mono, 2 for stereo)
	 * 
	 * @return <code>null</code> if the device doesn't have inputs
	 */
	public SignalInput[] getSignalInputs();

	/**
	 * Get the SignalOutputs (1 for mono, 2 for stereo)
	 * 
	 * @return <code>null</code> if the device doesn't have outputs
	 */
	public SignalOutput[] getSignalOutputs();

	/**
	 * Get the device controls
	 * 
	 * @return <code>null</code> if the device doesn't have controls
	 */
	public DeviceControl[] getDeviceControls();

	/**
	 * Get the sub devices
	 * 
	 * @return <code>null</code> if the device doesn't have sub devices
	 */
	public Device[] getSubDevices();

}
