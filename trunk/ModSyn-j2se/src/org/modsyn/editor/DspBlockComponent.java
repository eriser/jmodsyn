package org.modsyn.editor;

import static java.lang.Math.max;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
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

	private final static Font font = new Font("Arial", Font.BOLD, 9);

	public DspBlockComponent(Context c, DspBlockModel<?> model, DspPatchModel patchModel) {
		this(c, model, patchModel, 0, 0, 80, -1);
	}

	public DspBlockComponent(Context c, DspBlockModel<?> model, final DspPatchModel patchModel, int x, int y, int w, int h) {
		super(new BorderLayout());

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

		button = new JButton();

		Mover m = new Mover();
		name.addMouseListener(m);
		name.addMouseMotionListener(m);

		name.setForeground(Color.WHITE);
		name.setBackground(Color.BLACK);
		name.setOpaque(true);
		boolean isMeta = getModel() instanceof MetaModel;
		if (isMeta) {
			name.setFont(name.getFont().deriveFont(Font.ITALIC));
		}

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
			inputList.setDropMode(DropMode.ON);
			inputList.setTransferHandler(th);
			inputList.setDragEnabled(true);
			inputList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			inputList.setFont(font);
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
			outputList.setTransferHandler(th);
			outputList.setDragEnabled(true);
			outputList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			outputList.setFont(font);

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

		setBounds(x, y, w, height);
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

	public static final Color META_COLOR = new Color(0x006000);

	public void setSelected(boolean b) {
		this.selected = b;
		boolean isMeta = getModel() instanceof MetaModel;
		setBorder(BorderFactory.createLineBorder(b ? Color.BLUE : isMeta ? META_COLOR : Color.BLACK));
		name.setBackground(b ? Color.BLUE : isMeta ? META_COLOR : Color.BLACK);
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
			getParent().repaint();
		}
	}

	public void snapToGrid() {
		int x1 = getX() + 12;
		int y1 = getY() + 12;
		int x2 = getX() - (getX() % 25);
		int y2 = getY() - (getY() % 25);

		if (!patchModel.isMainModel) {
			List<DspBlockComponent> linked = patchModel.parent.getLinkedBlockComponents(this);
			for (DspBlockComponent dbc : linked) {
				dbc.setLocation(x1, y1);
				dbc.setLocation(x2, y2);
			}
		} else {
			setLocation(x1, y1);
			setLocation(x2, y2);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		repaint();
	};
}
