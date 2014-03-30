/*
 * Created on 27-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modsyn.modules.fx;

import org.modsyn.Context;
import org.modsyn.SignalInput;

/**
 * @author DU1381
 *
 * Stereo version of Reverb142
 */
public class Reverb284 implements SignalInput {
    
    public final Reverb184 left, right;
    
    public Reverb284(Context context) {
        left  = new Reverb184(context, 2.00f, 0.7f, 0.5f, 0.1f);
        right = new Reverb184(context, 2.01f, 0.7f, 0.5f, 0.1f);
    }

    public Reverb284(Context context, float time, float decay, float density, float dry_wet) {
        left = new Reverb184(context, time, decay, density, dry_wet);
        right = new Reverb184(context, time * 1.01f, decay, density, dry_wet);
    }

    /**
     * This is the MONO input of the reverb.
     * If you need stereo input, write to revL and revR separately.
     * 
     * @see org.modsyn.SignalInput#set(float)
     */
    @Override
	public void set(float sample) {
        left.set(sample);
        right.set(sample);
    }

}
