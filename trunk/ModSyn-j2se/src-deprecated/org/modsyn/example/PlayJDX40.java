/*
 * Created on Jun 17, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.example;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.PolyphonicSynth;
import org.modsyn.Synth;
import org.modsyn.modules.Amplifier;
import org.modsyn.modules.Mixer;
import org.modsyn.modules.ext.ToJavaSound;
import org.modsyn.synth.JDX40;
import org.modsyn.util.GuiTools;
import org.modsyn.util.Keyboard;

public class PlayJDX40 extends JFrame {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	AudioThread audio;
	Thread thread;

	private final Context context;

	/**
	 * Constructor
	 */
	public PlayJDX40(Context context) {
		super();
		this.context = context;
		audio = new AudioThread(context);
		thread = new Thread(audio);
		thread.start();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		genGui();
		pack();
		setVisible(true);
	}

	/**
     *  
     */
	private void genGui() {
		this.setTitle("Demo JDX-40");
		this.setResizable(false);
		JPanel contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		/*
		 * Close window event
		 */
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});

		Keyboard kb = new Keyboard(audio.synth);
		this.addKeyListener(kb);

		contentPane.add(GuiTools.createTitlePanel("JDX-40 demo",
				"<HTML>Use the keyboard to play the 'JDX-40' synth.<BR>This synth is an FM synthesizer with 4 operators.</HTML>"), BorderLayout.CENTER);
		contentPane.add(GuiTools.createLinkedComponent(audio.patch), BorderLayout.SOUTH);
		GuiTools.addActionListenerToAll(contentPane, kb);

	}

	public static void main(String args[]) {
		new PlayJDX40(ContextFactory.create());
	}

	public class AudioThread implements Runnable {

		public float oscfreq = 300;
		public float mod = 80;

		PolyphonicSynth synth;
		Synth[] patch = new Synth[6];

		public AudioThread(Context context) {

			// Master for play back using javax.sound
			ToJavaSound master = new ToJavaSound(context, ToJavaSound.STEREO, 4096);
			Mixer mixerL = new Mixer(context);
			Mixer mixerR = new Mixer(context);
			Amplifier outL = new Amplifier();
			Amplifier outR = new Amplifier();

			for (int i = 0; i < patch.length; i++) {
				patch[i] = new JDX40(context);
				mixerL.addChannel(patch[i].getOutput(Synth.LEFT));
				mixerR.addChannel(patch[i].getOutput(Synth.RIGHT));
			}

			synth = new PolyphonicSynth(patch);

			mixerL.connectTo(outL);
			mixerR.connectTo(outR);
			outL.control.set(1f / 6f);
			outR.control.set(1f / 6f);
			outL.connectTo(master.inputL);
			outR.connectTo(master.inputR);

			master.setGain(25000);
		}

		@Override
		public void run() {
			while (true) {
				context.update();
			}
		}
	}
}