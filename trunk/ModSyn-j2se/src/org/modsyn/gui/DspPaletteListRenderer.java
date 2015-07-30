package org.modsyn.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.modsyn.editor.DspPalette;

@SuppressWarnings("serial")
public class DspPaletteListRenderer extends JLabel implements ListCellRenderer<DspPalette> {

	private final Color categoryColor;

	private final int gradientWidth = 16;

	public DspPaletteListRenderer(Color categoryColor) {
		super();
		setOpaque(true);
		this.categoryColor = categoryColor;
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (!isOpaque()) {
			super.paintComponent(g);
			return;
		}

		int w = getWidth();
		int h = getHeight();

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(getBackground());
		g2d.fillRect(0, 0, w, h);

		GradientPaint gp = new GradientPaint(w - gradientWidth, 0, getBackground(), w - gradientWidth / 2, 0, categoryColor);

		g2d.setPaint(gp);
		g2d.fillRect(w - gradientWidth, 0, gradientWidth, h - 1);

		setOpaque(false);
		super.paintComponent(g);
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends DspPalette> list, DspPalette value, int index, boolean isSelected, boolean cellHasFocus) {
		setText(value.toString());

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		return this;
	}
}
