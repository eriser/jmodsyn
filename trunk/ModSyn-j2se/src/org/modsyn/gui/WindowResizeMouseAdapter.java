package org.modsyn.gui;

import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse Adapter to move and resize undecorated windows.
 */
public class WindowResizeMouseAdapter extends MouseAdapter {

	private static final int DRAG_MODE_MOVE = 0;
	private static final int DRAG_MODE_RESIZE_W = 1;
	private static final int DRAG_MODE_RESIZE_H = 2;
	private static final int DRAG_MODE_RESIZE_WH = 3;

	private static final int DRAG_AREA = 8;

	private final Cursor moveCursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
	private final Cursor resizeCursorW = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
	private final Cursor resizeCursorH = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
	private final Cursor resizeCursorWH = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
	private final Cursor normalCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	private int dragMode;
	private int px = 0, py = 0;
	private final Window w;

	public WindowResizeMouseAdapter(Window w) {
		this.w = w;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int mx = e.getXOnScreen() - px;
		int my = e.getYOnScreen() - py;
		px = e.getXOnScreen();
		py = e.getYOnScreen();

		switch (dragMode) {
		case DRAG_MODE_MOVE:
			w.setCursor(moveCursor);
			w.setLocation(w.getX() + mx, w.getY() + my);
			break;
		case DRAG_MODE_RESIZE_W:
			w.setBounds(w.getX(), w.getY(), w.getWidth() + mx, w.getHeight());
			w.validate();
			break;
		case DRAG_MODE_RESIZE_H:
			w.setBounds(w.getX(), w.getY(), w.getWidth(), w.getHeight() + my);
			w.validate();
			break;
		case DRAG_MODE_RESIZE_WH:
			w.setBounds(w.getX(), w.getY(), w.getWidth() + mx, w.getHeight() + my);
			w.validate();
			break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		px = e.getXOnScreen();
		py = e.getYOnScreen();

		boolean rightEdge = e.getX() > w.getWidth() - DRAG_AREA;
		boolean bottomEdge = e.getY() > w.getHeight() - DRAG_AREA;

		if (rightEdge || bottomEdge) {
			if (rightEdge && bottomEdge) {
				dragMode = DRAG_MODE_RESIZE_WH;
			} else if (rightEdge) {
				dragMode = DRAG_MODE_RESIZE_W;
			} else {
				dragMode = DRAG_MODE_RESIZE_H;
			}
		} else {
			dragMode = DRAG_MODE_MOVE;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setCursor(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setCursor(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		setCursor(e);
	}

	private void setCursor(MouseEvent e) {
		boolean rightEdge = e.getX() > w.getWidth() - DRAG_AREA;
		boolean bottomEdge = e.getY() > w.getHeight() - DRAG_AREA;

		if (rightEdge || bottomEdge) {
			if (rightEdge && bottomEdge) {
				w.setCursor(resizeCursorWH);
			} else if (rightEdge) {
				w.setCursor(resizeCursorW);
			} else {
				w.setCursor(resizeCursorH);
			}
		} else {
			w.setCursor(normalCursor);
		}
	}
}
