package org.modsyn.editor;

import static java.lang.Math.max;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.modsyn.Context;
import org.modsyn.editor.blocks.MetaModel;

public class DspBlockComponent extends JPanel implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 778563636039495750L;
	private static final Color colClose = new Color(0x804040);
	private static final Color colBright = new Color(0xffc0c0);
	private DspBlockModel<?> model;
	final JList<InputModel> inputList;
	final JList<OutputModel> outputList;
	private final JLabel close;
	private ActionListener closeListener;
	final JButton button;

	private boolean selected;
	private JLabel name;
	private DspPatchModel patchModel;
	private DspPatchModel metaPatchModel;
	private boolean hasMetaConnection;
	private int connHeight;

	public DspBlockComponent(Context c, DspBlockModel<?> model, DspPatchModel patchModel) {
		this(c, model, patchModel, 0, 0, 80, -1);
	}

	public DspBlockComponent(Context c, DspBlockModel<?> model, final DspPatchModel patchModel, int x, int y, int w, int h) {
		super(new BorderLayout());
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		this.patchModel = patchModel;
		this.model = model;
		model.component = this;

		this.name = new JLabel(model.getName());

		close = new JLabel(" x ");
		close.setBackground(colClose);
		close.setForeground(Color.white);
		close.setOpaque(true);
		close.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				close.setBackground(colClose);
				close.setForeground(Color.white);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				close.setBackground(colBright);
				close.setForeground(Color.red);

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				close.setBackground(colClose);
				close.setForeground(Color.white);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				close.setBackground(Color.red);
				close.setForeground(Color.white);
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (closeListener != null) {
					closeListener.actionPerformed(new ActionEvent(close, ActionEvent.ACTION_FIRST, "close"));
				}
			}
		});
		close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		button = new JButton();

		Mover m = new Mover();
		name.addMouseListener(m);
		name.addMouseMotionListener(m);

		name.setForeground(Color.WHITE);
		name.setBackground(Color.BLACK);
		name.setOpaque(true);
		boolean isMeta = getModel() instanceof MetaModel;
		if (isMeta) {
			name.setFont(EditorTheme.FONT_METABLOCK_TITLE);
		} else {
			name.setFont(EditorTheme.FONT_BLOCK_TITLE);
		}
		name.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		JPanel top = new JPanel(new BorderLayout());
		top.add(name, BorderLayout.CENTER);
		top.add(close, BorderLayout.EAST);

		add(top, BorderLayout.NORTH);
		add(createCenterComponent(), BorderLayout.CENTER);

		TransferHandler th = new DndConnection.ListTransferHandler(c, patchModel);
		if (model.getInputs().size() > 0) {
			ListModel<InputModel> lm = new AbstractListModel<InputModel>() {
				private static final long serialVersionUID = -8027752702985618699L;

				@Override
				public InputModel getElementAt(int index) {
					return DspBlockComponent.this.model.getInputs().get(index);
				}

				@Override
				public int getSize() {
					return DspBlockComponent.this.model.getInputs().size();
				}
			};
			inputList = new JList<InputModel>(lm);
			inputList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			inputList.setDropMode(DropMode.ON);
			inputList.setTransferHandler(th);
			inputList.setDragEnabled(true);
			inputList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			inputList.setFont(EditorTheme.FONT_BLOCK_CONNECTION_LIST);
			inputList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					patchModel.selectInputValue(inputList.getSelectedValue());
					inputList.removeSelectionInterval(inputList.getSelectedIndex(), inputList.getSelectedIndex());
				}
			});

			add(inputList, BorderLayout.WEST);
		} else {
			inputList = null;
		}

		if (model.getOutputs().size() > 0) {
			ListModel<OutputModel> lm = new AbstractListModel<OutputModel>() {
				private static final long serialVersionUID = -8027752702985618699L;

				@Override
				public OutputModel getElementAt(int index) {
					return DspBlockComponent.this.model.getOutputs().get(index);
				}

				@Override
				public int getSize() {
					return DspBlockComponent.this.model.getOutputs().size();
				}
			};
			outputList = new JList<OutputModel>(lm);
			outputList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			outputList.setTransferHandler(th);
			outputList.setDragEnabled(true);
			outputList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			outputList.setFont(EditorTheme.FONT_BLOCK_CONNECTION_LIST);

			add(outputList, BorderLayout.EAST);
		} else {
			outputList = null;
		}
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		int ch;
		if (model.getInputs().size() > 0) {
			ch = inputList.getCellBounds(0, 0).height;
		} else {
			ch = outputList.getCellBounds(0, 0).height;
		}
		int height = 18 + (max(model.getInputs().size(), model.getOutputs().size()) * ch);
		this.connHeight = ch;

		setBounds(x, y, w, height);

		for (InputModel im : model.inputs) {
			hasMetaConnection |= im.isMetaRename();
		}
		for (OutputModel om : model.outputs) {
			hasMetaConnection |= om.isMetaRename();
		}

		if (hasMetaConnection) {
			close.setVisible(false);
		}

		setSelected(false);
	}

	public boolean isSelected() {
		return selected;
	}

	public DspPatchModel getMetaPatchModel() {
		return metaPatchModel;
	}

	public void setMetaPatchModel(DspPatchModel metaPatchModel) {
		this.metaPatchModel = metaPatchModel;
	}

	public void setSelected(boolean b) {
		this.selected = b;
		boolean isMeta = getModel() instanceof MetaModel;
		setBorder(BorderFactory
				.createLineBorder(b ? EditorTheme.COLOR_SELECTED_BG : isMeta ? EditorTheme.COLOR_META_BLOCK_BG : EditorTheme.COLOR_UNSELECTED_BG));
		name.setBackground(b ? EditorTheme.COLOR_SELECTED_BG : isMeta ? EditorTheme.COLOR_META_BLOCK_BG : EditorTheme.COLOR_UNSELECTED_BG);
	}

	public Component createCenterComponent() {
		return button;
	}

	public void addCloseButtonListener(ActionListener al) {
		closeListener = al;
	}

	public DspBlockModel<?> getModel() {
		return model;
	}

	class Mover extends MouseAdapter {
		boolean dragging;
		int x, y;

		@Override
		public void mouseDragged(MouseEvent e) {
			if (dragging) {
				Rectangle r = getBounds();
				setBounds((int) (r.getX() + (e.getX() - x)), (int) (r.getY() + (e.getY() - y)), (int) r.getWidth(), (int) r.getHeight());
				getParent().repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			dragging = true;
			x = e.getX();
			y = e.getY();
			patchModel.selectSingleComponent(DspBlockComponent.this);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			dragging = false;

			snapToGrid();
		}
	}

	private final Map<InputModel, JComponent> metaInComponents = new HashMap<InputModel, JComponent>();
	private final Map<OutputModel, JComponent> metaOutComponents = new HashMap<OutputModel, JComponent>();

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D pg = (Graphics2D) getParent().getGraphics();
		pg.setColor(Color.WHITE);

		if (hasMetaConnection) {
			int i = 0;
			for (InputModel im : model.inputs) {
				if (im.isMetaRename()) {
					JComponent lbl;
					if (!metaInComponents.containsKey(im)) {
						lbl = new MetaConnectionLabel(" " + im.getMetaRename());
						lbl.setBackground(EditorTheme.COLOR_META_BLOCK_BG);
						lbl.setForeground(EditorTheme.COLOR_META_BLOCK_FG);
						lbl.setOpaque(true);
						getParent().add(lbl);
						metaInComponents.put(im, lbl);
					} else {
						lbl = metaInComponents.get(im);
					}
					lbl.setBounds(getX() - 100, getY() + 16 + (i * connHeight), 100, connHeight - 1);
				}
				i++;
			}

			i = 0;
			for (OutputModel im : model.outputs) {
				if (im.isMetaRename()) {
					JComponent lbl;
					if (!metaOutComponents.containsKey(im)) {
						lbl = new MetaConnectionLabel(im.getMetaRename() + "   ", SwingConstants.RIGHT);
						lbl.setBackground(EditorTheme.COLOR_META_BLOCK_BG);
						lbl.setForeground(EditorTheme.COLOR_META_BLOCK_FG);
						lbl.setOpaque(true);
						getParent().add(lbl);
						metaOutComponents.put(im, lbl);
					} else {
						lbl = metaOutComponents.get(im);
					}
					lbl.setBounds(getX() + getWidth(), getY() + 16 + (i * connHeight), 100, connHeight - 1);
				}
				i++;
			}
		}
	}

	public void snapToGrid() {
		int x1 = getX() + 12;
		int y1 = getY() + 12;
		int x = x1 - (x1 % 25);
		int y = y1 - (y1 % 25);

		if (!patchModel.isMainModel) {
			List<DspBlockComponent> linked = patchModel.parent.getLinkedBlockComponents(this);
			for (DspBlockComponent dbc : linked) {
				dbc.setLocation(x, y);
			}
		} else {
			setLocation(x, y);
		}

		if (getParent() != null) {
			getParent().repaint();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		repaint();
	}

	public void hideClose() {
		close.setVisible(false);
		for (InputModel im : model.inputs) {
			hasMetaConnection |= im.isMetaRename();
		}
		for (OutputModel om : model.outputs) {
			hasMetaConnection |= om.isMetaRename();
		}
	};
}
