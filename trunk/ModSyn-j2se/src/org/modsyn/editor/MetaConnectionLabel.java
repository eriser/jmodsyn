package org.modsyn.editor;

import java.awt.Graphics;

import javax.swing.JLabel;

public class MetaConnectionLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3273306472666033456L;

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
		g.fillPolygon(new int[] { getWidth() - (getHeight() / 2), getWidth(), getWidth(), getWidth() - (getHeight() / 2), getWidth() }, new int[] { 0, 0,
				getHeight(), getHeight(), getHeight() / 2 }, 5);
	}
}
