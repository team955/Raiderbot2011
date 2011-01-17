package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class AutoBot {

	Timer timer;
	DigitalInput frontLeft, frontRight, backLeft, backRight;
	DriverStation driverStation;
	double timedSpeed[];
	boolean takeLeftFork;
	boolean takeRightFork;
	double forkAngle;
	double frontWheelAngle;
	double backWheelAngle;
	double frontWheelSpeed;
	double backWheelSpeed;
	boolean usingSpeedFront, usingSpeedBack;
	boolean atFork;
	double angleAdjustment;

	public AutoBot(int frontLeftChannel, int frontRightChannel,
			int backLeftChannel, int backRightChannel) {
		timer = new Timer();
		timer.start();
		timer.reset();
		frontLeft = new DigitalInput(frontLeftChannel);
		frontRight = new DigitalInput(frontRightChannel);
		backLeft = new DigitalInput(backLeftChannel);
		backRight = new DigitalInput(backRightChannel);
		atFork = false;
	}

//	public double followLine(boolean left, boolean right) {
//		int binary = (left ? 1 : 0) + (right ? 1 : 0);
//
//		if (binary == 1) {
//			return Vars.steeringCorrection * (left ? -1 : 1);
//		} else {
//			if (timer.get() >= Vars.forkTime) {
//				atFork = true;
//			}
//			return 0;
//		}
//	}
//	NOTE: If at the fork due to the light switches' distance from each other and
//		the robot's orientation both switches will be triggered, this must be
//		accounted for (by simply checking for atFork in the function above)
	public void autoDrive(SwerveDrive driveSystem, SpecialSystems systems,
			MyCamera camera) {
		driverStation = DriverStation.getInstance();
		takeLeftFork = driverStation.getDigitalIn(Vars.goLeftChannel);
		takeRightFork = driverStation.getDigitalIn(Vars.goRightChannel);
		timedSpeed = (takeLeftFork || takeRightFork) ? Vars.forkProfile
				: Vars.straightProfile;
		forkAngle = ((takeLeftFork ? -Vars.forkAngle : 0)
				+ (takeRightFork ? Vars.forkAngle : 0)) * (atFork ? 1 : 0);

//		frontWheelAngle = followLine(frontLeft.get(), frontRight.get())
//				+ forkAngle;
//		if (!atFork) {
//			backWheelAngle = followLine(backLeft.get(), backRight.get());
//		} else {
//			backWheelAngle = frontWheelAngle;
//		}
//
//		driveSystem.setFrontSwivelAngle(frontWheelAngle);
//		driveSystem.setBackSwivelAngle(backWheelAngle);
		driveSystem.setMainMotor(timedSpeed[(int) timer.get()]);

		if (frontLeft.get() && !frontRight.get()) {
			usingSpeedFront = true;
			frontWheelSpeed = -Vars.swivelAutoSpeed;
		}
		if (!frontLeft.get() && frontRight.get()) {
			usingSpeedFront = true;
			frontWheelAngle = Vars.swivelAutoSpeed;
		}
		if (!frontLeft.get() && !frontRight.get()) {
			//add saftey?
			usingSpeedFront = false;
			frontWheelAngle = 0;
		}
		if (frontLeft.get() && frontRight.get()) {
			usingSpeedFront = false;
			frontWheelAngle = forkAngle;
		}

		if (backLeft.get() && !backRight.get()) {
			usingSpeedBack = true;
			backWheelAngle = -Vars.swivelAutoSpeed;
		}
		if (!backLeft.get() && backRight.get()) {
			usingSpeedBack = true;
			backWheelAngle = Vars.swivelAutoSpeed;
		}
		if (!backLeft.get() && !backRight.get()) {
			//add saftey?
			usingSpeedBack = false;
			backWheelAngle = 0;
		}
		if (backLeft.get() && backRight.get()) {
			usingSpeedBack = false;
			backWheelAngle = forkAngle;
		}

		if (usingSpeedFront) {
			driveSystem.setFrontSwivelSpeed(frontWheelSpeed);
		} else {
			driveSystem.setFrontSwivelAngle(frontWheelAngle);
		}
		if (usingSpeedBack) {
			driveSystem.setBackSwivelSpeed(backWheelSpeed);
		} else {
			driveSystem.setBackSwivelAngle(backWheelAngle);
		}


		/// END EFFECTOR CODE HERE ///


	}
}
