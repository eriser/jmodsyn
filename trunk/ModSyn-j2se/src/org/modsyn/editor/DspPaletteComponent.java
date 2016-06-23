package org.modsyn.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.UIManager;

import org.modsyn.Context;
import org.modsyn.gui.DspPaletteListRenderer;

/**
 * Component that displays the categorized palette of DSP components.
 * 
 * @author Erik Duijs
 */
public class DspPaletteComponent extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6174278157459809170L;

	public DspPaletteComponent(final Context c, final DspPatchModel patchModel) {
		super(new GridBagLayout());

		Color bg = (Color) UIManager.get("TabbedPane.tabAreaBackground");

		setOpaque(true);
		setMinimumSize(new Dimension(120, 100));
		setPreferredSize(new Dimension(120, 100));
		setBackground(Color.GRAY);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 1.0;
		gbc.weighty = 0;
		gbc.gridx = 0;
		gbc.gridy = 0;

		Map<String, List<DspPalette>> lists = new LinkedHashMap<String, List<DspPalette>>();
		for (DspPalette pal : DspPalette.values()) {
			List<DspPalette> list = lists.get(pal.category);
			if (list == null) {
				list = new ArrayList<DspPalette>();
				lists.put(pal.category, list);
			}
			list.add(pal);
		}

		final ArrayList<JList<DspPalette>> jLists = new ArrayList<>();

		for (String key : lists.keySet()) {

			List<DspPalette> l = lists.get(key);
			DspPalette[] pal = new DspPalette[l.size()];
			pal = l.toArray(pal);
			final Color categoryColor = EditorTheme.getColor(pal[0]);

			final JList<DspPalette> list = new JList<DspPalette>(pal);

			@SuppressWarnings("serial")
			final JLabel lbl = new JLabel(key) {
				@Override
				public void paintComponent(Graphics g) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setPaint(new GradientPaint(0, 0, Color.BLACK, 0, getHeight(), new Color(0xff404040)));
					g2d.fillRect(0, 0, getWidth(), getHeight());

					g2d.setPaint(new GradientPaint(getWidth() - 16, 0, new Color(0, true), getWidth() - 16 / 2, 0, categoryColor));
					g2d.fillRect(getWidth() - 16, getHeight() - 4, 16, 4);

					g2d.setColor(Color.GRAY);
					if (!list.isVisible()) {
						g2d.fillPolygon(new Polygon(new int[] { 1, getHeight() / 2, 1 }, new int[] { 1, getHeight() / 2, getHeight() - 2 }, 3));
					} else {
						g2d.fillPolygon(new Polygon(new int[] { 1, getHeight() - 2, getHeight() / 2 }, new int[] { 2, 2, 1 + getHeight() / 2 }, 3));
					}

					super.paintComponent(g);
				}
			};

			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setForeground(bg);
			lbl.setFont(EditorTheme.FONT_PALETTE_GROUP_TITLE);
			lbl.setOpaque(false);

			TransferHandler th = new DndConnection.ListTransferHandler(c, patchModel);

			list.setDragEnabled(true);
			list.setTransferHandler(th);
			list.setSelectionBackground(EditorTheme.LIST_SELECTION_COLOR);
			list.setCellRenderer(new DspPaletteListRenderer(categoryColor));

			lbl.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					list.setVisible(!list.isVisible());
					lbl.repaint();
				}
			});

			gbc.gridy++;
			add(lbl, gbc);
			gbc.gridy++;
			add(list, gbc);

			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2 && list.getSelectedIndex() >= 0) {
						if (patchModel.isMainModel) {
							patchModel.addDspComponent(list.getSelectedValue().create(c, patchModel, -1));
						} else {
							for (DspPatchModel m : patchModel.parent.getLinkedSubModels(patchModel.name)) {
								m.addDspComponent(list.getSelectedValue().create(c, m, -1));
							}
						}
					}
				}
			});
			list.setVisible(false);
			jLists.add(list);
		}

		gbc.gridy++;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.weighty = 1;
		add(new JLabel(), gbc);

	}
}
