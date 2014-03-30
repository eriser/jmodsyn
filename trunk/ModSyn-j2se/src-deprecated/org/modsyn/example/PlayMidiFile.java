package org.modsyn.example;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.Device;
import org.modsyn.PolyphonicSynth;
import org.modsyn.SignalOutput;
import org.modsyn.Synth;
import org.modsyn.midi.DefaultMIDIEventListener;
import org.modsyn.midi.MIDIFile;
import org.modsyn.midi.Player;
import org.modsyn.modules.FxLoop;
import org.modsyn.modules.Mixer;
import org.modsyn.modules.ext.ToJavaSound;
import org.modsyn.modules.fx.CrudeVerb;
import org.modsyn.synth.JDX20;
import org.modsyn.synth.Subtractive1;
import org.modsyn.util.GuiTools;

public class PlayMidiFile extends JFrame {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	private static final String[] labels = { "Start", "Exit" };

	private final JButton btn = new JButton(labels[0]);
	private final Context context;

	private int status = 0;
	private RunMidi audio;
	private Thread thread;

	private class RunMidi implements Runnable {

		ToJavaSound master;
		Mixer mixerL;
		Mixer mixerR;

		PolyphonicSynth[] synth = new PolyphonicSynth[16];

		Player player;
		private final Context context;

		public RunMidi(Context context, String path) throws IOException {
			this.context = context;
			MIDIFile mf = new MIDIFile(path);
			master = new ToJavaSound(context, ToJavaSound.STEREO, 2048 * 8);
			mixerL = new Mixer(context);
			mixerR = new Mixer(context);
			player = new Player(context, mf, context.getSampleRate());
		}

		public void stop() {
			player.stop();
		}

		@Override
		public void run() {

			// create synths for each MIDI channel
			player.setEventListener(0, new DefaultMIDIEventListener(
					createSynth(0, 1)));
			player.setEventListener(1, new DefaultMIDIEventListener(
					createSynth(1, 4)));
			player.setEventListener(2, new DefaultMIDIEventListener(
					createSynth(2, 1)));
			player.setEventListener(3, new DefaultMIDIEventListener(
					createSynth(3, 3)));
			player.setEventListener(4, new DefaultMIDIEventListener(
					createSynth(4, 1)));
			player.setEventListener(5, new DefaultMIDIEventListener(
					createSynth(5, 3)));
			player.setEventListener(6, new DefaultMIDIEventListener(
					createSynth(6, 1)));

			// alter the default parameters
			for (int i = 0; i < synth[0].voices.length; i++) {
				Device d = (Device) synth[0].voices[i];
				d.getSubDevices()[0].getDeviceControls()[1]
						.setControlValue(0.2f);
				d.getSubDevices()[1].getDeviceControls()[0].setControlValue(2f);
			}
			// for (int i = 0; i < synth[4].voices.length; i++) {
			// Device d = (Device) synth[4].voices[i];
			// d.getSubDevices()[0].getParameterControls()[1].setControlValue(0.2f);
			// }

			synth[1].setControl(Subtractive1.CONTROL_FILTER_ATTACK_TIME, 0.01f);
			synth[1].setControl(Subtractive1.CONTROL_AMP_ATTACK_TIME, 0.001f);
			synth[1].setControl(Subtractive1.CONTROL_FILTER_DECAY_TIME, 0.5f);

			// flute
			// synth[4].setControl(Subtractive1.CONTROL_WAVEFORM_SELECT,
			// WaveTables.SHAPE_ID_TRIANGLE);
			synth[4].setControl(Subtractive1.CONTROL_AMP_ATTACK_TIME, 0.001f);
			synth[4].setControl(Subtractive1.CONTROL_FILTER_ATTACK_TIME, 0.1f);
			synth[4].setControl(Subtractive1.CONTROL_FILTER_ATTACK_LEVEL, 1000f);
			synth[4].setControl(Subtractive1.CONTROL_FILTER_DECAY_TIME, 0.01f);
			synth[4].setControl(Subtractive1.CONTROL_FILTER_DECAY_LEVEL, 3000);
			synth[4].setControl(Subtractive1.CONTROL_FILTER_SUSTAIN_LEVEL, 3000);
			synth[4].setControl(Subtractive1.CONTROL_FILTER_RELEASE_TIME, 1f);
			synth[4].setControl(Subtractive1.CONTROL_FILTER_RELEASE_LEVEL, 3000);

			synth[3].setControl(Subtractive1.CONTROL_FILTER_ATTACK_TIME, 0.001f);
			synth[3].setControl(Subtractive1.CONTROL_AMP_ATTACK_TIME, 0.001f);
			synth[3].setControl(Subtractive1.CONTROL_FILTER_DECAY_TIME, 0.2f);
			synth[3].setControl(Subtractive1.CONTROL_FILTER_RESONANCE, 1);
			synth[3].setControl(Subtractive1.CONTROL_VIBRATO_AMOUNT, 0.005f);

			// synth[6].setControl(Subtractive1.cONTROL_WAVEFORM_SELECT,
			// WaveTables.SHAPE_ID_SQUARE);
			synth[6].setControl(Subtractive1.CONTROL_FILTER_RESONANCE, 6);
			// synth[6].setControl(Subtractive1.CONTROL_FILTER_DECAY_LEVEL,
			// 2000);
			/**/

			mixerL.connectTo(master.inputL);
			mixerR.connectTo(master.inputR);

			master.setGain(30000);

			player.run();

		}

