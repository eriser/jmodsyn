package org.modsyn.gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

/**
 * A JDialog that can be closed with the ESC key, and will be closed when it loses focus.
 * 
 * @author Erik Duijs
 */
public class EscapeDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8251847249828598076L;

	public EscapeDialog() {
		super();
	}

	public EscapeDialog(Frame owner, boolean modal) {
		super(owner, modal);
	}

	@Override
	protected JRootPane createRootPane() {
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		KeyStroke strokeE = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		JRootPane rootPane = new JRootPane();
		rootPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		rootPane.registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		}, strokeE, JComponent.WHEN_IN_FOCUSED_WINDOW);
		addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowLostFocus(final WindowEvent e) {
				if (getOwner() != null) {
					dispose();
				}
			}
		});
		return rootPane;
	}
}
