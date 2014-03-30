/*
 * Created on Apr 29, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.example;

/**
 * @author Erik Duijs
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modsyn.Context;
import org.modsyn.ContextFactory;

/**
 * @author edy
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class TestFrame2 extends JFrame {

	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    
    JSlider osc1FreqSlide = new JSlider(JSlider.HORIZONTAL, 10, 2010, 300);
	JSlider modSlide = new JSlider(JSlider.HORIZONTAL, 0, 100, 80);   

	
	TestFrame2AudioThread thread;
	
	/**
	 * Constructor
	 */
	public TestFrame2(Context context) {
		super();
		thread = new TestFrame2AudioThread(context);
		thread.start();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		genGui();
		pack();
		setVisible(true);
	}

	/**
	 *  
	 */
	private void genGui() {
		this.setTitle("Real Time Audio Synthesis demo 2");
		this.setResizable(false);
		JPanel contentPane = (JPanel)this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		JPanel downLeft = new JPanel();
		downLeft.setBorder(new EtchedBorder());
		downLeft.setLayout(new GridLayout(0,1));
		

		osc1FreqSlide.setMajorTickSpacing(500);
		osc1FreqSlide.setMinorTickSpacing(100);
		osc1FreqSlide.setPaintTicks(true);
		osc1FreqSlide.setPaintLabels(true);

		modSlide.setMajorTickSpacing(50);
		modSlide.setMinorTickSpacing(10);
		modSlide.setPaintTicks(true);
		modSlide.setPaintLabels(true);


		/*
		 * Close window event
		 */
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
		
		osc1FreqSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				thread.oscfreq = osc1FreqSlide.getValue();
			}

		});
		modSlide.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				thread.mod = modSlide.getValue();
			}

		});


		
		downLeft.add(new JLabel("Modulator Hz", JLabel.CENTER));
		downLeft.add(osc1FreqSlide);
		downLeft.add(new JLabel("Modulation amount", JLabel.CENTER));
		downLeft.add(modSlide);

		
		contentPane.add(downLeft, BorderLayout.CENTER);
	}
	
	public static void main(String args[]) {
		new TestFrame2(ContextFactory.create());
	}
}