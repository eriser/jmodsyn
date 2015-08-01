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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.modsyn.Context;
import org.modsyn.ContextFactory;
import org.modsyn.editor.io.FileSys;
import org.modsyn.editor.io.XmlExport;
import org.modsyn.editor.io.XmlExportMeta;
import org.modsyn.editor.io.XmlImport;
import org.modsyn.editor.io.XmlImportMeta;
import org.modsyn.modules.ext.AsioSupport;
import org.modsyn.util.IOTransferTool;

public class PatchEditor {

	private static final String FILE_SUFFIX = ".dsp-patch";
	private static final String TEMPLATE_PREFIX = ".template";
	private static final String PATCH_EDITOR = "PatchEditor - ";

	private static final JFrame frame = new JFrame("PatchEditor");

	private static String getFileName() {
		return frame.getTitle().length() <= PATCH_EDITOR.length() ? null : frame.getTitle().substring(PATCH_EDITOR.length());
	}

	public static void main(String[] args) throws Exception {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				final Context context = ContextFactory.create();

				final DspPatchCombinationModel pcModel = new DspPatchCombinationModel(context);
				final DspPatchCombinationComponent pcComponent = new DspPatchCombinationComponent(context, pcModel);

				JToolBar toolBar = new JToolBar();
				final JButton btnLoad = new JButton("Load");
				btnLoad.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser fc = createFileChooser(FileSys.dirPatches);
						int response = fc.showOpenDialog(btnLoad);
						if (response == JFileChooser.APPROVE_OPTION) {
							pcModel.clear();
							try {
								File f = fc.getSelectedFile();
								if (!f.getName().endsWith(FILE_SUFFIX)) {
									f = new File(f.getAbsolutePath() + FILE_SUFFIX);
								}
								new XmlImport(f, context, pcModel);
								frame.setTitle(PATCH_EDITOR + f.getName());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});

				final JButton btnSave = new JButton("Save");
				btnSave.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser fc = createFileChooser(FileSys.dirPatches);
						if (getFileName() != null) {
							fc.setSelectedFile(new File(FileSys.dirPatches, getFileName()));
						}
						int response = fc.showSaveDialog(btnSave);
						if (response == JFileChooser.APPROVE_OPTION) {
							try {
								File f = fc.getSelectedFile();
								if (!f.getName().endsWith(FILE_SUFFIX)) {
									f = new File(f.getAbsolutePath() + FILE_SUFFIX);
								}
								boolean isRenamed = !f.getName().equalsIgnoreCase(getFileName());

								frame.setTitle(PATCH_EDITOR + f.getName());

								for (DspPatchModel pm : pcModel.getLinkedSubModels()) {
									String pmname = pm.name;

									boolean isTemplate = pmname.startsWith(TEMPLATE_PREFIX) && !f.getName().startsWith(TEMPLATE_PREFIX);

									if (isTemplate || isRenamed) {
										// rename templates
										JFileChooser fcm = createFileChooser(FileSys.dirMeta);
										if (isTemplate) {
											JOptionPane.showConfirmDialog(btnSave,
													"Your patch includes a template.\nIt's recommended to save under a different name.", "Rename " + pmname,
													JOptionPane.YES_NO_OPTION);
										} else {
											fcm.setSelectedFile(new File(FileSys.dirMeta, pmname + FILE_SUFFIX));
										}

										int responsem = fcm.showSaveDialog(btnSave);
										if (responsem == JFileChooser.APPROVE_OPTION) {
											File frm = fcm.getSelectedFile();
											if (!frm.getName().endsWith(FILE_SUFFIX)) {
												frm = new File(frm.getAbsolutePath() + FILE_SUFFIX);
											}

											pcModel.renameLinkedSubModels(pmname, frm.getName());
										}
									}
									File fm = new File(FileSys.dirMeta, pm.name + FILE_SUFFIX);
									new IOTransferTool().saveString(new XmlExportMeta(pm.getDspBlocks(), pm.getDspConnections()).toString(), "utf-8", fm);
								}

								new IOTransferTool().saveString(new XmlExport(pcModel.getMainModel()).toString(), "utf-8", f);

								// reload, just to be sure...
								pcModel.clear();
								new XmlImport(f, context, pcModel);
								frame.setTitle(PATCH_EDITOR + f.getName());

							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});
				final JButton btnClear = new JButton("Clear");
				btnClear.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						pcModel.clear();
						frame.setTitle("PatchEditor");
					}
				});

				final JButton btnImportMeta = new JButton("Import META");
				btnImportMeta.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						JFileChooser fc = createFileChooser(FileSys.dirMeta);
						int response = fc.showOpenDialog(btnImportMeta);
						if (response == JFileChooser.APPROVE_OPTION) {
							try {
								File f = fc.getSelectedFile();
								if (!f.getName().endsWith(FILE_SUFFIX)) {
									f = new File(f.getAbsolutePath() + FILE_SUFFIX);
								}
								new XmlImportMeta(f, context, pcModel, pcModel.getMainModel());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});
				final JButton btnExportMeta = new JButton("Export META");
				btnExportMeta.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser fc = createFileChooser(FileSys.dirMeta);
						int response = fc.showSaveDialog(btnExportMeta);
						if (response == JFileChooser.APPROVE_OPTION) {
							try {
								File f = fc.getSelectedFile();
								if (!f.getName().endsWith(FILE_SUFFIX)) {
									f = new File(f.getAbsolutePath() + FILE_SUFFIX);
								}

								new IOTransferTool().saveString(new XmlExportMeta(pcModel.getMainModel(), true).toString(), "utf-8", f);
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
				toolBar.add(btnImportMeta);
				toolBar.add(btnExportMeta);

				JPanel p = new JPanel(new BorderLayout());
				p.add(pcComponent, BorderLayout.CENTER);
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

	private static JFileChooser createFileChooser(File dir) {
		JFileChooser fc = new JFileChooser(dir);
		fc.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "DSP patch";
			}

			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(FILE_SUFFIX);
			}
		});
		return fc;
	}
}
