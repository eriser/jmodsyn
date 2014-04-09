package org.modsyn.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

public class DspPaletteComponent extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6174278157459809170L;

	public DspPaletteComponent(final Context c, final DspPatchModel patchModel) {
		super(new GridBagLayout());

		Color bg = (Color) UIManager.get("TabbedPane.tabAreaBackground");
		// Color bg = (Color) UIManager.get("TabbedPane.selected");

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

		for (String key : lists.keySet()) {
			List<DspPalette> l = lists.get(key);
			DspPalette[] pal = new DspPalette[l.size()];
			pal = l.toArray(pal);

			JLabel lbl = new JLabel(key);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			lbl.setForeground(bg);
			lbl.setBackground(Color.black);
			lbl.setFont(lbl.getFont().deriveFont(lbl.getFont().getSize2D() + 2));
			lbl.setOpaque(true);

			TransferHandler th = new DndConnection.ListTransferHandler(c, patchModel);

			final JList<DspPalette> list = new JList<DspPalette>(pal);
			list.setDragEnabled(true);
			list.setTransferHandler(th);

			gbc.gridy++;
			add(lbl, gbc);
			gbc.gridy++;
			add(list, gbc);

			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2 && list.getSelectedIndex() >= 0) {
						patchModel.addDspComponent(list.getSelectedValue().create(c, patchModel, -1));
					}

				}
			});
		}

		gbc.gridy++;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.weighty = 1;
		add(new JLabel(), gbc);

	}
}
