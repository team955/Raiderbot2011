package edu.wpi.first.wpilibj.templates;

public class MyPIDController {

	double timerCalibration, timerValue;
	double kP, kI, kD;
	double P, I, D;
	double lastD = 0;
	double lastValue = 0;
	double output = 0, outputLS = 0, dOutput = 0;

	public MyPIDController(double kP, double kI, double kD) {
		timerCalibration = Vars.timer.get();
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}

	void run(double targetValue, double currentValue) {
		timerValue = Vars.timer.get() - timerCalibration;
		P = targetValue - currentValue;
		I = I + P * timerValue;
		D = (currentValue - lastValue) / timerValue;

		if (Math.abs(D) > 100) { // For use with encoders
			D = lastD;
		}

		lastD = D;
		lastValue = currentValue;
		outputLS = output;
		output = (kP * P) + (kI / (2 * timerValue) * I)  + (2 * kD * D);
		dOutput = (output - outputLS) / timerValue;
		timerCalibration = Vars.timer.get();
	}

	public double out(double targetValue, double currentValue) {
		run(targetValue, currentValue);
		return output;
	}

	public double dOut(double targetValue, double currentValue) {
		run(targetValue, currentValue);
		return dOutput;
	}
}
