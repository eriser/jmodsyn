/*
 * Created on Jun 16, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.modsyn.util;

public class Tools {

    public static final float[] FREQUENCY = new float[128];

    static {
        for (int key = 0; key < 128; key++) {
            FREQUENCY[key] = (float) (440f * Math.pow(2, (key - 69) / 12f));
        }
    }

    public static float getFreq(int MIDInote) {
        return FREQUENCY[MIDInote];
    }
}
