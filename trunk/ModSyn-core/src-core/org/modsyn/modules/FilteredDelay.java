/*
 * Created on 29-jun-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.modules;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * @author DU1381
 *
 * A simple delay with feedback, with per sample accuracy.
 * Probably a bit faster than VarDelay.
 */
public class FilteredDelay implements SignalInsert {
    
    private final int delay;
    private final float feedback;
    private final float[] buffer;
    public final LPF lpf;
    
    private int pointer;
    private SignalInput connectedDevice;
    
    public FilteredDelay(Context context, float time, float feedback) {
        this.lpf = new LPF(context);
        this.delay = (int) (time * context.getSampleRate());
        this.feedback = feedback;
        this.pointer = 0;
        this.buffer = new float[delay];
        lpf.setCutOff(8000);
        lpf.setResonance(1);
    }
    
    
    /* (non-Javadoc)
     * @see org.modsyn.SignalInput#write(float)
     */
    @Override
	public void set(float sample) {
        float delayed = buffer[pointer];
        buffer[pointer] = lpf.process(sample + (delayed * feedback));
        pointer = (pointer + 1) % delay;
        
        connectedDevice.set(delayed);
    }

    /* (non-Javadoc)
     * @see org.modsyn.SignalOutput#connectTo(org.modsyn.SignalInput)
     */
    @Override
	public void connectTo(SignalInput input) {
        this.connectedDevice = input;
    }

}
