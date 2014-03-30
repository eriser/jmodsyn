package org.modsyn.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.modsyn.Context;
import org.modsyn.editor.blocks.ADSRModel;
import org.modsyn.editor.blocks.APFModel;
import org.modsyn.editor.blocks.AbsoluteModel;
import org.modsyn.editor.blocks.AdderModel;
import org.modsyn.editor.blocks.AmplifierModel;
import org.modsyn.editor.blocks.ArpeggioModel;
import org.modsyn.editor.blocks.BinauralModel;
import org.modsyn.editor.blocks.ChorusModel;
import org.modsyn.editor.blocks.CompressorModel;
import org.modsyn.editor.blocks.EnvelopeFollowerModel;
import org.modsyn.editor.blocks.Filter4PoleModel;
import org.modsyn.editor.blocks.Filter8PoleModel;
import org.modsyn.editor.blocks.FilterXPoleModel;
import org.modsyn.editor.blocks.FromAsioModel;
import org.modsyn.editor.blocks.FromJavaSoundModel;
import org.modsyn.editor.blocks.FromMidiPolyModel;
import org.modsyn.editor.blocks.Karlsen24dBModel;
import org.modsyn.editor.blocks.KarplusStrongModel;
import org.modsyn.editor.blocks.Keyboard2Model;
import org.modsyn.editor.blocks.KnobModel;
import org.modsyn.editor.blocks.LPFModel;
import org.modsyn.editor.blocks.MixerModel;
import org.modsyn.editor.blocks.MoogVCFModel;
import org.modsyn.editor.blocks.MultiSplitterModel;
import org.modsyn.editor.blocks.OctaverModel;
import org.modsyn.editor.blocks.OscillatorHQModel;
import org.modsyn.editor.blocks.OscillatorModel;
import org.modsyn.editor.blocks.PanPotModel;
import org.modsyn.editor.blocks.PhaserModel;
import org.modsyn.editor.blocks.PitcherModel;
import org.modsyn.editor.blocks.SoftClipModel;
import org.modsyn.editor.blocks.ToAsioModel;
import org.modsyn.editor.blocks.ToJavaSoundModel;
import org.modsyn.editor.blocks.VUMeterModel;
import org.modsyn.editor.blocks.VocoderModel;
import org.modsyn.gui.JKnob;
import org.modsyn.modules.Compressor;
import org.modsyn.modules.Tracker;
import org.modsyn.modules.ctrl.ADSREnvelope;
import org.modsyn.modules.ext.AsioSupport;
import org.modsyn.modules.ext.MidiVoicePolyAdapter;
import org.modsyn.modules.ext.ToJavaSound;
import org.modsyn.util.Keyboard2;
import org.modsyn.util.Keyboard2Adapter;

public enum DspPalette {

