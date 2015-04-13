package org.modsyn.editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.TransferHandler;

import org.modsyn.Context;

public class DspPatchComponent extends JPanel implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 756689332041402433L;

	private final DspPatchModel model;

	private final SelectionListener sl;

	class SelectionListener extends MouseAdapter {

		boolean sizing;
		boolean moving;
		int x, y;

		Rectangle selRectangle = null;

		List<DspBlockComponent> selComponents = new ArrayList<DspBlockComponent>();

		@Override
		public void mouseDragged(MouseEvent e) {
			if (sizing) {
				selRectangle.width = e.getX() - selRectangle.x;
				selRectangle.height = e.getY() - selRectangle.y;
				setSelectedComponents();
			} else if (moving) {
				int dx = (e.getX() - x);
				int dy = (e.getY() - y);

				for (DspBlockComponent c : selComponents) {
					if (selRectangle.intersects(c.getBounds())) {
						c.setLocation(c.getX() + dx, c.getY() + dy);
					}
				}

				selRectangle.x += dx;
				selRectangle.y += dy;
				x = e.getX();
				y = e.getY();
			} else {
				sizing = true;
				selRectangle = new Rectangle(e.getX(), e.getY(), 1, 1);
				setSelectedComponents();
			}
			repaint();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			clearSelectedComponents();
		}

		@Override
		public void mousePressed(MouseEvent e) {

			x = e.getX();
			y = e.getY();
			// ve.setVisible(false);
			if (!hasSelection()) {
				sizing = true;
				moving = false;
				selRectangle = new Rectangle(e.getX(), e.getY(), 1, 1);
			} else {
				if (selRectangle != null && selRectangle.contains(e.getX(), e.getY())) {
					// inside selection -> move
					moving = true;
					sizing = false;
				} else {
					// outside selection -> clear selection
					clearSelectedComponents();
					selRectangle = null;
					moving = sizing = false;
					repaint();
				}
			}
		}

		private boolean hasSelection() {
			return selComponents.size() > 0;
		}

		private void clearSelectedComponents() {
			selRectangle = null;
			selComponents.clear();
			for (DspBlockComponent c : model.getDspBlocks()) {
				c.setSelected(false);
			}
			model.selectionChanged(selComponents);
			repaint();
		}

		private void setSelectedComponents() {
			selComponents.clear();
			for (DspBlockComponent c : model.getDspBlocks()) {
				if (selRectangle.intersects(c.getBounds())) {
					selComponents.add(c);
					c.setSelected(true);
				} else {
					c.setSelected(false);
				}
			}
			model.selectionChanged(selComponents);
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			if (moving) {
				for (DspBlockComponent c : model.getDspBlocks()) {
					c.snapToGrid();
				}
			}
			moving = sizing = false;
		}

		final float dash[] = { 4.0f };
		final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash, 0.0f);

		public void render(Graphics g) {
			if (selRectangle != null) {
				g.setColor(Color.blue);
				Stroke s = ((Graphics2D) g).getStroke();
				((Graphics2D) g).setStroke(dashed);
				g.drawRect(selRectangle.x, selRectangle.y, selRectangle.width, selRectangle.height);
				((Graphics2D) g).setStroke(s);
			}
		}
	}

	public DspPatchComponent(Context c, DspPatchModel model) {
		super(null);
		this.model = model;

		if (model.isMainModel) {
			setBackground(EditorTheme.MAIN_PATCH_BG);
		} else {
			setBackground(EditorTheme.SUB_PATCH_BG);
		}
		model.addListener(this);
		TransferHandler th = new DndConnection.ListTransferHandler(c, model);
		setTransferHandler(th);

		sl = new SelectionListener();
		addMouseListener(sl);
		addMouseMotionListener(sl);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					for (DspBlockComponent c : sl.selComponents) {
						getModel().ctrlRemoveDspComponent(c);
					}
					sl.clearSelectedComponents();
				}
			}
		});
	}

	public DspPatchModel getModel() {
		return model;
	}

	public List<DspBlockComponent> getSelectedBlocks() {
		return sl.selComponents;
	}

	@Override
	public void paintComponent(Graphics g) {
		resize();
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (DspConnection c : model.dspConnections) {
			c.render(g);
		}
		sl.render(g);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (prop == DspPatchModel.EVENT_ADD_BLOCK) {
			DspBlockComponent c = (DspBlockComponent) evt.getNewValue();
			add(c);
			resize();
			c.revalidate();
		} else if (prop == DspPatchModel.EVENT_REMOVE_BLOCK) {
			remove((Component) evt.getNewValue());
			sl.selComponents.remove(evt.getNewValue());
			model.selectionChanged(sl.selComponents);
			resize();
		} else if (prop == DspPatchModel.EVENT_SELECT_INPUT) {
			// ve.edit((InputModel) evt.getNewValue());
		} else if (prop == DspPatchModel.EVENT_REMOVE_ALL) {
			sl.clearSelectedComponents();
			model.selectionChanged(sl.selComponents);
		} else if (prop == DspPatchModel.EVENT_ADD_CONNECTION || prop == DspPatchModel.EVENT_REMOVE_CONNECTION) {
			model.selectionChanged(sl.selComponents);
		} else if (prop == DspPatchModel.EVENT_SELECT_SINGLE_COMPONENT) {
			sl.clearSelectedComponents();
			sl.moving = sl.sizing = false;
			DspBlockComponent c = (DspBlockComponent) evt.getNewValue();
			c.setSelected(true);
			sl.selComponents.add(c);
			model.selectionChanged(sl.selComponents);
		}
		repaint();
	}

	private void resize() {
		int w = 0, h = 0;
		for (Component c : getComponents()) {
			if (c.getX() + c.getWidth() > w) {
				w = c.getX() + c.getWidth();
			}
			if (c.getY() + c.getHeight() > h) {
				h = c.getY() + c.getHeight();
			}
		}

		setPreferredSize(new Dimension(w, h));
		getParent().validate();
	}
}
