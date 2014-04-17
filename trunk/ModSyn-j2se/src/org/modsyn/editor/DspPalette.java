package org.modsyn.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.modsyn.Context;
import org.modsyn.editor.blocks.ADRModel;
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
import org.modsyn.editor.blocks.FromMidiPolyModel;
import org.modsyn.editor.blocks.Karlsen24dBModel;
import org.modsyn.editor.blocks.KarplusStrongModel;
import org.modsyn.editor.blocks.Keyboard2Model;
import org.modsyn.editor.blocks.KnobModel;
import org.modsyn.editor.blocks.LPFModel;
import org.modsyn.editor.blocks.MixerModel;
import org.modsyn.editor.blocks.MoogVCFModel;
import org.modsyn.editor.blocks.MultiSplitterModel;
import org.modsyn.editor.blocks.NoiseModel;
import org.modsyn.editor.blocks.OctaverModel;
import org.modsyn.editor.blocks.OscillatorHQModel;
import org.modsyn.editor.blocks.OscillatorModel;
import org.modsyn.editor.blocks.PanPotModel;
import org.modsyn.editor.blocks.PhaserModel;
import org.modsyn.editor.blocks.PitcherModel;
import org.modsyn.editor.blocks.RingModulatorModel;
import org.modsyn.editor.blocks.ScopeModel;
import org.modsyn.editor.blocks.ScopeModel.Scope;
import org.modsyn.editor.blocks.SoftClipModel;
import org.modsyn.editor.blocks.SpeakerModel;
import org.modsyn.editor.blocks.TipScaleModel;
import org.modsyn.editor.blocks.VUMeterModel;
import org.modsyn.editor.blocks.VeloSensModel;
import org.modsyn.editor.blocks.VocoderModel;
import org.modsyn.gui.JKnob;
import org.modsyn.modules.ADR;
import org.modsyn.modules.Compressor;
import org.modsyn.modules.Tracker;
import org.modsyn.modules.ctrl.ADSREnvelope;
import org.modsyn.modules.ext.AudioInSupport;
import org.modsyn.modules.ext.AudioOutSupport;
import org.modsyn.modules.ext.MidiVoicePolyAdapter;
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
			return new DspBlockComponent(c, new AmplifierModel(), pm);
		}
	},
	Mix("Basics") {
		@Override
		public String getModelName() {
			return MixerModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new MixerModel(c, getChannelsOrAsk(channels, "channels", 2, 32)), pm);
		}
	},
	Add("Basics") {
		@Override
		public String getModelName() {
			return AdderModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new AdderModel(), pm);
		}
	},
	Split("Basics") {
		@Override
		public String getModelName() {
			return MultiSplitterModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new MultiSplitterModel(getChannelsOrAsk(channels, "channels", 2, 16)), pm);
		}
	},
	Pan("Basics") {
		@Override
		public String getModelName() {
			return PanPotModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new PanPotModel(), pm);
		}
	},
	Osc("Oscillators") {
		@Override
		public String getModelName() {
			return OscillatorModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new OscillatorModel(c), pm);
		}
	},
	Osc__HQ("Oscillators") {
		@Override
		public String getModelName() {
			return OscillatorHQModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new OscillatorHQModel(c), pm);
		}
	},
	Noise("Oscillators") {
		@Override
		public String getModelName() {
			return NoiseModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new NoiseModel(c), pm);
		}
	},
	K__Str("Oscillators") {
		@Override
		public String getModelName() {
			return KarplusStrongModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new KarplusStrongModel(c), pm);
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
			return new DspBlockComponent(c, new ArpeggioModel(c, channels), pm);
		}
	},
	ADR("Dynamics") {
		@Override
		public String getModelName() {
			return ADRModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new ADRModel(new ADR(c)), pm);
		}
	},
	ADSR("Dynamics") {
		@Override
		public String getModelName() {
			return ADSRModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new ADSRModel(new ADSREnvelope(c)), pm);
		}
	},
	Tracker("Dynamics") {
		@Override
		public String getModelName() {
			return EnvelopeFollowerModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new EnvelopeFollowerModel(new Tracker()), pm);
		}
	},
	TipScale("Dynamics") {
		@Override
		public String getModelName() {
			return TipScaleModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new TipScaleModel(), pm);
		}
	},
	VeloSens("Dynamics") {
		@Override
		public String getModelName() {
			return VeloSensModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new VeloSensModel(), pm);
		}
	},
	Compressor("Dynamics") {
		@Override
		public String getModelName() {
			return CompressorModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new CompressorModel(new Compressor()), pm);
		}
	},
	xxx4__Pole("Filters") {
		@Override
		public String getModelName() {
			return Filter4PoleModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new Filter4PoleModel(c), pm);
		}
	},
	xxx8__Pole("Filters") {
		@Override
		public String getModelName() {
			return Filter8PoleModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new Filter8PoleModel(c), pm);
		}
	},
	X__Pole("Filters") {
		@Override
		public String getModelName() {
			return FilterXPoleModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new FilterXPoleModel(c), pm);
		}
	},
	MoogVCF("Filters") {
		@Override
		public String getModelName() {
			return MoogVCFModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new MoogVCFModel(c), pm);
		}
	},
	LPF("Filters") {
		@Override
		public String getModelName() {
			return LPFModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new LPFModel(c), pm);
		}
	},
	Resonator("Filters") {
		@Override
		public String getModelName() {
			return Karlsen24dBModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new Karlsen24dBModel(c), pm);
		}
	},
	APF("Filters") {
		@Override
		public String getModelName() {
			return APFModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new APFModel(c), pm);
		}
	},
	Vocoder("Filters") {
		@Override
		public String getModelName() {
			return VocoderModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new VocoderModel(), pm);
		}
	},
	Speaker("Filters") {
		@Override
		public String getModelName() {
			return SpeakerModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new SpeakerModel(), pm);
		}
	},
	SoftClip("Shape") {
		@Override
		public String getModelName() {
			return SoftClipModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new SoftClipModel(), pm);
		}
	},
	Absolute("Shape") {
		@Override
		public String getModelName() {
			return AbsoluteModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new AbsoluteModel(), pm);
		}
	},
	Chorus("FX") {
		@Override
		public String getModelName() {
			return ChorusModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new ChorusModel(c), pm);
		}
	},
	Phaser("FX") {
		@Override
		public String getModelName() {
			return PhaserModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new PhaserModel(c), pm);
		}
	},
	Octaver("FX") {
		@Override
		public String getModelName() {
			return OctaverModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new OctaverModel(), pm);
		}
	},
	PitchShift("FX") {
		@Override
		public String getModelName() {
			return PitcherModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new PitcherModel(), pm);
		}
	},
	RingMod("FX") {
		@Override
		public String getModelName() {
			return RingModulatorModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new RingModulatorModel(c), pm);
		}
	},
	Binaural("FX") {
		@Override
		public String getModelName() {
			return BinauralModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return new DspBlockComponent(c, new BinauralModel(c), pm);
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

			DspBlockComponent dbc = new DspBlockComponent(c, km, pm, 0, 0, 80, 80) {
				private static final long serialVersionUID = 603933338049577236L;

				@Override
				public Component createCenterComponent() {
					return knob;
				}
			};

			dbc.setBounds(dbc.getX(), dbc.getY(), dbc.getWidth(), 80);
			return dbc;
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
			DspBlockComponent dbc = new DspBlockComponent(c, vmm, pm, 0, 0, 80, 80) {
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
			dbc.setBounds(dbc.getX(), dbc.getY(), dbc.getWidth(), 80);
			return dbc;
		}
	},
	Scope("Misc") {
		@Override
		public String getModelName() {
			return ScopeModel.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			final ScopeModel vmm = new ScopeModel();
			final Scope wv = vmm.getDspObject();
			DspBlockComponent dbc = new DspBlockComponent(c, vmm, pm, 0, 0, 240, 240) {
				private static final long serialVersionUID = -8093488546616747252L;

				@Override
				public Component createCenterComponent() {
					JComponent c = new JComponent() {
						private static final long serialVersionUID = 6430783866717213954L;
						final Color bg = new Color(0x000080);
						final Color bgl = new Color(0x0000e0);
						final Color bgp = new Color(0x0000b0);
						int prev;

						@Override
						public void paintComponent(Graphics g) {
							final int h = getHeight();
							final int w = getWidth();
							final int c = h / 2;
							final int p1 = (int) (c * wv.amp);

							int p = wv.ptr - w;
							while (p < 0) {
								p += wv.wave.length;
							}
							g.setColor(bg);
							g.fillRect(0, 0, w, h);
							g.setColor(bgl);
							g.fillRect(0, c, w, 1);
							g.setColor(bgp);
							g.fillRect(0, c - p1, w, 1);
							g.fillRect(0, c + p1, w, 1);

							g.setColor(Color.GREEN);

							// System.out.println("P=" + p);
							for (int i = 0; i < w; i++) {
								float signal = wv.wave[p++];
								p %= wv.wave.length;
								int u = (int) (signal * wv.amp * c);

								g.drawLine(i - 1, prev, i, c + u);
								prev = c + u;

								// g.fillRect(i, c + u, 1, 1);

								// if (u < 0) {
								// g.fillRect(i, c + u, 1, -u);
								// } else {
								// g.fillRect(i, c, 1, u + 1);
								// }
							}
							// System.out.println("...P=" + p);
						}
					};
					return c;
				}
			};
			dbc.setBounds(dbc.getX(), dbc.getY(), dbc.getWidth(), 240);
			return dbc;
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
			DspBlockComponent block = new DspBlockComponent(c, model, pm);
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
			return new DspBlockComponent(c, new FromMidiPolyModel(new MidiVoicePolyAdapter(channels)), pm);
		}
	},
	Audio_IN("EXT") {
		@Override
		public String getModelName() {
			return AudioInSupport.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return AudioInSupport.create(c, pm, channels);
		}
	},
	Audio_OUT("EXT") {
		@Override
		public String getModelName() {
			return AudioOutSupport.class.getName();
		}

		@Override
		public DspBlockComponent create(Context c, DspPatchModel pm, int channels) {
			return AudioOutSupport.create(c, pm, channels);
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

		if (AudioInSupport.normalizeClassName(modelName) != null) {
			return AudioInSupport.create(c, pm, channels);
		}
		if (AudioOutSupport.normalizeClassName(modelName) != null) {
			return AudioOutSupport.create(c, pm, channels);
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
