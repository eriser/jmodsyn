package org.modsyn.editor;

import java.awt.Color;
import java.awt.Font;

public class EditorTheme {

	public static final Color COLOR_META_BLOCK_BG = new Color(0x006000);
	public static final Color COLOR_META_BLOCK_FG = Color.WHITE;

	public static final Color COLOR_SELECTED_BG = Color.BLUE;
	public static final Color COLOR_UNSELECTED_BG = Color.BLACK;

	public static final Color COLOR_MAIN_PATCH_BG = new Color(0xc0a080);
	public static final Color COLOR_SUB_PATCH_BG = new Color(0xc0e0c0);

	public static Color brighten(Color c, int v) {
		return new Color(v8(c.getRed() + v), v8(c.getGreen() + v), v8(c.getBlue() + v));
	}

	private static int v8(int v) {
		return Math.max(0, Math.min(v, 255));
	}

	public final static Font FONT_BLOCK_TITLE = new Font("Arial", Font.BOLD, 12);
	public final static Font FONT_METABLOCK_TITLE = FONT_BLOCK_TITLE.deriveFont(Font.ITALIC);
	public final static Font FONT_BLOCK_CONNECTION_LIST = new Font("Arial", Font.BOLD, 9);

	public final static Font FONT_KNOB_TITLE = new Font("Arial", Font.PLAIN, 10);
	public final static Font FONT_PALETTE_GROUP_TITLE = new Font("Arial", Font.BOLD, 14);

}
