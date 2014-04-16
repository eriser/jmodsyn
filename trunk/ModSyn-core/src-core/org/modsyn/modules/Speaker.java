package org.modsyn.modules;

import org.modsyn.DefaultSignalOutput;
import org.modsyn.SignalInsert;

public class Speaker extends DefaultSignalOutput implements SignalInsert {

	float signal;
	float vel;

	// 0..1
	float weight = 0.8f;

	@Override
	public void set(float signal) {
		this.signal += vel;
		vel -= (vel * weight);

		float new_vel = signal - this.signal;
		float delta_vel = new_vel - vel;
		vel += delta_vel * (1 - weight);

		System.out.println(this.signal);
	}

	public static void main(String[] args) {
		Speaker s = new Speaker();

		for (int i = 0; i < 200; i++) {
			s.set(i < 10 ? 1 : 0);
		}

	}
}
