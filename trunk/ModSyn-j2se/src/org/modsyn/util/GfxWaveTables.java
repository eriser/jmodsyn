package org.modsyn.util;

import org.modsyn.SignalInputValue;
import org.modsyn.modules.ADSR;
import org.modsyn.modules.SoftClip;

public class GfxWaveTables {

	public static int SIZE = 128;

	public static float[] SOFTCLIP = new float[SIZE];
	public static float[] ADR = new float[SIZE];

	static {

		SoftClip sc = new SoftClip();

		ADSR adr = new ADSR(null);
		adr.ctrlAttack.set(0.04f);
		adr.ctrlDecay.set(0.995f);
		adr.ctrlRelease.set(0.94f);
		adr.connectTo(new SignalInputValue());
		adr.trigger.set(1);
		adr.velo.set(2);

		for (int i = 0; i < 128; i++) {
			SOFTCLIP[i] = sc.saturate(i / 64f, 0.5f);

			if (i == SIZE * 3 / 4) {
				adr.trigger.set(0);
			}
			adr.updateSignal();
			ADR[i] = ((SignalInputValue) adr.connectedInput).value;
		}
	}

}
