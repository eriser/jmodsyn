package org.modsyn.modules.fx;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;
import org.modsyn.modules.AllPassDelay;
import org.modsyn.modules.Delay;
import org.modsyn.modules.LPF;

/**
 * Reverb using Schroeder's algorithm.
 * Uses 4 comb filters, and 2 all-pass filters
 * 
 * FLOAT fvgc_time[FVCombs] = {
>   1116,
>   1188,
>   1277,
>   1356,
>   1422,
>   1491,
>   1557,
>   1617
> };
> 
> FLOAT fvgc_gain[FVCombs] = {
>   .5f,
>   .5f,
>   .5f,
>   .5f,
>   .5f,
>   .5f,
>   .5f,
>   .5f
> };
> 
> FLOAT fvga_time[FVAlpas] = {
>   556,
>   441,
>   341,
>   225
> };
> 
> FLOAT fvga_gain[FVAlpas] = {
>   .5f,
>   .5f,
>   .5f,
>   .5f
> };
 * 
 * @author Erik Duijs
 */
public class Reverb184 implements SignalInsert {
    private SignalInput connectedDevice;
    Delay delay0, delay1, delay2, delay3, delay4, delay5, delay6, delay7;
    AllPassDelay apf0, apf1, apf2, apf3;
    LPF lpf;
    float dry, wet;
    
    float time, decay1, decay2;
	private final Context context;
    
    public Reverb184(Context context, float time, float decay, float density, float dry_wet) {
    	this.context = context;
        init(time, decay, density, dry_wet);
    }
    
    public Reverb184(Context context) {
    	this.context = context;
        init(1, 0.5f, 0.5f, 0.1f);
    }
    
    private void init(float time, float fbDelay, float fbAPF, float dry_wet) {
        delay0 = new Delay(context, (1116f / 44100f) * time, fbDelay);
        delay1 = new Delay(context, (1188f / 44100f) * time, fbDelay);
        delay2 = new Delay(context, (1277f / 44100f) * time, fbDelay);
        delay3 = new Delay(context, (1356f / 44100f) * time, fbDelay);
        delay4 = new Delay(context, (1422f / 44100f) * time, fbDelay);
        delay5 = new Delay(context, (1491f / 44100f) * time, fbDelay);
        delay6 = new Delay(context, (1557f / 44100f) * time, fbDelay);
        delay7 = new Delay(context, (1617f / 44100f) * time, fbDelay);
        apf0 = new AllPassDelay(context, (556f / 44100f) * time, fbAPF);
        apf1 = new AllPassDelay(context, (441f / 44100f) * time, fbAPF);
        apf2 = new AllPassDelay(context, (341f / 44100f) * time, fbAPF);
        apf3 = new AllPassDelay(context, (225f / 44100f) * time, fbAPF);
        
        lpf = new LPF(context);
        lpf.setCutOff(4000);
        lpf.setResonance(1);
        
        this.dry = 1f - dry_wet;
        this.wet = dry_wet;
    }
    
    /**
     * Process the reverb.
     * 
     * 
     * @param input is the sample to feed the reverb
     * @return the output from the reverb
     */
    public float process(float input) {
        float rev;
        
        // run through 8 parallel delays
        rev  = delay0.process(input);
        rev += delay1.process(input);
        rev += delay2.process(input);
        rev += delay3.process(input);
        rev += delay4.process(input);
        rev += delay5.process(input);
        rev += delay6.process(input);
        rev += delay7.process(input);
        
        rev = lpf.process(rev);
        
        // run mixed output of the 8 delays through 4 all-pass filters
        rev = apf0.process(rev);
        rev = apf1.process(rev);
        rev = apf2.process(rev);
        rev = apf3.process(rev);

        return (rev * wet) + (input * dry); 
    }

    @Override
	public void set(float input) {
        connectedDevice.set(process(input));
    }

    @Override
	public void connectTo(SignalInput input) {
        this.connectedDevice = input;;
    }

}
