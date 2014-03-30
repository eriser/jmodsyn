package org.modsyn.modules.fx;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.modules.Delay;

/**
 * A crude, but cheap reverb.
 * 
 * @author Erik Duijs
 */
public class CrudeVerb implements SignalInsert {
    
    private final Delay delay0, delay1, delay2, delay3;
    private SignalInput connectedDevice;
    
    public CrudeVerb(Context context, float time, float decay) {
        delay0 = new Delay(context, .050f * time, decay * 0.85f);
        delay1 = new Delay(context, .082f * time, decay * 0.85f);
        delay2 = new Delay(context, .073f * time, decay * 0.85f);
        delay3 = new Delay(context, .061f * time, decay * 0.85f);
    }

    @Override
	public void set(float sample) {
        float rev = delay0.process(sample);
        rev += delay1.process(sample);
        rev += delay2.process(sample);
        rev += delay3.process(sample);
        
        connectedDevice.set(rev);
    }

    @Override
	public void connectTo(SignalInput input) {
        connectedDevice = input;
    }
}
