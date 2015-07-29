package org.modsyn.gui;

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
			addMouseWheelListener(new InputMouseWheelListener(im));
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
