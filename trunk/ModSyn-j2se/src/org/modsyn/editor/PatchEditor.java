package org.modsyn.editor;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.editor.io.FileSys;
import org.modsyn.editor.io.XmlExport;
import org.modsyn.editor.io.XmlExportMeta;
import org.modsyn.editor.io.XmlExportMidi;
import org.modsyn.editor.io.XmlImport;
import org.modsyn.editor.io.XmlImportMeta;
import org.modsyn.modules.ext.AsioSupport;
import org.modsyn.util.IOTransferTool;

public class PatchEditor {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final JFrame frame = new JFrame("PatchEditor");

				final Context context = ContextFactory.create();

				final DspPatchModel model = new DspPatchModel(context);
				final DspPatchComponent cmpPatch = new DspPatchComponent(context, model);

				DspPaletteComponent cmpPalette = new DspPaletteComponent(context, model);

				JToolBar toolBar = new JToolBar();
				JButton btnLoad = new JButton("Load");
				btnLoad.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser fc = new JFileChooser(FileSys.dirPatches);
						fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
						fc.setFileFilter(new FileFilter() {

							@Override
							public String getDescription() {
								return "DSP patch";
							}

							@Override
							public boolean accept(File f) {
								return f.getName().endsWith(".dsp-patch") || f.isDirectory();
							}
						});
						int response = fc.showOpenDialog(cmpPatch);
						if (response == JFileChooser.APPROVE_OPTION) {
							model.clear();
							try {
								File f = fc.getSelectedFile();
								if (!f.getName().endsWith(".dsp-patch")) {
									f = new File(f.getAbsolutePath() + ".dsp-patch");
								}
								new XmlImport(f, context, model);
								frame.setTitle("PatchEditor - " + f.getName());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});

				JButton btnSave = new JButton("Save");
				btnSave.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser fc = new JFileChooser(FileSys.dirPatches);
						fc.setFileFilter(new FileFilter() {

							@Override
							public String getDescription() {
								return "DSP patch";
							}

							@Override
							public boolean accept(File f) {
								return f.getName().endsWith(".dsp-patch");
							}
						});
						int response = fc.showSaveDialog(cmpPatch);
						if (response == JFileChooser.APPROVE_OPTION) {
							try {
								File f = fc.getSelectedFile();
								if (!f.getName().endsWith(".dsp-patch")) {
									f = new File(f.getAbsolutePath() + ".dsp-patch");
								}
								new IOTransferTool().saveString(new XmlExport(model).toString(), "utf-8", f);
								frame.setTitle("PatchEditor - " + f.getName());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});
				JButton btnClear = new JButton("Clear");
				btnClear.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						model.clear();
						frame.setTitle("PatchEditor");
					}
				});
				JButton btnExportMidi = new JButton("Export MIDI Voice");
				btnExportMidi.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser fc = new JFileChooser(FileSys.dirMeta);
						fc.setFileFilter(new FileFilter() {

							@Override
							public String getDescription() {
								return "DSP patch";
							}

							@Override
							public boolean accept(File f) {
								return f.getName().endsWith(".dsp-patch");
							}
						});
						int response = fc.showSaveDialog(cmpPatch);
						if (response == JFileChooser.APPROVE_OPTION) {
							try {
								File f = fc.getSelectedFile();
								if (!f.getName().endsWith(".dsp-patch")) {
									f = new File(f.getAbsolutePath() + ".dsp-patch");
								}

								new IOTransferTool().saveString(new XmlExportMidi(model).toString(), "utf-8", f);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});

				JButton btnImportMeta = new JButton("Import META");
				btnImportMeta.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser fc = new JFileChooser(FileSys.dirMeta);
						fc.setFileFilter(new FileFilter() {

							@Override
							public String getDescription() {
								return "DSP patch";
							}

							@Override
							public boolean accept(File f) {
								return f.getName().endsWith(".dsp-patch");
							}
						});
						int response = fc.showOpenDialog(cmpPatch);
						if (response == JFileChooser.APPROVE_OPTION) {
							try {
								File f = fc.getSelectedFile();
								if (!f.getName().endsWith(".dsp-patch")) {
									f = new File(f.getAbsolutePath() + ".dsp-patch");
								}
								new XmlImportMeta(f, context, model);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});
				JButton btnExportMeta = new JButton("Export META");
				btnExportMeta.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser fc = new JFileChooser(FileSys.dirMeta);
						fc.setFileFilter(new FileFilter() {

							@Override
							public String getDescription() {
								return "DSP patch";
							}

							@Override
							public boolean accept(File f) {
								return f.getName().endsWith(".dsp-patch");
							}
						});
						int response = fc.showSaveDialog(cmpPatch);
						if (response == JFileChooser.APPROVE_OPTION) {
							try {
								File f = fc.getSelectedFile();
								if (!f.getName().endsWith(".dsp-patch")) {
									f = new File(f.getAbsolutePath() + ".dsp-patch");
								}

								new IOTransferTool().saveString(new XmlExportMeta(model).toString(), "utf-8", f);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});

				toolBar.add(btnLoad);
				toolBar.add(btnSave);
				toolBar.add(new JLabel("   "));
				toolBar.add(btnClear);
				toolBar.add(new JLabel("   "));
				toolBar.add(btnExportMidi);
				toolBar.add(new JLabel("   "));
				toolBar.add(btnImportMeta);
				toolBar.add(btnExportMeta);

				JScrollPane scrollC = new JScrollPane(cmpPatch, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scrollC.setBorder(new EmptyBorder(0, 0, 0, 0));
				scrollC.getVerticalScrollBar().setUnitIncrement(16);
				scrollC.getHorizontalScrollBar().setUnitIncrement(16);

				JPanel pCenter = new JPanel(new BorderLayout());
				pCenter.add(scrollC, BorderLayout.CENTER);
				pCenter.add(new DspBlockEditPanel(cmpPatch), BorderLayout.SOUTH);

				JPanel p = new JPanel(new BorderLayout());
				p.add(cmpPalette, BorderLayout.WEST);
				p.add(pCenter, BorderLayout.CENTER);
				p.add(toolBar, BorderLayout.NORTH);

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
