package org.modsyn.editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;

public class DspConnection {

	private static final BasicStroke selected = new BasicStroke(2.0f);
	private static final BasicStroke normal = new BasicStroke(1);

	public final DspBlockComponent from;
	public final OutputModel fromSignal;
	public final DspBlockComponent to;
	public final InputModel toSignal;

	public DspConnection(DspBlockComponent from, int idxFrom, DspBlockComponent to, int idxTo) {
		this.from = from;
		this.to = to;
		this.fromSignal = from.getModel().outputs.get(idxFrom);
		this.toSignal = to.getModel().inputs.get(idxTo);
	}

	public void render(Graphics g) {
		int idx0 = from.getModel().getOutputs().indexOf(fromSignal);
		int idx1 = to.getModel().getInputs().indexOf(toSignal);
		int y0 = from.getY() + 23 + from.outputList.getCellBounds(idx0, idx0).y;
		int x0 = from.getX() + from.getWidth();
		int x01 = x0 + 8;
		int y1 = to.getY() + 23 + to.inputList.getCellBounds(idx1, idx1).y;
		int x1 = to.getX();
		int x11 = x1 - 8;

		Graphics2D g2d = (Graphics2D) g;

		if (from.isSelected() || to.isSelected()) {
			g.setColor(Color.BLUE);
			g2d.setStroke(selected);
		} else {
			g.setColor(Color.BLACK);
			g2d.setStroke(normal);
		}

		g.drawLine(x0, y0, x01, y0); // from output
		g.drawLine(x1, y1, x11, y1); // from input
		g.drawLine(x1, y1, x1 - 5, y1 - 3);
		g.drawLine(x1, y1, x1 - 5, y1 + 3);
		g.fillRect(x0, y0 - 2, 4, 5);

		if (x01 < x11) {
			CubicCurve2D curve = new CubicCurve2D.Double();
			curve.setCurve(x01, y0, x01 + (x11 - x01) / 3, y0, x11 - (x11 - x01) / 3, y1, x11, y1);
			g2d.draw(curve);
		} else {
			int yHalf = (from.getY() + from.getHeight() + to.getY()) / 2;
			CubicCurve2D curve = new CubicCurve2D.Double();
			curve.setCurve(x01, y0, x01, yHalf, x11, yHalf, x11, y1);
			g2d.draw(curve);
		}

		g2d.setStroke(normal);
	}
}
