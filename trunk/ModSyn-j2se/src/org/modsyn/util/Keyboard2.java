/*
 * Created on Jun 17, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.modsyn.KeyboardListener;

/*
 Will only work correctly on UK keyboards :/
 */
public class Keyboard2 implements KeyListener {

	// @formatter:off
	private static final int[] keys = new int[] { KeyEvent.VK_ENTER, KeyEvent.VK_SPACE, KeyEvent.VK_F1, KeyEvent.VK_F2, KeyEvent.VK_F3, KeyEvent.VK_F4,
			KeyEvent.VK_F5, KeyEvent.VK_F6, KeyEvent.VK_F7, KeyEvent.VK_F8,

			KeyEvent.VK_Z, /* C0 = 10 */
			KeyEvent.VK_S, KeyEvent.VK_X, KeyEvent.VK_D, KeyEvent.VK_C, KeyEvent.VK_V, KeyEvent.VK_G, KeyEvent.VK_B, KeyEvent.VK_H, KeyEvent.VK_N,
			KeyEvent.VK_J, KeyEvent.VK_M,

			KeyEvent.VK_Q, /* C1 = 22 */
			KeyEvent.VK_2, KeyEvent.VK_W, KeyEvent.VK_3, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_5, KeyEvent.VK_T, KeyEvent.VK_6, KeyEvent.VK_Y,
			KeyEvent.VK_7, KeyEvent.VK_U,

			KeyEvent.VK_I, KeyEvent.VK_9, KeyEvent.VK_O, KeyEvent.VK_0, KeyEvent.VK_P, };
	// @formatter:on

	private final KeyboardListener synthesizer;
	private int octave;

	public Keyboard2(KeyboardListener synth) {
		synthesizer = synth;
		octave = 4;
	}

	private int get_key(int key_code) {
		int key, idx;
		key = -1;
		for (idx = 0; idx < keys.length; idx++) {
			if (keys[idx] == key_code) {
				key = idx;
			}
		}
		return key;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		int key = get_key(ke.getKeyCode());
		// System.out.println(key);
		if (key >= 10) { /* Note */
			synthesizer.keyOn(Tools.getFreq(octave * 12 + key - 10), 1);
		} else if (key >= 2) {
			/* Set Octave */
			octave = key - 2;
			// synthesizer.all_notes_off( false );
		} else if (key >= 0) {
			// synthesizer.all_notes_off( key == 0 );
		}
		// System.out.println(key);
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		int key = get_key(ke.getKeyCode());
		if (key >= 10) { /* Note */
			synthesizer.keyOff();
		}
	}

	@Override
	public void keyTyped(KeyEvent ke) {
	}
}
