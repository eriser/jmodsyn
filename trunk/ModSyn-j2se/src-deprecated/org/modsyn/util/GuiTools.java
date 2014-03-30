/*
 * Created on 25-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modsyn.Device;
import org.modsyn.DeviceControl;
import org.modsyn.Synth;

/**
 * @author Erik Duijs
 * 
 *         Tools for generating GUIs etc
 */
public class GuiTools {

	/**
	 * Add an ActionListener to the passed JComponent + all the components it contains
	 * 
	 * @param c
	 * @param l
	 */
	public static void addActionListenerToAll(JComponent c, KeyListener l) {
		c.addKeyListener(l);
		Component[] sc = c.getComponents();
		if (sc == null)
			return;
		for (int i = 0; i < sc.length; i++) {
			if (sc[i] instanceof JComponent) {
				addActionListenerToAll((JComponent) sc[i], l);
			}
		}
	}

	/**
	 * Generate a GUI component for a Device
	 * 
	 * @param device
	 * @return
	 */
	public static JComponent createComponent(final Device device) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		panel.setBorder(BorderFactory.createTitledBorder(device.getName()));

		JTabbedPane tabs = new JTabbedPane();

		DeviceControl[] dc = device.getDeviceControls();
		if (dc != null) {
			JPanel main = new JPanel();
			main.setLayout(new GridLayout(0, 1));
			main.setBorder(BorderFactory.createTitledBorder(device.getName()));
			for (int i = 0; i < dc.length; i++) {
				main.add(createComponent(dc[i]));
			}
			tabs.add(main);
		}

		Device[] subDevices = device.getSubDevices();
		if (subDevices != null) {
			for (int i = 0; i < subDevices.length; i++) {
				tabs.add(subDevices[i].getName(), createComponent(subDevices[i]));
			}
		}

		panel.add(tabs);

		return panel;
	}

	/**
	 * Create a GUI component for a DeviceControl
	 * 
	 * @param control
	 * @return
	 */
	public static JComponent createComponent(final DeviceControl control) {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(control.name));

		switch (control.type) {
		case DeviceControl.TYPE_DEFAULT_SLIDER:
			addDefaultSlider(panel, control);
			break;
		case DeviceControl.TYPE_COMBO:
			addComboBox(panel, control);
			break;
		}

		return panel;
	}

	/**
	 * @param panel
	 * @param control
	 */
	private static void addComboBox(JPanel panel, final DeviceControl control) {
		JComboBox<?> combo = new JComboBox<>(control.labels);
		combo.setSelectedIndex((int) control.getControlValue());
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<?> cb = (JComboBox<?>) e.getSource();
				control.setControlValue(cb.getSelectedIndex());
			}
		});
		panel.add(combo);
	}

	/**
	 * @param panel
	 * @param control
	 */
	private static void addComboBox(JPanel panel, final DeviceControl[] control) {
		JComboBox<?> combo = new JComboBox<>(control[0].labels);
		combo.setSelectedIndex((int) control[0].getControlValue());
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<?> cb = (JComboBox<?>) e.getSource();
				for (int i = 0; i < control.length; i++) {
					control[i].setControlValue(cb.getSelectedIndex());
				}
			}
		});
		panel.add(combo);
	}

	private static void addDefaultSlider(JComponent panel, final DeviceControl[] control) {
		int min = (int) (control[0].min * 1000f);
		int max = (int) (control[0].max * 1000f);
		final JSlider slider = new JSlider(min, max, (int) (control[0].getControlValue() * 1000f));
		final JTextField field = new JTextField(Float.toString(control[0].getControlValue()), 10);

		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					float val = source.getValue() / 1000f;
					field.setText(Float.toString(val));
					for (int i = 0; i < control.length; i++) {
						control[i].setControlValue(val);
					}
				}
			}
		});

		field.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					float val = Float.parseFloat(field.getText());
					slider.setValue((int) (val * 1000f));
					for (int i = 0; i < control.length; i++) {
						control[i].setControlValue(val);
					}
				} catch (NumberFormatException nfe) {
					// ignore for now
				}
			}
		});

		panel.add(slider);
		panel.add(field);

	}

	private static void addDefaultSlider(JComponent panel, final DeviceControl control) {
		int min = (int) (control.min * 1000f);
		int max = (int) (control.max * 1000f);
		final JSlider slider = new JSlider(min, max, (int) (control.getControlValue() * 1000f));
		final JTextField field = new JTextField(Float.toString(control.getControlValue()), 10);

		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					float val = source.getValue() / 1000f;
					field.setText(Float.toString(val));
					control.setControlValue(val);
				}
			}
		});

		field.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					float val = Float.parseFloat(field.getText());
					slider.setValue((int) (val * 1000f));
					control.setControlValue(val);
				} catch (NumberFormatException nfe) {
					// ignore for now
				}
			}
		});

		panel.add(slider);
		panel.add(field);

	}

	public static JComponent createLinkedComponent(Synth[] synth) {
		Device[] dev = new Device[synth.length];
		for (int i = 0; i < dev.length; i++) {
			dev[i] = (Device) synth[i];
		}

		return createLinkedComponent(dev);
	}

	public static JComponent createLinkedComponent(final Device[] devices) {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		// panel.setBorder(BorderFactory.createTitledBorder(devices[0].getName()));
		JTabbedPane tabs = new JTabbedPane();

		DeviceControl[] _dc = devices[0].getDeviceControls();
		Device[] _sub = devices[0].getSubDevices();

		JPanel main = new JPanel();

		if (_dc != null) {
			main.setLayout(new GridLayout(0, 1));
			for (int iControl = 0; iControl < _dc.length; iControl++) {
				DeviceControl[] linked = new DeviceControl[devices.length];
				for (int iLinked = 0; iLinked < devices.length; iLinked++) {
					linked[iLinked] = devices[iLinked].getDeviceControls()[iControl];
				}
				main.add(createLinkedComponent(linked));
			}
			if (_sub != null)
				tabs.add(main, devices[0].getName());
		}

		if (_sub != null) {
			for (int iSubDevice = 0; iSubDevice < _sub.length; iSubDevice++) {
				Device[] linked = new Device[devices.length];
				for (int iLinked = 0; iLinked < devices.length; iLinked++) {
					linked[iLinked] = devices[iLinked].getSubDevices()[iSubDevice];
				}

				tabs.add(linked[0].getName(), createLinkedComponent(linked));
			}
		}

		if (_sub != null) {
			panel.add(tabs);
		} else {
			panel.add(main);
		}

		return panel;
	}

	private static JComponent createLinkedComponent(final DeviceControl[] control) {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(control[0].name));

		switch (control[0].type) {
		case DeviceControl.TYPE_DEFAULT_SLIDER:
			addDefaultSlider(panel, control);
			break;
		case DeviceControl.TYPE_COMBO:
			addComboBox(panel, control);
			break;
		}

		return panel;
	}

	public static JComponent createTitlePanel(String title, String description) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.WHITE);

		JLabel lblTitle = new JLabel("<html><b>" + title + "</b></html>");
		JLabel lblDescr = new JLabel(description);

		panel.add(lblTitle, BorderLayout.NORTH);
		panel.add(lblDescr, BorderLayout.CENTER);
		panel.add(new JLabel("   "), BorderLayout.WEST);
		panel.setBorder(new EmptyBorder(7, 10, 7, 10));

		return panel;
	}
}
