package org.modsyn.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.EditorTheme;
import org.modsyn.util.IOTransferTool;

@SuppressWarnings("serial")
public class HelpDialog extends EscapeDialog {

	public HelpDialog(DspBlockComponent block) {
		super(null, false);
		setLayout(new BorderLayout());
		setUndecorated(true);
		String name = "/" + block.getModel().getClass().getName().replace('.', '_') + ".html";
		IOTransferTool io = new IOTransferTool();
		String html = null;
		try {
			html = io.loadString(HelpDialog.class.getResource(name));
			JLabel lbl = new JLabel(html, JLabel.LEFT);
			lbl.setBackground(EditorTheme.COLOR_HELP_BG);
			lbl.setOpaque(true);
			lbl.setBorder(new CompoundBorder(new LineBorder(EditorTheme.COLOR_SELECTED_BG), new EmptyBorder(0, 4, 0, 8)));

			add(lbl, BorderLayout.CENTER);
			pack();
			// setLocationRelativeTo(block);
			setLocation(block.getLocationOnScreen().x + block.getWidth(), block.getY());
			setVisible(true);
		} catch (Exception e) {
			System.out.println("No help for " + name);
			dispose();
		}
	}
}