package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class RampingJaguar extends Jaguar{

	double timerCalibration, timerValue;
	double currentSpeed;
	double rampRate;

	public RampingJaguar(int slot, int channel, double rampRate) {
		super(slot, channel);
		timerCalibration = Global.timer.get();
		this.rampRate = rampRate;
	}

	public void set(double targetSpeed) {
		timerValue = Global.timer.get() - timerCalibration;
		if (Math.abs(targetSpeed - currentSpeed) > rampRate * timerValue) {
			currentSpeed += rampRate * timerValue
					* (targetSpeed > currentSpeed ? 1 : -1);
		} else {
			currentSpeed = targetSpeed;
		}
		super.set(currentSpeed);
		timerCalibration = Global.timer.get();
	}
}
