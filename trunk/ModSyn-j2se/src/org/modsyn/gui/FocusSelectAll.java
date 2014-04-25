package org.modsyn.gui;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.JTextComponent;

/**
 * FocusListener for JTextComponents that selects all text when the component
 * gets focus.
 * 
 * @author ed83897
 */
public class FocusSelectAll implements FocusListener {

	@Override
	public void focusGained(final FocusEvent e) {
		final Component c = e.getComponent();
		if (c instanceof JTextComponent) {
			final JTextComponent tc = (JTextComponent) c;
			tc.setSelectionStart(0);
			tc.setSelectionEnd(tc.getDocument().getLength());
		}
	}

	@Override
	public void focusLost(final FocusEvent e) {
	}
}
