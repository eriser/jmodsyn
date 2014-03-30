/*
 * Created on Apr 22, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.modules;

import java.util.Random;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalSource;

/**
 * @author Erik Duijs
 * 
 *         White noise generator
 */
public class NoiseGenerator implements SignalSource {

    private SignalInput connectedDevice;
    private float buffer;
    private final Random random = new Random();

    public NoiseGenerator(Context context) {
    	context.addSignalSource(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modsyn.Output#connectTo(org.modsyn.Input)
     */
    @Override
    public void connectTo(SignalInput input) {
        connectedDevice = input;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modsyn.SoundGenerator#update()
     */
    @Override
    public void updateSignal() {
        buffer = random.nextFloat();
        connectedDevice.set(buffer);
    }

}
