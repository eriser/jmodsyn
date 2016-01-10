package org.modsyn.vst;

import java.io.File;
import java.io.FileFilter;

import jvst.wrapper.VSTPluginAdapter;
import jvst.wrapper.valueobjects.VSTEvent;
import jvst.wrapper.valueobjects.VSTEvents;
import jvst.wrapper.valueobjects.VSTMidiEvent;
import jvst.wrapper.valueobjects.VSTPinProperties;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.ContextListener;
import org.modsyn.DspObject;
import org.modsyn.editor.DspPatchCombinationComponent;
import org.modsyn.editor.DspPatchCombinationModel;
import org.modsyn.editor.io.FileSys;
import org.modsyn.editor.io.XmlImport;
import org.modsyn.modules.ext.MidiListener;
import org.modsyn.modules.ext.MidiSupport;

public class VSTInstrumentSupport extends VSTPluginAdapter {

	final Context context;

	final DspPatchCombinationModel pcModel;

	final DspPatchCombinationComponent pcComponent;

	final File[] patches;

	VSTPluginAudioSupport audioObject;

	private int program;

	public VSTInstrumentSupport(long wrapper) {
		super(wrapper);
		setUniqueID('J' << 24 | 'M' << 16 | 'd' << 8 | 'S');
		setNumInputs(VSTPluginSupport.VST_MONO_IN ? 1 : 0);
		setNumOutputs(VSTPluginSupport.VST_STEREO_OUT ? 2 : 1);
		canProcessReplacing(true);
		isSynth(VSTPluginSupport.VST_INSTRUMENT);
		// this.hasVu(false); //deprecated as of vst2.4
		// this.hasClip(false); //deprecated as of vst2.4

		this.context = ContextFactory.create();
		this.pcModel = new DspPatchCombinationModel(context);
		this.pcComponent = new DspPatchCombinationComponent(context, pcModel);

		this.patches = FileSys.dirPatches.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isFile() && f.getName().endsWith(".dsp-patch") && !f.getName().startsWith(".") && !f.getName().startsWith("_");
			}
		});

		context.setListener(new ContextListener() {
			@Override
			public void removed(DspObject o) {
				if (o instanceof VSTPluginAudioSupport) {
					audioObject = null;
				}
			}

			@Override
			public void added(DspObject o) {
				if (o instanceof VSTPluginAudioSupport) {
					audioObject = (VSTPluginAudioSupport) o;
				}
			}
		});

		setProgram(0);
		suspend();
	}

	@Override
	public VSTPinProperties getOutputProperties(int index) {
		VSTPinProperties ret = null;

		if (index < 2) {
			ret = new VSTPinProperties();
			ret.setLabel("JModSyn " + (index + 1) + "d");
			ret.setFlags(VSTPinProperties.VST_PIN_IS_ACTIVE);
		}

		return ret;
	}

	@Override
	public void setSampleRate(float s) {
		context.setSampleRate((int) s);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void resume() {
		this.wantEvents(1); // deprecated as of vst2.4
							// but keep it --> backward compatibility!!!
	}

	@Override
	public int processEvents(VSTEvents ev) {
		for (int i = 0; i < ev.getNumEvents(); i++) {
			if (ev.getEvents()[i].getType() != VSTEvent.VST_EVENT_MIDI_TYPE)
				continue;

			VSTMidiEvent event = (VSTMidiEvent) ev.getEvents()[i];
			byte[] midiData = event.getData();

			int status = midiData[0];
			int type = status & 0xf0;
			int channel = 1;// default to channel 1, otherwise status & 0x0f;

			MidiListener listener = MidiSupport.INSTANCE.getListener(channel);
			if (listener == null || type == 0xf0) {
				// no listener or SYSEX message
				return 1;
			}

			int b1 = midiData[1] & 0xff;
			int b2 = midiData[2] & 0xff;

			switch (type) {
			case 0x80: // key off
				listener.keyOff(b1, b2);
				break;
			case 0x90: // key on
				if (midiData[2] == 0) {
					listener.keyOff(b1, b2);
				} else {
					listener.keyOn(b1, b2);
				}
				break;
			case 0xb0: // control change / channel mode msg
				listener.controlChange(b1, b2);
				break;
			case 0xe0: // pitch bend
				int val = (b1 | (b2 << 7)) - 0x2000;
				listener.pitchBend(val);
				break;
			}

		}

		return 1; // want more
	}

	@Override
	public int canDo(String feature) {
		int ret = CANDO_NO;

		if (CANDO_PLUG_RECEIVE_VST_EVENTS.equals(feature))
			ret = CANDO_YES;
		if (CANDO_PLUG_RECEIVE_VST_MIDI_EVENT.equals(feature))
			ret = CANDO_YES;
		if (CANDO_PLUG_MIDI_PROGRAM_NAMES.equals(feature))
			ret = CANDO_YES;

		return ret;
	}

	@Override
	public int getPlugCategory() {
		return VSTPluginAdapter.PLUG_CATEG_SYNTH;
	}

	@Override
	public String getProductString() {
		return "JModSyn PatchPlayer";
	}

	@Override
	public String getProgramNameIndexed(int category, int index) {
		if (index < patches.length) {
			return patches[index].getName();
		}
		return "";
	}

	@Override
	public String getVendorString() {
		return "JModSyn";
	}

	@Override
	public boolean setBypass(boolean b) {
		return false;
	}

	@Override
	public boolean string2Parameter(int arg0, String arg1) {
		return false;
	}

	@Override
	public int getNumParams() {
		return 0;
	}

	@Override
	public int getNumPrograms() {
		return patches.length;
	}

	@Override
	public float getParameter(int arg0) {
		return 0;
	}

	@Override
	public String getParameterDisplay(int arg0) {
		return "";
	}

	@Override
	public String getParameterLabel(int arg0) {
		return "";
	}

	@Override
	public String getParameterName(int arg0) {
		return "";
	}

	@Override
	public int getProgram() {
		return program;
	}

	@Override
	public String getProgramName() {
		return patches[program].getName();
	}

	@Override
	public void process(float[][] input, float[][] output, int samples) {
		processReplacing(input, output, samples);
	}

	@Override
	public void processReplacing(float[][] input, float[][] output, int samples) {
		if (audioObject != null) {
			boolean doInput = input != null && input.length > 0 && input[0].length >= samples;

			for (int i = 0; i < samples; i++) {
				if (doInput) {
					audioObject.connectedInput.set(input[0][i]);
				}

				context.update();

				output[0][i] = audioObject.outputBuffer0;
				output[1][i] = audioObject.outputBuffer1;
			}
		}
	}

	@Override
	public void setParameter(int arg0, float arg1) {
	}

	@Override
	public void setProgram(int program) {
		if (program < patches.length) {
			pcModel.clear();
			try {
				new XmlImport(patches[program], context, pcModel);
				this.program = program;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setProgramName(String name) {
	}
}
