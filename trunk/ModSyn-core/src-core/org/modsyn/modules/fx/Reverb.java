package org.modsyn.modules.fx;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.modules.AllPassFilter;
import org.modsyn.modules.Delay;
import org.modsyn.modules.Mixer;

public class Reverb implements SignalInsert {
    
    Delay delay1, delay2, delay3, delay4;
    AllPassFilter apf1, apf2;
    Mixer mix;
    
    public Reverb(Context context, float time, float decay) {
    	mix = new Mixer(context);
        delay1 = new Delay(context, .050f * time, decay * 0.85f);
        delay2 = new Delay(context, .082f * time, decay * 0.85f);
        delay3 = new Delay(context, .073f * time, decay * 0.85f);
        delay4 = new Delay(context, .061f * time, decay * 0.85f);
        apf1 = new AllPassFilter(context, 0.015f * time, decay);
        apf2 = new AllPassFilter(context, 0.006f * time, decay);
        
        mix.addChannel(delay1);
		mix.addChannel(delay2);
		mix.addChannel(delay3);
		mix.addChannel(delay4);

        mix.connectTo(apf1);
        apf1.connectTo(apf2);
    }

    @Override
	public void set(float sample) {
    	delay1.set(sample);
		delay2.set(sample);
		delay3.set(sample);
		delay4.set(sample);
    }

    @Override
	public void connectTo(SignalInput input) {
        apf2.connectTo(input);
    }

}
