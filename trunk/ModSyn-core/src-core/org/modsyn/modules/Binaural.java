package org.modsyn.modules;

import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.max;
import static java.lang.Math.sqrt;

import org.modsyn.Context;
import org.modsyn.SignalInput;
import org.modsyn.SignalInputValue;
import org.modsyn.SignalOutput;

/**
 * Simple binaural acoustics simulation of a positional signal source in 2D space. It simulates the delay and
 * attenuation based on distance (so the Doppler effect can be heard with moving objects), and filtering based on angle.
 * 
 * @author Erik Duijs
 */
public class Binaural implements SignalInput {

	private final Context context;
	public final SignalInputValue posX = new SignalInputValue(0);
	public final SignalInputValue posY = new SignalInputValue(1);
	public final Ear leftEar, rightEar;

	public static class Ear implements SignalInput {
		private final Binaural b;
		private final float xoffs;
		private final float nearest; // nearest distance limit = max volume

		private final VarDelayHQ delay;
		private final Amplifier amp;
		private final FilterXPole filter;

		public final SignalOutput out;

		Ear(Binaural b, float xoffs, float nearest) {
			this.b = b;
			this.xoffs = xoffs;
			this.nearest = nearest;
			this.delay = new VarDelayHQ(b.context, 2, 1, 0);
			this.amp = new Amplifier();
			this.filter = new FilterXPole();
			this.out = filter;

			delay.connectTo(amp);
			amp.connectTo(filter);

			filter.ctrlReso.set(0);
			filter.ctrlPoles.set(8);
		}

		@Override
		public void set(float signal) {
			float x = b.posX.value + xoffs;
			float y = b.posY.value;

			float dist = (float) sqrt((x * x) + (y * y));
			delay.setDelayTime(dist / Context.SPEED_OF_SOUND_MPS);

			float limDist = max(nearest, dist);
			float vol = 1f / (limDist);
			amp.control.set(vol);

			// Calculate filtering based on relative position.
			// This could be much refined; now it simply filters most when the source is exactly behind you and doesn't
			// filter when it's exactly in front.
			float angle = (float) asin(x / dist);
			float fAmount = (float) (angle / (PI * 2));
			if (y < 0) {
				fAmount = 0.5f - fAmount;
			}
			if (fAmount < 0) {
				fAmount += 1;
			}
			if (fAmount > 0.5f) {
				fAmount = 1 - fAmount;
			}
			fAmount = fAmount + fAmount; // 0..1;

			filter.ctrlCutoff.set(1 - fAmount * 0.4f);

			delay.set(signal);
		}
	}

	public Binaural(Context c) {
		this.context = c;
		this.leftEar = new Ear(this, -0.08f, 0.1f);
		this.rightEar = new Ear(this, +0.08f, 0.1f);
	}

	@Override
	public void set(float signal) {
		leftEar.set(signal);
		rightEar.set(signal);
	}
}
