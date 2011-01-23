package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class RampingJaguar {

	Jaguar jaguar;
	double timerCalibration, timerValue;
	double currentSpeed;
	double rampRate;

	public RampingJaguar(int slot, int channel, double rampRate) {
		jaguar = new Jaguar(slot, channel);
		timerCalibration = Vars.timer.get();
		this.rampRate = rampRate;
	}

	public void set(double targetSpeed) {
		timerValue = Vars.timer.get() - timerCalibration;
		if (Math.abs(targetSpeed - currentSpeed) > rampRate * timerValue) {
			currentSpeed += rampRate * timerValue
					* (targetSpeed > currentSpeed ? 1 : -1);
		} else {
			currentSpeed = targetSpeed;
		}
		jaguar.set(currentSpeed);
		timerCalibration = Vars.timer.get();
	}
}
