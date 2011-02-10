package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class MyPneumatic {

	Solenoid firstSolenoid;
	Solenoid secondSolenoid;
	boolean state = false;

	public MyPneumatic(int slot, int firstSolenoidChannel,
			int secondSolenoidChannel) {
		firstSolenoid = new Solenoid(slot, firstSolenoidChannel);
		secondSolenoid = new Solenoid(slot, secondSolenoidChannel);
	}

	public void set(boolean state) {
		firstSolenoid.set(state);
		secondSolenoid.set(!state);
	}

	public void flip() {
		state = !state;
		set(state);
	}
}
