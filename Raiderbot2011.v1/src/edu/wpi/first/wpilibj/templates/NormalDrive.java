package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class NormalDrive {

	MySpeedController leftMotor;
	MySpeedController rightMotor;
	double leftMotorSpeed, rightMotorSpeed;

	public NormalDrive(int analogSlot, int leftMotorChannel,
			int rightMotorChannel) {
		leftMotor = new MySpeedController(analogSlot, leftMotorChannel);
		rightMotor = new MySpeedController(analogSlot, rightMotorChannel);
	}

	public void drive(Joystick joy1) {
		leftMotorSpeed = Vars.limit(-joy1.getY() + joy1.getX(), -1, 1);
		rightMotorSpeed = Vars.limit(joy1.getY() + joy1.getX(), -1, 1);
		leftMotor.set(leftMotorSpeed);
		rightMotor.set(rightMotorSpeed);
	}

	public void setLeftMotor(double speed) {
		leftMotor.set(speed);
	}

	public void setRightMotor(double speed) {
		rightMotor.set(speed);
	}
}
