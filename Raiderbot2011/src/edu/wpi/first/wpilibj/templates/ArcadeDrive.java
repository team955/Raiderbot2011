package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class ArcadeDrive {

	RampingJaguar leftMotor;
	RampingJaguar rightMotor;
	double leftMotorSpeed, rightMotorSpeed;

	public ArcadeDrive(int analogSlot, int leftMotorChannel,
			int rightMotorChannel, double rampRate) {
		leftMotor = new RampingJaguar(analogSlot, leftMotorChannel,
				rampRate);
		rightMotor = new RampingJaguar(analogSlot, rightMotorChannel,
				rampRate);
	}

	public void drive(Joystick joy1) {
		leftMotorSpeed = joy1.getY() + joy1.getX();
		rightMotorSpeed = joy1.getY() - joy1.getX();
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
