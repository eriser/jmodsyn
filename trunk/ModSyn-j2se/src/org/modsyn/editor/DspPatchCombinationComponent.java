package org.modsyn.editor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.modsyn.Context;

public class DspPatchCombinationComponent extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 3449327741564158580L;

	final JTabbedPane tabs = new JTabbedPane();

	private final DspPatchCombinationModel model;

	private final Context context;

	public DspPatchCombinationComponent(Context context, DspPatchCombinationModel model) {
		super(new BorderLayout());
		this.model = model;
		this.context = context;

		model.addListener(this);

		addTab(context, model.getMainModel());

		add(tabs, BorderLayout.CENTER);
	}

	private void addTab(Context context, DspPatchModel model) {
		final DspPatchComponent cmpPatch = new DspPatchComponent(context, model);
		DspPaletteComponent cmpPalette = new DspPaletteComponent(context, model);

		JScrollPane scrollC = new JScrollPane(cmpPatch, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollC.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollC.getVerticalScrollBar().setUnitIncrement(16);
		scrollC.getHorizontalScrollBar().setUnitIncrement(16);

		JPanel pCenter = new JPanel(new BorderLayout());
		pCenter.add(scrollC, BorderLayout.CENTER);
		pCenter.add(new DspBlockEditPanel(cmpPatch, this.model), BorderLayout.SOUTH);

		JPanel p = new JPanel(new BorderLayout());
		p.add(cmpPalette, BorderLayout.WEST);
		p.add(pCenter, BorderLayout.CENTER);

		tabs.addTab(model.name, p);
		if (tabs.getTabCount() > 1) {
			JLabel lbl = new JLabel(model.name);
			lbl.setForeground(DspBlockComponent.META_COLOR);
			lbl.setFont(lbl.getFont().deriveFont(Font.ITALIC));
			tabs.setTabComponentAt(tabs.getTabCount() - 1, lbl);
		}
	}

	public DspPatchCombinationModel getModel() {
		return model;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (DspPatchCombinationModel.EVENT_CLEAR.equals(evt.getPropertyName())) {
			for (int i = tabs.getTabCount() - 1; i >= 1; i--) {
				tabs.removeTabAt(i);
			}
		} else if (DspPatchCombinationModel.EVENT_ADD_SUBMODEL.equals(evt.getPropertyName())) {
			addTab(context, (DspPatchModel) evt.getNewValue());
		}
	}
}
