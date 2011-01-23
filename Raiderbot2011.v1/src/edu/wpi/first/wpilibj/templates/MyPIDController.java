package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class MyPIDController {

	Timer timer;
	double kP, kI, kD;
	double P, I, D;
	double lastD = 0;
	double lastValue = 0;
	double output = 0, outputLS = 0, dOutput = 0;

	public MyPIDController(double kP, double kI, double kD) {
		timer = new Timer();
		timer.start();
		timer.reset();
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}

	void run(double targetValue, double currentValue) {
		P = targetValue - currentValue;
		I = I + P * timer.get();
		D = (currentValue - lastValue);

		if (Math.abs(D) > 100) { // For use with encoders
			D = lastD;
		}

		lastD = D;
		lastValue = currentValue;
		dOutput = (output - outputLS) / timer.get();
		outputLS = output;
		output = kP * P + kI * I + kD * D;
		timer.reset();
	}

	public double out(double targetValue, double currentValue) {
		run(targetValue, currentValue);
		return output;
	}

	public double dOut(double targetValue, double currentValue) {
		run(targetValue, currentValue);
		return dOutput;
	}
	/**************************TUNING MYPIDCONTROLLER***************************
	 * Set kP, kI, and kD to 0
	 * Raise kP until output oscillates consistently around setpoint
	 * Increase kD until it stop oscillating
	 * Increase kI until output stops within a reasonable range of setpoint
	 **************************************************************************/
}
