package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class SpecialSystems {

	Solenoid compressor;
	MyPneumatic clamp;
	MyPneumatic lifter;
	boolean clampButtonLS;
	boolean liftButtonLS;
	boolean compressorButtonLS;
	boolean compressorState;

	public SpecialSystems(int solenoidSlot, int compressorChannel,
			int clampChannel1, int clampChannel2, int lifterChannel1,
			int lifterChannel2) {
		compressor = new Solenoid(solenoidSlot, compressorChannel);
		clamp = new MyPneumatic(solenoidSlot, clampChannel1, clampChannel2);
		lifter = new MyPneumatic(solenoidSlot, lifterChannel1, lifterChannel2);
		compressorState = false;
	}

	public void run(Joystick joystick) {
		if (Vars.debounce(joystick.getRawButton(Vars.compressorButton),
				compressorButtonLS)) {
			compressorState = !compressorState;
		}
		if (Vars.debounce(joystick.getRawButton(Vars.clampButton),
				clampButtonLS)) {
			clamp.flip();
		}
		if (Vars.debounce(joystick.getRawButton(Vars.liftButton),
				liftButtonLS)) {
			lifter.flip();
		}

		compressor.set(compressorState);
		compressorButtonLS = joystick.getRawButton(Vars.compressorButton);
		clampButtonLS = joystick.getRawButton(Vars.clampButton);
		liftButtonLS = joystick.getRawButton(Vars.liftButton);
	}
}
