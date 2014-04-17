package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInput;
import org.modsyn.SignalInsert;

/**
 * Simplistic physical modeling of a speaker, simulating the weight/inertia of a speaker and the 'spring'
 * characteristics of the speaker wanting to return to 0.
 * 
 * @author media
 * 
 */
public class Speaker extends DefaultSignalOutput implements SignalInsert {

	float signal;
	float vel;

	// 0..1
	float spring = 1;
	float weight = 0.8f;

	public SignalInput ctrlWeight = new SignalInput() {
		@Override
		public void set(float signal) {
			weight = signal;
		}
	};
	public SignalInput ctrlSpring = new SignalInput() {
		@Override
		public void set(float signal) {
			spring = signal;
		}
	};

	@Override
	public void set(float signal) {

		float new_vel = signal - this.signal;
		float delta_vel = new_vel - vel;
		vel += delta_vel * (1 - weight);
		vel += (-this.signal * spring);
		this.signal += vel;

		connectedInput.set(this.signal);
	}

	public static void main(String[] args) {
		Speaker s = new Speaker();
		s.connectTo(new SignalInput() {
			@Override
			public void set(float signal) {
				System.out.println(signal);
			}
		});
		s.weight = 0.9f;
		s.spring = 0f;
		for (int i = 0; i < 100; i++) {
			s.set(i < 1 ? 1 : 0);
		}

	}
}
