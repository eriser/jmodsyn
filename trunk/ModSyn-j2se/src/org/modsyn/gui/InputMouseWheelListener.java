package org.modsyn.gui;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import org.modsyn.editor.DspBlockComponent;
import org.modsyn.editor.DspPatchCombinationModel;
import org.modsyn.editor.InputModel;

public class InputMouseWheelListener implements MouseWheelListener {

	private final InputModel im;

	public InputMouseWheelListener(InputModel im) {
		this.im = im;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		float v = im.getValue() - e.getWheelRotation();
		if (v > im.getMax()) {
			v = im.getMax();
		}
		if (v < im.getMin()) {
			v = im.getMin();
		}

		if (!im.getSoundBlockModel().isSubModel()) {
			im.setValue(v);
		} else {
			int inputIdx = im.getSoundBlockModel().getInputs().indexOf(im);
			DspPatchCombinationModel combinationModel = im.getSoundBlockModel().component.getPatchModel().parent;
			List<DspBlockComponent> linkedBlocks = combinationModel.getLinkedBlockComponents(im.getSoundBlockModel().component);
			for (DspBlockComponent linkedBlockComponent : linkedBlocks) {
				linkedBlockComponent.getModel().getInputs().get(inputIdx).setValue(v);
			}
		}
	}

}
