package org.modsyn.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
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
	private final boolean isMainModel;
	private List<DspPatchModel> linkedSubModels;
	private final DspPatchCombinationModel combinationModel;

	public DspBlockEditPanel(DspPatchComponent dpc, DspPatchCombinationModel model) {
		this.patchModel = dpc.getModel();
		this.isMainModel = patchModel.isMainModel;
		this.combinationModel = model;

		if (isMainModel) {
			this.linkedSubModels = new ArrayList<DspPatchModel>();
			this.linkedSubModels.add(patchModel);
		} else {
			this.linkedSubModels = model.getLinkedSubModels(patchModel.name);
		}

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
				DspBlockModel<?> block = c.getModel();

				int count = 0;
				for (final InputModel input : block.getInputs()) {
					if (!input.isConnected()) {
						count++;
					}
				}

				if (count > 0) {
					JPanel p = new JPanel(new BorderLayout());
					p.setBackground(Color.BLUE);
					p.setOpaque(true);
					JLabel title = new JLabel(block.getName());
					title.setForeground(Color.WHITE);
					p.add(title, BorderLayout.NORTH);

					JPanel pc = new JPanel();
					pc.setBackground(Color.GRAY);
					pc.setOpaque(true);

					List<DspBlockComponent> linkedBlocks = combinationModel.getLinkedBlockComponents(c);

					int inputIdx = -1;
					for (final InputModel input : block.getInputs()) {
						inputIdx++;
						if (input.isConnected()) {
							continue;
						}
						final JKnob knob = new JKnob();
						knob.setName(input.getName());
						knob.setDecimals(input.getDecimals());
						knob.setMin(input.getMin());
						knob.setMax(input.getMax());
						knob.setValue(input.getValue());

						if (isMainModel) {
							// controls just the selected component
							knob.addPropertyChangeListener(new PropertyChangeListener() {
								@Override
								public void propertyChange(PropertyChangeEvent evt) {
									if ("value".equals(evt.getPropertyName())) {
										input.setValue(knob.getValue());
									} else if ("min".equals(evt.getPropertyName())) {
										input.setMin(knob.getMin());
									} else if ("max".equals(evt.getPropertyName())) {
										input.setMax(knob.getMax());
									} else if ("decimals".equals(evt.getPropertyName())) {
										input.setDecimals(knob.getDecimals());
									}
								}
							});
						} else {
							// controls also all linked components
							final List<InputModel> linkedInputs = new ArrayList<>();
							for (DspBlockComponent linkedBlockComponent : linkedBlocks) {
								linkedInputs.add(linkedBlockComponent.getModel().getInputs().get(inputIdx));
							}

							knob.addPropertyChangeListener(new PropertyChangeListener() {
								@Override
								public void propertyChange(PropertyChangeEvent evt) {
									if ("value".equals(evt.getPropertyName())) {
										for (InputModel inputModel : linkedInputs) {
											inputModel.setValue(knob.getValue());
										}
									} else if ("min".equals(evt.getPropertyName())) {
										for (InputModel inputModel : linkedInputs) {
											inputModel.setMin(knob.getMin());
										}
									} else if ("max".equals(evt.getPropertyName())) {
										for (InputModel inputModel : linkedInputs) {
											inputModel.setMax(knob.getMax());
										}
									} else if ("decimals".equals(evt.getPropertyName())) {
										for (InputModel inputModel : linkedInputs) {
											inputModel.setDecimals(knob.getDecimals());
										}
									}
								}
							});
						}

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
