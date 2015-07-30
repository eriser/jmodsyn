package org.modsyn.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class EditorTheme {

	public static final Color COLOR_META_BLOCK_BG = new Color(0x006000);
	public static final Color COLOR_META_BLOCK_FG = Color.WHITE;

	public static final Color COLOR_SELECTED_BG = Color.BLUE;
	public static final Color COLOR_UNSELECTED_BG = Color.BLACK;

	public static final Color COLOR_MAIN_PATCH_BG = new Color(0xc0a080);
	public static final Color COLOR_SUB_PATCH_BG = new Color(0xc0e0c0);

	public static final Color COLOR_BASIC_BLOCK_BG = new Color(0xc0c0e0);
	public static final Color COLOR_DYNAMICS_BLOCK_BG = new Color(0x80b0ff);
	public static final Color COLOR_SHAPE_BLOCK_BG = new Color(0xffc080);
	public static final Color COLOR_FILTER_BLOCK_BG = new Color(0xe08040);
	public static final Color COLOR_OSC_BLOCK_BG = new Color(0xc060ff);
	public static final Color COLOR_EXT_BLOCK_BG = new Color(0xc00000);

	private static final Map<String, Color> DSP_CATEGORY_COLORS = new HashMap<>();
	static {
		DSP_CATEGORY_COLORS.put("Basics", COLOR_BASIC_BLOCK_BG);
		DSP_CATEGORY_COLORS.put("Dynamics", COLOR_DYNAMICS_BLOCK_BG);
		DSP_CATEGORY_COLORS.put("Shape", COLOR_SHAPE_BLOCK_BG);
		DSP_CATEGORY_COLORS.put("Filters", COLOR_FILTER_BLOCK_BG);
		DSP_CATEGORY_COLORS.put("Oscillators", COLOR_OSC_BLOCK_BG);
		DSP_CATEGORY_COLORS.put("EXT", COLOR_EXT_BLOCK_BG);
	}

	public static final Color COLOR_LINE = Color.BLACK;
	public static final Color COLOR_CTRL_LINE = Color.RED.darker();
	public static final Color LIST_SELECTION_COLOR = new Color(0xd0d0ff);

	public static final ImageIcon ICON_MIDI = new ImageIcon(EditorTheme.class.getResource("/midi32.png"));
	public static final ImageIcon ICON_ADR = new ImageIcon(EditorTheme.class.getResource("/adr32.png"));
	public static final ImageIcon ICON_ADSR = new ImageIcon(EditorTheme.class.getResource("/adsr32.png"));
	public static final ImageIcon ICON_TANH = new ImageIcon(EditorTheme.class.getResource("/tanh32.png"));
	public static final ImageIcon ICON_BPF = getScaledImage(new ImageIcon(EditorTheme.class.getResource("/filter_bandpass.png")), 32, 32);
	public static final ImageIcon ICON_LPF = getScaledImage(new ImageIcon(EditorTheme.class.getResource("/filter_lowpass.png")), 32, 32);
	public static final ImageIcon ICON_HPF = getScaledImage(new ImageIcon(EditorTheme.class.getResource("/filter_hipass.png")), 32, 32);
	public static final ImageIcon ICON_ENVELOPE_FOLLOWER = getScaledImage(new ImageIcon(EditorTheme.class.getResource("/enevelop-tracker.png")), 32, 32);
	public static final ImageIcon ICON_COMPRESSOR = getScaledImage(new ImageIcon(EditorTheme.class.getResource("/compressor.png")), 32, 32);

	public static final Color getColor(DspPalette pal) {
		Color c = DSP_CATEGORY_COLORS.get(pal.category);
		if (c == null) {
			return COLOR_BASIC_BLOCK_BG;
		} else {
			return c;
		}
	}

	public static Color brighten(Color c, int v) {
		return new Color(v8(c.getRed() + v), v8(c.getGreen() + v), v8(c.getBlue() + v));
	}

	private static int v8(int v) {
		return Math.max(0, Math.min(v, 255));
	}

	private static ImageIcon getScaledImage(ImageIcon srcImg, int w, int h) {
		return new ImageIcon(getScaledInstance(srcImg.getImage(), w, h));
	}

	public static Image getScaledInstance(Image img, int targetWidth, int targetHeight) {
		Image ret = img;
		int w = img.getWidth(null);
		int h = img.getHeight(null);

		do {
			if (w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}

	public final static Font FONT_OPERATOR = new Font("Courier", Font.BOLD, 11);
	public final static Font FONT_COLORLABEL = new Font("Courier", Font.BOLD, 14);
	public final static Font FONT_BLOCK_TITLE = new Font("Arial", Font.BOLD, 12);
	public final static Font FONT_METABLOCK_TITLE = FONT_BLOCK_TITLE.deriveFont(Font.ITALIC);
	public final static Font FONT_BLOCK_CONNECTION_LIST = new Font("Arial", Font.BOLD, 9);

	public final static Font FONT_KNOB_TITLE = new Font("Arial", Font.PLAIN, 10);
	public final static Font FONT_PALETTE_GROUP_TITLE = new Font("Arial", Font.BOLD, 14);
}