		private PolyphonicSynth createSynth(int channel, int voices) {

			Synth[] patch = new Synth[voices];
			boolean reverb = false;
			for (int i = 0; i < patch.length; i++) {
				switch (channel) {
				case 0:
					patch[i] = new JDX20(context);
					break;
				case 3:
					reverb = true; // fall through
				default:
					patch[i] = new Subtractive1(context);
					break;
				}
				SignalOutput outL;
				SignalOutput outR;
				if (reverb) {
					FxLoop fxL = new FxLoop(context, new CrudeVerb(context,
							1.3f, 0.95f));
					FxLoop fxR = new FxLoop(context, new CrudeVerb(context,
							1.4f, 0.95f));
					fxL.setFxLevel(0.5f);
					fxR.setFxLevel(0.5f);

					outL = fxL;
					outR = fxR;
					patch[i].getOutput(Synth.LEFT).connectTo(fxL);
					patch[i].getOutput(Synth.RIGHT).connectTo(fxR);
				} else {
					outL = patch[i].getOutput(Synth.LEFT);
					outR = patch[i].getOutput(Synth.RIGHT);
				}
				mixerL.addChannel(outL);
				mixerR.addChannel(outR);
			}

			PolyphonicSynth synth = new PolyphonicSynth(patch);
			this.synth[channel] = synth;

			return synth;
		}
	}

	/**
	 * Constructor
	 */
	public PlayMidiFile(Context context) {
		super();
		this.context = context;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		genGui();
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
     * 
     */
	private void genGui() {
		this.setTitle("Demo MIDI file");
		this.setResizable(false);
		JPanel contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (status == 0) {
					try {
						audio = new RunMidi(context, "test.mid");
						thread = new Thread(audio);
						thread.start();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					audio.stop();
					thread = null;
					System.exit(0);
				}
				status ^= 1;
				btn.setText(labels[status]);
			}
		});

		contentPane
				.add(GuiTools
						.createTitlePanel(
								"MIDI file demo",
								"<HTML>Press 'start' to play a MIDI file with the theme from the movie 'A Clockwork Orange'.<BR>The song is played through multiple MIDI channels, Reverb is used on one channel,<BR>and two types of synths are used: A subtractive synth, and an FM synth.</HTML>"),
						BorderLayout.CENTER);
		contentPane.add(btn, BorderLayout.SOUTH);

	}

	public static void main(String args[]) {
		new PlayMidiFile(ContextFactory.create());
	}
}