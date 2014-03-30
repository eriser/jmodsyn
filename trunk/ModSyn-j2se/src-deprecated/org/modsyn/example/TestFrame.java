/*
 * Created on Apr 28, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package org.modsyn.example;

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
public class TestFrame extends JFrame {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    
    JSlider osc1FreqSlide = new JSlider(JSlider.HORIZONTAL, 0, 500, 100);
    JSlider osc2FreqSlide = new JSlider(JSlider.HORIZONTAL, 0, 500, 100);   
    JSlider pwm1Slide = new JSlider(JSlider.HORIZONTAL, 0, 50, 50);
    JSlider pwm2Slide = new JSlider(JSlider.HORIZONTAL, 0, 50, 50);   
    JSlider lpf1CoSlide = new JSlider(JSlider.HORIZONTAL, 0, 5000, 2000);
    JSlider lpf2CoSlide = new JSlider(JSlider.HORIZONTAL, 0, 5000, 2000);   
    JSlider lpf1QSlide = new JSlider(JSlider.HORIZONTAL, 1, 21, 1);
    JSlider lpf2QSlide = new JSlider(JSlider.HORIZONTAL, 1, 21, 1);   
    JSlider amp1Slide = new JSlider(JSlider.HORIZONTAL, 0, 50, 25);
    JSlider amp2Slide = new JSlider(JSlider.HORIZONTAL, 0, 50, 25);
    JSlider pan1Slide = new JSlider(JSlider.HORIZONTAL, -100, 100, -50);
    JSlider pan2Slide = new JSlider(JSlider.HORIZONTAL, -100, 100, +50);
    JSlider lfo1aSlide = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
    JSlider lfo2aSlide = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
    JSlider lfo1fSlide = new JSlider(JSlider.HORIZONTAL, 0, 15, 0);
    JSlider lfo2fSlide = new JSlider(JSlider.HORIZONTAL, 0, 15, 0);
    
    TestFrameAudioThread thread;

    
    /**
     * Constructor
     */
    public TestFrame(Context context) {
        super();
        thread = new TestFrameAudioThread(context);
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
        this.setTitle("Real Time Audio Synthesis demo 1");
		this.setResizable(false);
        JPanel contentPane = (JPanel)this.getContentPane();
		contentPane.setLayout(new BorderLayout());

        JPanel downLeft = new JPanel();
        downLeft.setBorder(new EtchedBorder());
        JPanel downRight = new JPanel();
        downRight.setBorder(new EtchedBorder());
        downLeft.setLayout(new GridLayout(0,2));
        downRight.setLayout(new GridLayout(0,2));
        

        osc1FreqSlide.setMajorTickSpacing(500);
        osc1FreqSlide.setMinorTickSpacing(100);
        osc1FreqSlide.setPaintTicks(true);
        osc1FreqSlide.setPaintLabels(true);

        osc2FreqSlide.setMajorTickSpacing(500);
        osc2FreqSlide.setMinorTickSpacing(100);
        osc2FreqSlide.setPaintTicks(true);
        osc2FreqSlide.setPaintLabels(true);

        amp1Slide.setMajorTickSpacing(50);
        amp1Slide.setMinorTickSpacing(10);
        amp1Slide.setPaintTicks(true);
        amp1Slide.setPaintLabels(true);

        amp2Slide.setMajorTickSpacing(50);
        amp2Slide.setMinorTickSpacing(10);
        amp2Slide.setPaintTicks(true);
        amp2Slide.setPaintLabels(true);

        pan1Slide.setMajorTickSpacing(50);
        pan1Slide.setMinorTickSpacing(10);
        pan1Slide.setPaintTicks(true);
        pan1Slide.setPaintLabels(true);

        pan2Slide.setMajorTickSpacing(50);
        pan2Slide.setMinorTickSpacing(10);
        pan2Slide.setPaintTicks(true);
        pan2Slide.setPaintLabels(true);
        
        pwm1Slide.setMajorTickSpacing(50);
        pwm1Slide.setMinorTickSpacing(10);
        pwm1Slide.setPaintTicks(true);
        pwm1Slide.setPaintLabels(true);

        pwm2Slide.setMajorTickSpacing(50);
        pwm2Slide.setMinorTickSpacing(10);
        pwm2Slide.setPaintTicks(true);
        pwm2Slide.setPaintLabels(true);

        lpf1CoSlide.setMajorTickSpacing(2500);
        lpf1CoSlide.setMinorTickSpacing(250);
        lpf1CoSlide.setPaintTicks(true);
        lpf1CoSlide.setPaintLabels(true);

        lpf2CoSlide.setMajorTickSpacing(2500);
        lpf2CoSlide.setMinorTickSpacing(250);
        lpf2CoSlide.setPaintTicks(true);
        lpf2CoSlide.setPaintLabels(true);

        lpf1QSlide.setMajorTickSpacing(10);
        lpf1QSlide.setMinorTickSpacing(2);
        lpf1QSlide.setPaintTicks(true);
        lpf1QSlide.setPaintLabels(true);

        lpf2QSlide.setMajorTickSpacing(10);
        lpf2QSlide.setMinorTickSpacing(2);
        lpf2QSlide.setPaintTicks(true);
        lpf2QSlide.setPaintLabels(true);

        lfo1aSlide.setMajorTickSpacing(250);
        lfo1aSlide.setMinorTickSpacing(20);
        lfo1aSlide.setPaintTicks(true);
        lfo1aSlide.setPaintLabels(true);

        lfo2aSlide.setMajorTickSpacing(250);
        lfo2aSlide.setMinorTickSpacing(20);
        lfo2aSlide.setPaintTicks(true);
        lfo2aSlide.setPaintLabels(true);

        lfo1fSlide.setMajorTickSpacing(1);
        lfo1fSlide.setMinorTickSpacing(1);
        lfo1fSlide.setPaintTicks(true);
        lfo1fSlide.setPaintLabels(true);

        lfo2fSlide.setMajorTickSpacing(1);
        lfo2fSlide.setMinorTickSpacing(1);
        lfo2fSlide.setPaintTicks(true);
        lfo2fSlide.setPaintLabels(true);
                /*
         * Close window event
         */
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
			public void windowClosing(java.awt.event.WindowEvent e) {
                //thread.stop();
                thread = null;
                System.exit(0);
            }
        });
        
        osc1FreqSlide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.osc1freq = osc1FreqSlide.getValue();
            }

        });
        osc2FreqSlide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.osc2freq = osc2FreqSlide.getValue();
            }

        });

        amp1Slide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.a1 = amp1Slide.getValue();
            }

        });
        amp2Slide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.a2 = amp2Slide.getValue();
            }

        });

        pan1Slide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.p1 = pan1Slide.getValue();
            }
        });
        pan2Slide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.p2 = pan2Slide.getValue();
            }
        });

        pwm1Slide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.pwm1 = pwm1Slide.getValue();
            }
        });
        pwm2Slide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.pwm2 = pwm2Slide.getValue();
            }
        });

        lpf1CoSlide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.co1 = lpf1CoSlide.getValue();
            }
        });
        lpf2CoSlide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.co2 = lpf2CoSlide.getValue();
            }
        });

        lpf1QSlide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.q1 = lpf1QSlide.getValue();
            }
        });
        lpf2QSlide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.q2 = lpf2QSlide.getValue();
            }
        });

        lfo1aSlide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.lfo1a = lfo1aSlide.getValue();
            }
        });
        lfo2aSlide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.lfo2a = lfo2aSlide.getValue();
            }
        });

        lfo1fSlide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.lfo1f = lfo1fSlide.getValue();
            }
        });
        lfo2fSlide.addChangeListener(new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent e) {
                thread.lfo2f = lfo2fSlide.getValue();
            }
        });
        
        downLeft.add(new JLabel("Saw Tooth Hz", JLabel.TRAILING));
        downLeft.add(osc1FreqSlide);
        downRight.add(new JLabel("Square Hz", JLabel.TRAILING));
        downRight.add(osc2FreqSlide);
        downLeft.add(new JLabel("PWM %", JLabel.TRAILING));
        downLeft.add(pwm1Slide);
        downRight.add(new JLabel("PWM %", JLabel.TRAILING));
        downRight.add(pwm2Slide);
        downLeft.add(new JLabel("LPF Cut Off Hz", JLabel.TRAILING));
        downLeft.add(lpf1CoSlide);
        downRight.add(new JLabel("LPF Cut Off Hz", JLabel.TRAILING));
        downRight.add(lpf2CoSlide);
        downLeft.add(new JLabel("LPF Resonance", JLabel.TRAILING));
        downLeft.add(lpf1QSlide);
        downRight.add(new JLabel("LPF Resonance", JLabel.TRAILING));
        downRight.add(lpf2QSlide);
        downLeft.add(new JLabel("LFO freq (cut-off)", JLabel.TRAILING));
        downLeft.add(lfo1fSlide);
        downRight.add(new JLabel("LFO freq (cut-off)", JLabel.TRAILING));
        downRight.add(lfo2fSlide);
        downLeft.add(new JLabel("LFO amp (cut-off)", JLabel.TRAILING));
        downLeft.add(lfo1aSlide);
        downRight.add(new JLabel("LFO amp (cut-off)", JLabel.TRAILING));
        downRight.add(lfo2aSlide);
        downLeft.add(new JLabel("Panning %", JLabel.TRAILING));
        downLeft.add(pan1Slide);
        downRight.add(new JLabel("Panning %", JLabel.TRAILING));
        downRight.add(pan2Slide);
        downLeft.add(new JLabel("Gain", JLabel.TRAILING));
        downLeft.add(amp1Slide);
        downRight.add(new JLabel("Gain", JLabel.TRAILING));
        downRight.add(amp2Slide);
        
        contentPane.add(downLeft, BorderLayout.WEST);
        contentPane.add(downRight, BorderLayout.EAST);
    }
    
    public static void main(String args[]) {
        new TestFrame(ContextFactory.create());
    }
}
