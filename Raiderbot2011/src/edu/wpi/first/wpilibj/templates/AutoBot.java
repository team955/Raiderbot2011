package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class AutoBot {

	DigitalInput frontLeft, frontRight, backLeft, backRight;
	boolean takeLeftFork, takeRightFork, atFork;
	double frontWheelAngle, backWheelAngle, correctionAngle;
	double speed;
	boolean init;
	double timerValue, deployTime, atForkTime = 0;
	int armPosition;

	public AutoBot(int frontLeftChannel, int frontRightChannel,
			int backLeftChannel, int backRightChannel) {
		frontLeft = new DigitalInput(frontLeftChannel);
		frontRight = new DigitalInput(frontRightChannel);
		backLeft = new DigitalInput(backLeftChannel);
		backRight = new DigitalInput(backRightChannel);
		atFork = false;
	}

	public double followLine(boolean left, boolean right) {
		int binary = (left ? 1 : 0) + (right ? 1 : 0);

		if (binary == 1) {
			return Vars.steeringCorrection * (left ? -1 : 1);
		} else {
			if (timerValue >= Vars.forkTime && binary == 2) {
				if (!atFork) {
					atForkTime = Vars.timer.get();
				}
				atFork = true;
			}
			return 0;
		}
	}

	public void autoDrive(SwerveDrive driveSystem, SpecialSystems systems,
			DriverStation driverStation) {
		if (!init) {
			driverStation = DriverStation.getInstance();
			takeLeftFork = driverStation.getDigitalIn(Vars.goLeftChannel);
			takeRightFork = driverStation.getDigitalIn(Vars.goRightChannel);
			deployTime = (takeLeftFork || takeRightFork ? Vars.forkDeployTime : Vars.straightDeployTime);
			if (takeLeftFork && takeRightFork) {
				takeLeftFork = false;
			}
			init = true;
		}
		timerValue = Vars.timer.get();
		correctionAngle = ((takeLeftFork ? -Vars.forkAngle : 0)
				+ (takeRightFork ? Vars.forkAngle : 0)) * (atFork ? 1 : 0);

		frontWheelAngle = followLine(frontLeft.get(), frontRight.get())
				+ correctionAngle;
		if (!atFork) {
			backWheelAngle = followLine(backLeft.get(), backRight.get());
		} else {
			backWheelAngle = frontWheelAngle;
		}
		speed = Vars.autoSpeed;
		armPosition = Vars.armLvlArray[6 + (!takeLeftFork && !takeRightFork ? 1 : 0)];
		systems.setArmPosition(armPosition, driverStation);

		if (timerValue > deployTime - 2) {
			speed = speed / 4;
		}
		if (timerValue > deployTime) {
			systems.clawOut(true);

		}
		if (timerValue > deployTime + 3) {
			frontWheelAngle = 0;
			backWheelAngle = 0;
			speed = -1;
		}
		if (timerValue > deployTime + 5) {
			frontWheelAngle = Math.PI / 2;
			backWheelAngle = -Math.PI / 2;
			systems.clawOut(false);
		}
		if (timerValue > deployTime + 7) {
			frontWheelAngle = 0;
			backWheelAngle = 0;
			speed = 0;
			systems.autoPosition(3, driverStation);
		}

		driveSystem.setFrontSwivelAngle(frontWheelAngle);
		driveSystem.setBackSwivelAngle(backWheelAngle);
		driveSystem.setMainMotor(speed);
	}
}
