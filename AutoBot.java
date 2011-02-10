package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class AutoBot {

	DigitalInput leftSensor, middleSensor, rightSensor;
	boolean left, middle, right;
	boolean init = false;
	boolean leftSide;
	boolean straight = false, leftFork = false, pastFork = true;
	double speed, wheelAngle;
	double base = 0, bigCorrection = Math.PI/4, smallCorrection;
	int armPosition;
	double scoreTime;
	boolean clawOut;

	public AutoBot(int leftChannel, int middleChannel, int rightChannel) {
		leftSensor = new DigitalInput(leftChannel);
		middleSensor = new DigitalInput(middleChannel);
		rightSensor = new DigitalInput(rightChannel);
		smallCorrection = bigCorrection / 3;
	}

	public void autoRun(SwerveDrive drive, SpecialSystems systems, DriverStation station) {
		left = !leftSensor.get();
		middle = !middleSensor.get();
		right = !rightSensor.get();
		if (!init) {
			setup(station, systems);
		}
		correctAngles();
		drive.setMainMotor(speed);
		drive.setFrontSwivelAngle(base + wheelAngle);
		drive.setBackSwivelAngle(base + wheelAngle);
		atTarget(systems, station);
		systems.clawOut(clawOut);
		//System.out.println("speed " + speed + " angle " + (base + wheelAngle));
	}

	private void setup(DriverStation station, SpecialSystems systems) {
		station = DriverStation.getInstance();
		straight = station.getDigitalIn(1);
		leftFork = station.getDigitalIn(2);
		armPosition = 6 + (straight ? 1 : 0);
		speed = Global.autoSpeed;
		scoreTime = (straight ? Global.straightScoreTime : Global.forkScoreTime);
		pastFork = straight ? true : false;
		init = true;
		systems.autoPosition(armPosition, station);
	}

	private void correctAngles() {
		if (!left && middle && !right) {
			wheelAngle = 0;
		}
		if (left && !middle && !right) {
			wheelAngle = -bigCorrection;
			leftSide = false;
		}
		if (!left && !middle && right) {
			wheelAngle = bigCorrection;
			leftSide = true;
		}
		if (left && middle && !right) {
			wheelAngle = smallCorrection;
			leftSide = false;
		}
		if (!left && middle && right) {
			wheelAngle = -smallCorrection;
			leftSide = true;
		}
		if (!left && !middle && !right) {
			if (leftSide) {
				wheelAngle = bigCorrection;
			} else {
				wheelAngle = -bigCorrection;
			}
		}
		if (left && middle && right) {
			if (!pastFork && Global.timer.get() > Global.forkTime) {
				base += Global.forkAngle * (leftFork ? -1 : 1);
				pastFork = true;
				speed = Global.autoSpeed / 2;
			}
			if (Global.timer.get() > scoreTime - 3) {
				speed = Global.autoSpeed / 2;
			}
			if (pastFork && Global.timer.get() > scoreTime) {
				speed = 0;
			}
		}
	}

	public void atTarget(SpecialSystems systems, DriverStation station) {
		if (Global.timer.get() > scoreTime + 3) {
			base = 0;
			wheelAngle = Math.PI;
			speed = 1;
			clawOut = true;
		}
		if (Global.timer.get() > scoreTime + 5) {
			speed = 0;
			clawOut = false;
			systems.autoPosition(3, station);
		}
	}
}
