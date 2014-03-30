package org.modsyn.editor;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.TransferHandler;

/**
 * Drag-and-drop support for DspConnections
 * 
 * @author Erik Duijs
 */
public class DndConnection {

	public static final DataFlavor FLAVOR_OUTPUTMODEL = new DataFlavor(OutputModel.class, "OutputModel");
	public static final DataFlavor FLAVOR_INPUTMODEL = new DataFlavor(InputModel.class, "InputModel");

	public static class OutTransferable implements Transferable {
		OutputModel data;

		public OutTransferable(OutputModel object) {
			this.data = object;
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return data;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { FLAVOR_OUTPUTMODEL };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return (FLAVOR_OUTPUTMODEL.equals(flavor));
		}
	}

	public static class ListTransferHandler extends TransferHandler {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4208048654650933678L;
		private final DspPatchModel model;

		public ListTransferHandler(DspPatchModel model) {
			this.model = model;
		}

		@Override
		public boolean canImport(TransferHandler.TransferSupport info) {
			return info.isDataFlavorSupported(FLAVOR_OUTPUTMODEL);
		}

		/**
		 * Bundle up the selected items in a single list for export. Each line
		 * is separated by a newline.
		 */
		@Override
		protected Transferable createTransferable(JComponent c) {
			JList<?> list = (JList<?>) c;
			Object o = list.getSelectedValue();

			if (o instanceof OutputModel) {
				return new OutTransferable((OutputModel) o);
			}

			return null;
		}

		/**
		 * We support both copy and move actions.
		 */
		@Override
		public int getSourceActions(JComponent c) {
			return TransferHandler.COPY_OR_MOVE;
		}

		/**
		 * Perform the actual import. This demo only supports drag and drop.
		 */
		@Override
		public boolean importData(TransferHandler.TransferSupport info) {
			if (!info.isDrop()) {
				System.err.println("NO DROP");
				return false;
			}

			if (!(info.getComponent() instanceof JList<?>)) {
				// disconnect
				try {
					model.removeDspConnection((OutputModel) info.getTransferable().getTransferData(FLAVOR_OUTPUTMODEL));
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}

			JList<?> list = (JList<?>) info.getComponent();
			ListModel<?> listModel = list.getModel();
			JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
			int index = dl.getIndex();

			Transferable t = info.getTransferable();
			OutputModel outputModel;
			try {
				outputModel = (OutputModel) t.getTransferData(FLAVOR_OUTPUTMODEL);

				InputModel inputModel = (InputModel) listModel.getElementAt(index);

				if (outputModel.getSoundBlockModel() == inputModel.getSoundBlockModel()) {
					System.err.println("CAN'T CONNECT TO SELF");
					return false;
				}
				System.out.println("CREATE CONNECTION " + outputModel + " to " + inputModel);

				model.addDspConnection(new DspConnection(outputModel.getSoundBlockModel().component, outputModel.getSoundBlockModel().outputs
						.indexOf(outputModel), inputModel.getSoundBlockModel().component, index));
			} catch (Exception e) {
				System.err.println("CAN'T DO: " + e.getMessage());
				e.printStackTrace();
				return false;
			}

			return true;
		}
	}
}
