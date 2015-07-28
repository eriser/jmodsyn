package org.modsyn.editor;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.TransferHandler;

import org.modsyn.Context;

/**
 * Drag-and-drop support
 * 
 * @author Erik Duijs
 */
public class DndConnection {

	public static final DataFlavor FLAVOR_OUTPUTMODEL = new DataFlavor(OutputModel.class, "OutputModel");
	public static final DataFlavor FLAVOR_INPUTMODEL = new DataFlavor(InputModel.class, "InputModel");
	public static final DataFlavor FLAVOR_DSPPALETTE = new DataFlavor(DspPalette.class, "DspPalette");

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

	public static class DspPaletteTransferable implements Transferable {
		DspPalette data;

		public DspPaletteTransferable(DspPalette object) {
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
			return new DataFlavor[] { FLAVOR_DSPPALETTE };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return (FLAVOR_DSPPALETTE.equals(flavor));
		}
	}

	public static class ListTransferHandler extends TransferHandler {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4208048654650933678L;
		private final DspPatchModel model;
		private final Context context;

		public ListTransferHandler(Context context, DspPatchModel model) {
			this.context = context;
			this.model = model;
		}

		@Override
		public boolean canImport(TransferHandler.TransferSupport info) {
			return info.isDataFlavorSupported(FLAVOR_OUTPUTMODEL) || info.isDataFlavorSupported(FLAVOR_DSPPALETTE);
		}

		/**
		 * Bundle up the selected items in a single list for export. Each line is separated by a newline.
		 */
		@Override
		protected Transferable createTransferable(JComponent c) {
			JList<?> list = (JList<?>) c;
			Object o = list.getSelectedValue();

			if (o instanceof OutputModel) {
				return new OutTransferable((OutputModel) o);
			}

			if (o instanceof DspPalette) {
				return new DspPaletteTransferable((DspPalette) o);
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

				if (info.getTransferable().isDataFlavorSupported(FLAVOR_OUTPUTMODEL)) {
					// disconnect
					try {
						OutputModel om = (OutputModel) info.getTransferable().getTransferData(FLAVOR_OUTPUTMODEL);
						if (om.isMetaRename()) {
							return false;
						}
						model.ctrlRemoveDspConnection(om);
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}

				if (info.getTransferable().isDataFlavorSupported(FLAVOR_DSPPALETTE)) {

					try {
						DspPalette pal = (DspPalette) info.getTransferable().getTransferData(FLAVOR_DSPPALETTE);
						if (model.isMainModel) {
							DspBlockComponent dbc = pal.create(context, model, -1);
							dbc.setLocation(info.getDropLocation().getDropPoint().x, info.getDropLocation().getDropPoint().y);
							dbc.snapToGrid();

							model.addDspComponent(dbc);
						} else {
							int channels = -1;
							for (DspPatchModel m : model.parent.getLinkedSubModels(model.name)) {
								DspBlockComponent dbc = pal.create(context, m, channels);
								channels = dbc.getModel().channels;
								dbc.setLocation(info.getDropLocation().getDropPoint().x, info.getDropLocation().getDropPoint().y);
								dbc.snapToGrid();

								m.addDspComponent(dbc);
							}
						}

						return true;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
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

				if (inputModel.isMetaRename() || outputModel.isMetaRename()) {
					// Don't connect to meta-renamed in/outputs, because they
					// should be connected in the parent patch.
					return false;
				}

				DspBlockModel<?> outputBlockModel = outputModel.getSoundBlockModel();
				DspBlockModel<?> inputBlockModel = inputModel.getSoundBlockModel();

				if (outputBlockModel == inputBlockModel) {
					// Don't connect to itself
					return false;
				}

				if (model.isMainModel) {
					model.addDspConnection(new DspConnection(outputBlockModel.component, outputBlockModel.outputs.indexOf(outputModel),
							inputBlockModel.component, index), true);
				} else {

					List<DspBlockComponent> outComponents = model.parent.getLinkedBlockComponents(outputBlockModel.component);
					List<DspBlockComponent> inComponents = model.parent.getLinkedBlockComponents(inputBlockModel.component);
					List<DspPatchModel> subModels = model.parent.getLinkedSubModels(model.name);

					for (int i = 0; i < subModels.size(); i++) {
						subModels.get(i).addDspConnection(
								new DspConnection(outComponents.get(i), outputBlockModel.outputs.indexOf(outputModel), inComponents.get(i), index), true);
					}
				}
			} catch (Exception ignore) {
				// dropping where we can't drop
				return false;
			}

			return true;
		}
	}
}
