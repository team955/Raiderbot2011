package edu.wpi.first.wpilibj.templates;

public class MyPIDController {

	double timerCalibration, timerValue;
	double kP, kI, kD;
	double P, I, D;
	double lastD = 0;
	double lastValue = 0;
	double output = 0;

	public MyPIDController(double kP, double kI, double kD) {
		timerCalibration = Global.timer.get();
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}

	public double out(double targetValue, double currentValue) {
		timerValue = Global.timer.get() - timerCalibration;
		P = targetValue - currentValue;
		I = I + P * timerValue;
		D = (currentValue - lastValue) / timerValue;
		if (Math.abs(D) > 512) { // For use with encoders
			D = lastD;
		}

		lastD = D;
		lastValue = currentValue;
		output = (kP * P) + (kI * I) + (kD * D);
		timerCalibration = Global.timer.get();
		return output;
	}

	public void reset(double currentValue){
		lastValue = currentValue;
		lastD = 0;
	}
}
