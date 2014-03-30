package org.modsyn.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.modsyn.gui.JKnob;

public class DspBlockEditPanel extends JPanel implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 550295774262795448L;
	private final DspPatchModel patchModel;

	public DspBlockEditPanel(DspPatchComponent dpc) {
		this.patchModel = dpc.getModel();
		patchModel.addListener(this);
		setBackground(Color.BLACK);
		setOpaque(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == DspPatchModel.EVENT_SELECTION_CHANGED) {
			List<DspBlockComponent> selection = (List<DspBlockComponent>) evt.getNewValue();
			removeAll();

			Collections.sort(selection, new Comparator<DspBlockComponent>() {

				@Override
				public int compare(DspBlockComponent o1, DspBlockComponent o2) {
					int dx = o1.getX() - o2.getX();
					if (dx != 0) {
						return dx;
					} else {
						return o1.getY() - o2.getY();
					}
				}
			});

			for (DspBlockComponent c : selection) {
				DspBlockModel<?> bm = c.getModel();

				int count = 0;
				for (final InputModel input : bm.getInputs()) {
					if (!input.isConnected()) {
						count++;
					}
				}

				if (count > 0) {
					JPanel p = new JPanel(new BorderLayout());
					p.setBackground(Color.BLUE);
					p.setOpaque(true);
					JLabel title = new JLabel(bm.getName());
					title.setForeground(Color.WHITE);
					p.add(title, BorderLayout.NORTH);

					JPanel pc = new JPanel();
					pc.setBackground(Color.GRAY);
					pc.setOpaque(true);

					for (final InputModel input : bm.getInputs()) {
						if (input.isConnected()) {
							continue;
						}
						final JKnob knob = new JKnob();
						knob.setName(input.getName());
						knob.setValue(input.getValue());
						knob.setMin(input.getMin());
						knob.setMax(input.getMax());
						knob.setDecimals(input.getDecimals());

						knob.addPropertyChangeListener(new PropertyChangeListener() {
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								input.setValue(knob.getValue());
								input.setMin(knob.getMin());
								input.setMax(knob.getMax());
								input.setDecimals(knob.getDecimals());
							}
						});

						pc.add(knob);
						p.add(pc, BorderLayout.CENTER);
					}

					add(p);
					add(new JLabel("  "));
				}
			}

			invalidate();
			validate();
			repaint();
			getParent().validate();

		}
	}
}
