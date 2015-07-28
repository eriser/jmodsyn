package org.modsyn.gui;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.modsyn.FilterTypeChangeListener;
import org.modsyn.FilterTypeChangeObservable;
import org.modsyn.editor.EditorTheme;
import org.modsyn.editor.InputModel;

@SuppressWarnings("serial")
public class JFilterTypeComponent extends JColorLabel implements FilterTypeChangeListener {

	public JFilterTypeComponent(FilterTypeChangeObservable observable, final InputModel im) {
		super(EditorTheme.ICON_LPF, EditorTheme.COLOR_FILTER_BLOCK_BG);
		observable.setFilterTypeChangeListener(this);
		if (im != null) {
			addMouseWheelListener(new MouseWheelListener() {
				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					float v = im.getValue() - e.getWheelRotation();
					if (v > im.getMax()) {
						v = im.getMax();
					}
					if (v < im.getMin()) {
						v = im.getMin();
					}
					im.setValue(v);
				}
			});
		}
	}

	@Override
	public void filterTypeChanged(int type) {
		switch (type) {
		case 0:
			setIcon(EditorTheme.ICON_LPF);
			break;
		case 1:
			setIcon(EditorTheme.ICON_BPF);
			break;
		case 2:
			setIcon(EditorTheme.ICON_HPF);
			break;
		default:
			setIcon(EditorTheme.ICON_LPF);
			break;
		}
		repaint();
	}

}
