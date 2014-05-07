package org.modsyn.editor;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.editor.io.FileSys;
import org.modsyn.editor.io.XmlImport;
import org.modsyn.modules.ext.AsioSupport;

public class PatchPlayer {

	static final String[] patches = { "dirty-rhodes", "dirty-rhodes-compressed", "dirty-rhodes-compressed-phased", "simple-synth", "synth4", "karplus-clavi",
			"tubelead-synth", "multi-vocoder" };

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final JFrame frame = new JFrame("PatchEditor");

				final Context context = ContextFactory.create();

				final DspPatchModel model = new DspPatchModel(context);

				JPanel p = new JPanel(new GridLayout(2, 4));
				for (final String patch : patches) {
					JButton b = new JButton(patch);
					b.setFont(new Font("Arial", Font.BOLD, 25));
					b.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							model.clear();
							try {
								new XmlImport(new File(FileSys.dirPatches, patch + ".dsp-patch"), context, model);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					p.add(b);
				}

				frame.add(p);
				frame.pack();
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						AsioSupport.INSTANCE.stop();
						System.exit(0);
					}
				});
				frame.setExtendedState(Frame.MAXIMIZED_BOTH);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