	Amp("Basics") {
		@Override
		public String getModelName() {
			return AmplifierModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new AmplifierModel(), pm);
		}
	},
	Mix("Basics") {
		@Override
		public String getModelName() {
			return MixerModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new MixerModel(c, getChannelsOrAsk(channels, "channels", 2, 16)), pm);
		}
	},
	Add("Basics") {
		@Override
		public String getModelName() {
			return AdderModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new AdderModel(), pm);
		}
	},
	Split("Basics") {
		@Override
		public String getModelName() {
			return MultiSplitterModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new MultiSplitterModel(getChannelsOrAsk(channels, "channels", 2, 16)), pm);
		}
	}/*
	 * , Split2("Basics") {
	 * 
	 * @Override public String getModelName() { return SplitterModel.class.getName(); }
	 * 
	 * @Override public DspBlockComponent create(Context c, DspPatchModel pm, int channels) { return new
	 * DspBlockComponent(new SplitterModel(), pm); } }
	 */,
	Pan("Basics") {
		@Override
		public String getModelName() {
			return PanPotModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new PanPotModel(), pm);
		}
	},
	Osc("Oscillators") {
		@Override
		public String getModelName() {
			return OscillatorModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new OscillatorModel(c), pm);
		}
	},
	Osc__HQ("Oscillators") {
		@Override
		public String getModelName() {
			return OscillatorHQModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new OscillatorHQModel(c), pm);
		}
	},
	K__Str("Oscillators") {
		@Override
		public String getModelName() {
			return KarplusStrongModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new KarplusStrongModel(c), pm);
		}
	},
	Arpeggio("Oscillators") {
		@Override
		public String getModelName() {
			return ArpeggioModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			channels = getChannelsOrAsk(channels, "voices", 2, 16);
			return new DspBlockComponent(new ArpeggioModel(c, channels), pm);
		}
	},
	ADSR("Dynamics") {
		@Override
		public String getModelName() {
			return ADSRModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new ADSRModel(new ADSREnvelope(c)), pm);
		}
	},
	Tracker("Dynamics") {
		@Override
		public String getModelName() {
			return EnvelopeFollowerModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new EnvelopeFollowerModel(new Tracker()), pm);
		}
	},
	Compressor("Dynamics") {
		@Override
		public String getModelName() {
			return CompressorModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new CompressorModel(new Compressor()), pm);
		}
	},
	xxx4__Pole("Filters") {
		@Override
		public String getModelName() {
			return Filter4PoleModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new Filter4PoleModel(c), pm);
		}
	},
	xxx8__Pole("Filters") {
		@Override
		public String getModelName() {
			return Filter8PoleModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new Filter8PoleModel(c), pm);
		}
	},
	X__Pole("Filters") {
		@Override
		public String getModelName() {
			return FilterXPoleModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new FilterXPoleModel(c), pm);
		}
	},
	MoogVCF("Filters") {
		@Override
		public String getModelName() {
			return MoogVCFModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new MoogVCFModel(c), pm);
		}
	},
	LPF("Filters") {
		@Override
		public String getModelName() {
			return LPFModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new LPFModel(c), pm);
		}
	},
	Resonator("Filters") {
		@Override
		public String getModelName() {
			return Karlsen24dBModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new Karlsen24dBModel(c), pm);
		}
	},
	APF("Filters") {
		@Override
		public String getModelName() {
			return APFModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new APFModel(c), pm);
		}
	},
	Vocoder("Filters") {
		@Override
		public String getModelName() {
			return VocoderModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new VocoderModel(), pm);
		}
	},
	SoftClip("Shape") {
		@Override
		public String getModelName() {
			return SoftClipModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new SoftClipModel(), pm);
		}
	},
	Absolute("Shape") {
		@Override
		public String getModelName() {
			return AbsoluteModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new AbsoluteModel(), pm);
		}
	},
	Chorus("FX") {
		@Override
		public String getModelName() {
			return ChorusModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new ChorusModel(c), pm);
		}
	},
	Phaser("FX") {
		@Override
		public String getModelName() {
			return PhaserModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new PhaserModel(c), pm);
		}
	},
	Octaver("FX") {
		@Override
		public String getModelName() {
			return OctaverModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new OctaverModel(), pm);
		}
	},
	PitchShift("FX") {
		@Override
		public String getModelName() {
			return PitcherModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new PitcherModel(), pm);
		}
	},
	Binaural("FX") {
		@Override
		public String getModelName() {
			return BinauralModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new BinauralModel(c), pm);
		}
	},

	Knob("Misc") {
		@Override
		public String getModelName() {
			return KnobModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			final JKnob knob = new JKnob();
			final KnobModel km = new KnobModel(knob);

			knob.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent e) {
					String p = e.getPropertyName();
					InputModel target = km.getOutputs().get(0).getTarget();

					if (target != null) {
						if (p.equals("value")) {
							target.setValue((float) e.getNewValue());
						} else if (p.equals("min")) {
							target.setMin((float) e.getNewValue());
						} else if (p.equals("max")) {
							target.setMax((float) e.getNewValue());
						} else if (p.equals("decimals")) {
							target.setDecimals((int) e.getNewValue());
						}
					}
				}
			});

			return new DspBlockComponent(km, pm, 0, 0, 80, 80) {
				private static final long serialVersionUID = 603933338049577236L;

				@Override
				public Component createCenterComponent() {
					return knob;
				}
			};
		}
	},
	Meter("Misc") {
		@Override
		public String getModelName() {
			return VUMeterModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			final VUMeterModel vmm = new VUMeterModel();
			return new DspBlockComponent(vmm, pm, 0, 0, 80, 80) {
				private static final long serialVersionUID = -8093488546616747252L;

				@Override
				public Component createCenterComponent() {
					JComponent c = new JComponent() {
						private static final long serialVersionUID = 6430783866717213954L;
						final Color bg = new Color(0x000080);
						int peak = 80;

						@Override
						public void paintComponent(Graphics g) {
							final float value = vmm.getDspObject().value;
							final int h = getHeight();
							final int w = getWidth();

							final int y = h - (int) (h * value) - 1;
							if (peak > y) {
								peak = y;
							} else if (peak < h) {
								peak += 2;
							}

							g.setColor(bg);
							g.fillRect(0, 0, w, y);
							g.setColor(Color.GREEN);
							g.fillRect(0, y, w, h);
							g.setColor(Color.WHITE);
							g.drawLine(0, peak, w, peak);
						}
					};
					return c;
				}
			};
		}
	},
	Keyboard("Misc") {
		@Override
		public String getModelName() {
			return Keyboard2Model.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			Keyboard2Adapter dsp = new Keyboard2Adapter();
			Keyboard2 kb2 = new Keyboard2(dsp);
			Keyboard2Model model = new Keyboard2Model(dsp);
			DspBlockComponent block = new DspBlockComponent(model, pm);
			block.button.addKeyListener(kb2);
			return block;
		}
	},
	MIDI_IN("EXT") {
		@Override
		public String getModelName() {
			return FromMidiPolyModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			channels = getChannelsOrAsk(channels, "polyphony", 1, 16);
			return new DspBlockComponent(new FromMidiPolyModel(new MidiVoicePolyAdapter(channels)), pm);
		}
	},
	Audio_IN("EXT") {
		@Override
		public String getModelName() {
			return FromJavaSoundModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new FromJavaSoundModel(c), pm);
		}
	},
	Audio_OUT("EXT") {
		@Override
		public String getModelName() {
			return ToJavaSoundModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new ToJavaSoundModel(new ToJavaSound(c, 2, 1024)), pm);
		}
	},
	Asio_IN("EXT") {
		@Override
		public String getModelName() {
			return FromAsioModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new FromAsioModel(c, AsioSupport.INSTANCE), pm);
		}
	},
	Asio_OUT("EXT") {
		@Override
		public String getModelName() {
			return ToAsioModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(new ToAsioModel(c, AsioSupport.INSTANCE), pm);
		}
	};

	public final String category;

	DspPalette(String category) {
		this.category = category;
	}

	public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
		return null;
	}

	public String getModelName() {
		return null;
	}

	public static DspBlockComponent createFromModelName(String modelName, Context c, DspPatchModel pm, int channels) {
		for (DspPalette pal : values()) {
			if (pal.getModelName().equals(modelName)) {
				return pal.create(c, pm, channels);
			}
		}
		return null;
	}

	private static int getChannelsOrAsk(int channels, String name, int min, int max) {
		if (channels > 0) {
			return channels;
		}

		Integer[] choices = new Integer[max - (min - 1)];
		for (int i = 0; i < choices.length; i++) {
			choices[i] = i + min;
		}

		return (Integer) JOptionPane.showInputDialog(null, "Select " + name, name, JOptionPane.PLAIN_MESSAGE, null, choices, Integer.toString(min));
	}

	@Override
	public String toString() {
		return name().replace("xxx", "").replace("__", "-").replace('_', ' ');
	}
}
