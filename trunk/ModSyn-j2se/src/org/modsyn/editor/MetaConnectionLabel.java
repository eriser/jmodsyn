package org.modsyn.editor;

import java.awt.Graphics;

import javax.swing.JLabel;

public class MetaConnectionLabel extends JLabel {

	public MetaConnectionLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	public MetaConnectionLabel(String text) {
		super(text);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(getParent().getBackground());
		// @formatter:off
		g.fillPolygon(new int[] { getWidth() - (getHeight() / 2), getWidth(), getWidth(), getWidth() - (getHeight() / 2), getWidth() }, new int[] { 0, 0,
				getHeight(), getHeight(), getHeight() / 2 }, 5);
		// @formatter:on
	}
}
