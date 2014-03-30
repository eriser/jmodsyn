/*
 * Created on 2-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.modules;

import java.util.Random;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;

/**
 * Oscillator that generates tones using a noise generator + comb filter. Useful
 * for creating simulations of string instruments.
 * 
 * @author Erik Duijs
 */
public class CombOscillator implements SignalSource {

    public final SignalInput feedbackControl = new SignalInput() {
        @Override
        public void set(float data) {
            feedback = data;
        }
    };

    public final SignalInput frequencyControl = new SignalInput() {
        @Override
        public void set(float freqHz) {
            setFrequency(freqHz);
        }
    };

    protected SignalInput connectedDevice;

    protected float delay;
    protected float feedback;
    protected int pointer;
    protected float[] buffer;

    private final Random random;

	private Context context;

    public CombOscillator(Context context) {
        this.context = context;
        this.random = new Random();
        initDelay(1, 0, 0.98f);

        context.addSignalSource(this); 
    }

    private void initDelay(float maxDelayTimeSecs, float delayTimeSecs, float feedback) {
        if (delayTimeSecs > maxDelayTimeSecs) {
            throw new IllegalArgumentException("The delay time of " + delayTimeSecs + " is higher than the maximum of " + maxDelayTimeSecs + " seconds.");
        }
        this.delay = delayTimeSecs * context.getSampleRate();
        this.feedback = feedback;
        this.pointer = 0;
        this.buffer = new float[(int) (maxDelayTimeSecs * context.getSampleRate())];
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modsyn.UpdatedSignal#updateSignal()
     */
    @Override
    public void updateSignal() {
        // generate noise
        final float sample = (random.nextFloat() * 2f) - 1;

        // process interpolated comb filter
        final float delayPointer = ((pointer - delay) + buffer.length) % buffer.length;
        final int iDelayPtr0 = (int) delayPointer;
        final int iDelayPtr1 = (iDelayPtr0 + 1) % buffer.length;
        final float deltaPtr = delayPointer - iDelayPtr0;
        final float dly0 = buffer[iDelayPtr0];
        final float dly1 = buffer[iDelayPtr1];
        final float deltaDly = dly1 - dly0;
        final float delayed = dly0 + (deltaPtr * deltaDly);

        // TODO:
        // run delayed signal through LPF

        final float fbLine = delayed * feedback;

        buffer[pointer] = (sample + fbLine);
        pointer = (pointer + 1) % buffer.length;

        connectedDevice.set(delayed);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modsyn.SignalOutput#connectTo(org.modsyn.SignalInput)
     */
    @Override
    public void connectTo(SignalInput input) {
        connectedDevice = input;
    }

    public void setDelayTime(float secs) {
        this.delay = secs * context.getSampleRate();
    }

    public void setFrequency(float freqHz) {
        setDelayTime(1f / freqHz);
    }

    // feedback = 0f -> noise, feedback = 1f -> tone with all harmonics
    public void setFeedback(float feedback) {
        this.feedback = (feedback);
    }
}
