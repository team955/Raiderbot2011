package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class MyPneumatic {

	Solenoid firstSolenoid;
	Solenoid secondSolenoid;
	boolean pneumaticState = false;

	public MyPneumatic(int slot, int firstSolenoidChannel,
			int secondSolenoidChannel) {
		firstSolenoid = new Solenoid(slot, firstSolenoidChannel);
		secondSolenoid = new Solenoid(slot, secondSolenoidChannel);
	}

	private void setSolenoids() {
		if (pneumaticState) {
			firstSolenoid.set(true);
			secondSolenoid.set(false);
		} else {
			firstSolenoid.set(false);
			secondSolenoid.set(true);
		}
	}

	public void flip() {
		pneumaticState = !pneumaticState;
		setSolenoids();
	}

	public void set(boolean state) {
		if (state) {
			pneumaticState = true;
		} else {
			pneumaticState = false;
		}
		setSolenoids();
	}
}
