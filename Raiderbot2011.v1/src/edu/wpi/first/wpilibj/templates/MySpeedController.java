package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class MySpeedController {

	Jaguar jaguar;
	MyPIDController PIDController;
	double speed;
	static double kP, kI, kD;

	static void pidInit(double kP, double kI, double kD) {
		MySpeedController.kP = kP;
		MySpeedController.kI = kI;
		MySpeedController.kD = kD;
	}

	public MySpeedController(int slot, int channel) {
		jaguar = new Jaguar(slot, channel);
		PIDController = new MyPIDController(kP, kI, kD);
	}

	public void set(double targetSpeed) {
		speed = PIDController.out(targetSpeed, jaguar.get());
		if (speed > 1) {
			speed = 1;
		}
		if (speed < -1) {
			speed = -1;
		}
		jaguar.set(speed);
	}
}
