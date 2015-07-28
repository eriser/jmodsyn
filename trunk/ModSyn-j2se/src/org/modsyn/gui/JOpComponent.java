package org.modsyn.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import org.modsyn.editor.DspBlockModel;
import org.modsyn.editor.EditorTheme;

@SuppressWarnings("serial")
public class JOpComponent extends JComponent {

	private final DspBlockModel<?> model;
	private final String label;
	private final int lw, lh, lwb, lhb;
	private int yOffset;
	private final Color fontColor;

	public JOpComponent(DspBlockModel<?> model, String label) {
		this(model, label, Color.WHITE);
	}

	public JOpComponent(DspBlockModel<?> model, String label, Color fontColor) {
		super();
		this.model = model;
		this.label = label;
		this.fontColor = fontColor;
		setOpaque(true);
		setForeground(Color.BLACK);
		setBackground(EditorTheme.COLOR_BASIC_BLOCK_BG);

		setFont(EditorTheme.FONT_OPERATOR);

		FontMetrics metrics = getFontMetrics(getFont());
		Rectangle2D r2d = metrics.getStringBounds(label, getGraphics());

		lw = Math.max((int) r2d.getWidth(), 4);
		lh = lw;
		if ("+".equals(label)) {
			yOffset = 2;
		} else if ("\u2211".equals(label)) {
			yOffset = 0;
		} else {
			yOffset = -1;
		}

		lwb = (int) (lw * 1.6);
		lhb = (int) (lh * 1.6);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int w = getWidth();
		int h = getHeight();

		g.setColor(getBackground());
		g.fillRect(0, 0, w, h);

		float hUnit = h / (float) Math.max(model.getInputs().size(), model.getOutputs().size());
		int yHalf = (int) ((((model.getInputs().size() * hUnit * 0.5f) + (model.getOutputs().size()) * hUnit * 0.5f)) / 2);
		int xHalf = w / 2;

		for (int i = 0; i < model.getInputs().size(); i++) {
			if (model.getInputs().get(i).getName().toLowerCase().startsWith("in")) {
				g.setColor(getForeground());
			} else {
				g.setColor(EditorTheme.COLOR_CTRL_LINE);
			}
			g.drawLine(0, (int) ((i * hUnit) + (hUnit / 2)), xHalf, yHalf);
		}
		g.setColor(getForeground());
		for (int i = 0; i < model.getOutputs().size(); i++) {
			g.drawLine(w, (int) ((i * hUnit) + (hUnit / 2)), xHalf, yHalf);
		}
		g.fillOval(xHalf - lwb / 2, yHalf - lhb / 2, lwb, lhb);

		g.setColor(fontColor);
		g.drawString(label, xHalf - lw / 2, yOffset + yHalf + lh / 2 - 1);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}
