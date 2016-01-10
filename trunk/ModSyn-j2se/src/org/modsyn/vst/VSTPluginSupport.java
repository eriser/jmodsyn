package org.modsyn.vst;

public class VSTPluginSupport {

	/**
	 * If set, MIDI and Audio support should be overridden by VSTInstrumentSupport. Set this value in the jVSTwRapper's
	 * .ini file.
	 */
	public static final boolean VST_INSTRUMENT = Boolean.getBoolean("VST_INSTRUMENT");

	public static final boolean VST_STEREO_OUT = VST_INSTRUMENT || Boolean.getBoolean("VST_STEREO_OUT");

	public static final boolean VST_MONO_IN = Boolean.getBoolean("VST_MONO_IN");

}
