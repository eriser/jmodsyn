package org.modsyn.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class JKnob extends JComponent {

	private static final float SENSITIVITY = 10000f;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5992534977281485752L;

	private static final double a_min = 45 + 90;
	private static final double a_max = 315 + 90;

	private final MouseControl mouseControl = new MouseControl();

	private String format = "%.2f";
	private float knobSize = 0.5f;

	private float value = 0;
	private float min = 0;
	private float max = 1;
	private int decimals = 2;

	public JKnob() {
		super();
		setPreferredSize(new Dimension(64, 64));
		setMinimumSize(new Dimension(64, 64));
		addMouseMotionListener(mouseControl);
		addMouseListener(mouseControl);
		setName("---");

		setFont(new Font("Arial", Font.PLAIN, 10));
	}

	public void setKnobSize(float rel) {
		float prev = this.knobSize;
		this.knobSize = rel;
		super.firePropertyChange("knobSize", prev, rel);
	}

	public float getKnobSize() {
		return knobSize;
	}

	public float getValue() {
		return value;
	}

	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}

	public int getDecimals() {
		return decimals;
	}

	public void setValue(float value) {
		float prev = this.value;
		this.value = value;
		super.firePropertyChange("value", prev, value);
		repaint();
	}

	public void setMin(float min) {
		float prev = this.min;
		this.min = min;
		super.firePropertyChange("min", prev, min);
		repaint();
	}

	public void setMax(float max) {
		float prev = this.max;
		this.max = max;
		super.firePropertyChange("max", prev, max);
		repaint();
	}

	public void setDecimals(int decimals) {
		int prev = this.decimals;
		this.decimals = decimals;
		this.format = "%." + decimals + "f";
		super.firePropertyChange("decimals", prev, decimals);
		repaint();
	}

	private static final Color bg = new Color(0xe0e0ff);

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int w = getWidth();
		int h = getHeight();
		Paint gradient = new GradientPaint(w / 2, 0, bg, w / 2, h, Color.WHITE);
		g2d.setPaint(gradient);

		// g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		int wk = (int) (w * knobSize);
		int hk = (int) (h * knobSize);
		g.setColor(Color.GRAY);
		g.fillOval((w - wk) / 2, (h - hk) / 2, wk, hk);

		double angle = a_min + (a_max - a_min) * ((value - min) / (max - min));
		int x = (int) (((w / 2) + Math.cos(Math.toRadians(angle)) * (wk / 3)));
		int y = (int) (((h / 2) + Math.sin(Math.toRadians(angle)) * (hk / 3)));
		int sx = Math.max(2, w / 10);
		int sy = Math.max(2, h / 10);
		g.setColor(Color.WHITE);
		g.fillOval(x - sx / 2, y - sy / 2, sx, sy);

		String sv = String.format(format, value);
		Rectangle2D rect = g.getFontMetrics().getStringBounds(sv, g);

		int y_top = (int) (h - rect.getHeight() + 3);
		mouseControl.y_value_area = y_top;
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, y_top, w, h - y_top);

		g.setColor(Color.BLACK);
		g.drawString(sv, (int) ((w - rect.getWidth()) / 2f), h - 1);

		rect = g.getFontMetrics().getStringBounds(getName(), g);

		// g.setColor(bg);
		// g.fillRect(0, 0, w, (int) rect.getHeight());

		g.setColor(Color.BLACK);
		g.drawString(getName(), 0, (int) rect.getHeight() - 3);
	}

	public static void main(String[] args) {
		JKnob knob1 = new JKnob();

		JPanel p = new JPanel();
		p.add(knob1);

		JFrame f = new JFrame("Test");
		f.add(p);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	private class MouseControl extends MouseAdapter {
		public int y_value_area;
		private int startX, prevX;
		private int dirx;

		@Override
		public void mousePressed(MouseEvent e) {
			startX = e.getX();
			prevX = 0;
			dirx = 0;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getY() >= y_value_area) {
				openSettingsDialog();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			float range = max - min;

			int dx = e.getX() - prevX;
			int _dirx = (dx > startX ? 1 : dx < 0 ? -1 : 0);
			boolean directionChanged = _dirx != dirx;
			dirx = _dirx;
			if (directionChanged) {
				startX = e.getX();

			}

			dx = e.getX() - startX;
			float val = getValue() + ((dx / SENSITIVITY) * range);

			if (val > max) {
				val = max;
				startX = e.getX();
				dirx = 0;
			}
			if (val < min) {
				val = min;
				startX = e.getX();
				dirx = 0;
			}

			setValue(val);

			prevX = e.getX();
		}
	}

	private class SettingsDialog extends EscapeDialog {
		private static final long serialVersionUID = -7563534409767788441L;

		public SettingsDialog() {
			super();

			createGUI();

			addWindowFocusListener(new WindowAdapter() {
				@Override
				public void windowLostFocus(final WindowEvent e) {
					if (getOwner() != null) {
						dispose();
					}
				}
			});

			pack();
			setLocationRelativeTo(JKnob.this);
			setVisible(true);
		}

		private void createGUI() {
			setUndecorated(true);
			JPanel p = new JPanel(new GridBagLayout());
			p.setBackground(new Color(0xf0f0ff));
			p.setOpaque(true);
			p.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(4, 4, 4, 4)));

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.EAST;
			gbc.gridx = 0;

			gbc.gridy = 0;
			p.add(new JLabel("value: "), gbc);

			gbc.gridy = 1;
			p.add(new JLabel("min: "), gbc);

			gbc.gridy = 2;
			p.add(new JLabel("max: "), gbc);

			gbc.gridy = 3;
			p.add(new JLabel("decimals: "), gbc);

			gbc.gridx = 1;

			final JTextField txtValue = new JTextField(7);
			final JTextField txtMin = new JTextField(7);
			final JTextField txtMax = new JTextField(7);
			final JTextField txtDecimals = new JTextField(7);

			txtValue.setText(String.format(format, getValue()));
			txtMin.setText(String.format(format, getMin()));
			txtMax.setText(String.format(format, getMax()));
			txtDecimals.setText(Integer.toString(getDecimals()));

			txtValue.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					set();
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					set();
				}

				private void set() {
					try {
						float value = Float.parseFloat(txtValue.getText());
						if (value != getValue()) {
							setValue(value);
						}
					} catch (Exception e) {
					}
				}

				@Override
				public void changedUpdate(DocumentEvent arg0) {
				}
			});

			txtMin.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					set();
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					set();
				}

				private void set() {
					try {
						float value = Float.parseFloat(txtMin.getText());
						if (value != getMin() && value <= getMax()) {
							setMin(value);
						}
					} catch (Exception e) {
					}
				}

				@Override
				public void changedUpdate(DocumentEvent arg0) {
				}
			});

			txtMax.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					set();
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					set();
				}

				private void set() {
					try {
						float value = Float.parseFloat(txtMax.getText());
						if (value != getMax() && value >= getMin()) {
							setMax(value);
						}
					} catch (Exception e) {
					}
				}

				@Override
				public void changedUpdate(DocumentEvent arg0) {
				}
			});

			txtDecimals.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent arg0) {
					set();
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					set();
				}

				private void set() {
					try {
						int value = Integer.parseInt(txtDecimals.getText());
						if (value != getDecimals() && value >= 0 && value < 10) {
							setDecimals(value);
							// txtValue.setText(String.format(format,
							// getValue()));
							// txtMin.setText(String.format(format, getMin()));
							// txtMax.setText(String.format(format, getMax()));
						}
					} catch (Exception e) {
					}
				}

				@Override
				public void changedUpdate(DocumentEvent arg0) {
				}
			});

			gbc.gridy = 0;
			p.add(txtValue, gbc);
			gbc.gridy = 1;
			p.add(txtMin, gbc);
			gbc.gridy = 2;
			p.add(txtMax, gbc);
			gbc.gridy = 3;
			p.add(txtDecimals, gbc);

			add(p);
		}
	}

	public void openSettingsDialog() {
		new SettingsDialog();

	}
}
