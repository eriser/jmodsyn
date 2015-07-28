package org.modsyn.gui;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class JIcon extends JLabel {

	public JIcon(Icon image, Color bg) {
		super(image);
		setBackground(bg);
		setOpaque(true);
		setHorizontalAlignment(JLabel.CENTER);
		setVerticalAlignment(JLabel.CENTER);
	}
}
