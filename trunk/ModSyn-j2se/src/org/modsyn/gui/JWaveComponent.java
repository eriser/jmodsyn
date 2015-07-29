package org.modsyn.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import org.modsyn.WaveChangeListener;
import org.modsyn.WaveChangeObservable;
import org.modsyn.editor.EditorTheme;
import org.modsyn.editor.InputModel;

@SuppressWarnings("serial")
public class JWaveComponent extends JComponent implements WaveChangeListener {

	private float[] wave;

	public JWaveComponent(float[] wave, Color bg) {
		this((WaveChangeObservable) null, null);
		if (bg != null) {
			setBackground(bg);
		}
		setWave(wave);
	}

	public JWaveComponent(final WaveChangeObservable wco, final InputModel im) {
		super();
		setOpaque(true);
		setForeground(new Color(0x000000));
		setBackground(EditorTheme.COLOR_OSC_BLOCK_BG);
		if (wco != null) {
			wco.setWaveChangeListener(this);
		}
		if (im != null) {
			addMouseWheelListener(new InputMouseWheelListener(im));
		}
	}

	public void setWave(float[] wave) {
		this.wave = wave;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int w = getWidth();
		int h = getHeight();

		g.setColor(getBackground());
		g.fillRect(0, 0, w, h);

		if (wave == null) {
			return;
		}

		int yHalf = h / 2;
		int yMax = w >= h ? yHalf : w / 2;
		float fw = w;

		g.setColor(getForeground());
		for (int i = 0; i < w; i++) {
			int idx = (int) ((i / fw) * (wave.length));
			int a = (int) (wave[idx] * yMax);

			if (a > 0) {
				g.fillRect(i, yHalf - a, 1, 1 + a);
			} else {
				g.fillRect(i, yHalf, 1, 1 - a);
			}
		}
	}

	@Override
	public void waveChanged(float[] wave) {
		setWave(wave);
	}
}
