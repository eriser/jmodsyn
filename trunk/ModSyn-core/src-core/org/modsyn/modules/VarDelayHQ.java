package org.modsyn.modules;

import org.modsyn.Context;

/**
 * @author Erik Duijs
 */
public class VarDelayHQ extends VarDelay {

    public VarDelayHQ(Context context, float maxDelayTimeSecs, float delayTimeSecs, float feedback) {
        super(context, maxDelayTimeSecs, delayTimeSecs, feedback);
    }

    @Override
    public void set(float sample) {
        int buflen = buffer.length;
        float delayPointer = ((pointer - delay) + buflen) % buflen;

        int iDelayPtr0 = (int) delayPointer;
        float dly0 = buffer[iDelayPtr0];

        float deltaPtr = delayPointer - iDelayPtr0;

        int iDelayPtr1 = (iDelayPtr0 + 1) % buflen;
        float dly1 = buffer[iDelayPtr1];

        float deltaDly = dly1 - dly0;
        float delayed = dly0 + (deltaPtr * deltaDly);

        buffer[pointer] = (sample + delayed * feedback);
        pointer = (pointer + 1) % buflen;

        connectedDevice.set(delayed);// / (1f + feedback));
    }
}
