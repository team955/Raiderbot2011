package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class MyJoystick extends Joystick {

	boolean ls[] = {false, false, false, false, false, false, false, false,
		false, false, false, false,};
	boolean output;

	public MyJoystick(int port) {
		super(port);
	}

	public boolean debounce(int input) {
		output = Global.debounce(getRawButton(input), ls[input]);
		ls[input] = getRawButton(input);
		return output;
	}
}
