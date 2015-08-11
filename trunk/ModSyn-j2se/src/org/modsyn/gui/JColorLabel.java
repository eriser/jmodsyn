package org.modsyn.gui;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.modsyn.editor.EditorTheme;

@SuppressWarnings("serial")
public class JColorLabel extends JLabel {
	public JColorLabel(Color bg) {
		this("", bg);
	}

	public JColorLabel(String label, Color bg) {
		super(label);
		setFont(EditorTheme.FONT_COLORLABEL);
		setForeground(Color.WHITE);
		setBackground(bg);
		setOpaque(true);
		setHorizontalAlignment(JLabel.CENTER);
		setVerticalAlignment(JLabel.CENTER);
	}

	public JColorLabel(Icon image, Color bg) {
		super(image);
		setBackground(bg);
		setOpaque(true);
		setHorizontalAlignment(JLabel.CENTER);
		setVerticalAlignment(JLabel.CENTER);
	}
}
