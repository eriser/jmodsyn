package org.modsyn;
/*
 * Created on 6-jul-07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */


/**
 * @author Erik Duijs
 */
public class DeviceControl {

    public static final int TYPE_DEFAULT_SLIDER = 0;
    public static final int TYPE_COMBO = 1;

    float value;
    public final int type;
    public final float min, max;
    public final String name;
    public final String[] labels;
    SignalInput controlInput;

    public DeviceControl(String name, SignalInput controlInput, float defaultValue, float minValue, float maxValue) {
        this.type = TYPE_DEFAULT_SLIDER;
        this.name = name;
        this.controlInput = controlInput;
        this.value = defaultValue;
        this.min = minValue;
        this.max = maxValue;
        this.labels = null;
        controlInput.set(defaultValue);
    }

    public DeviceControl(String name, SignalInput controlInput, int defaultIndex, String[] labels) {
        this.type = TYPE_COMBO;
        this.name = name;
        this.controlInput = controlInput;
        this.value = defaultIndex;
        this.labels = labels;
        this.min = this.max = -1;
        controlInput.set(defaultIndex);
    }

    public void setControlValue(float value) {
        this.value = value;
        controlInput.set(value);
    }

    public float getControlValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
