package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class Main extends SimpleRobot{
	MyCamera RaiderCamera;
	SwerveDrive RaiderSwerve;
	SpecialSystems RaiderSystems;
	AutoBot AutoRaider;
	Joystick mainJoy, secondJoy;
	DriverStation DriverStation;
//	ArcadeDrive RaiderDrive;

	public void robotInit(){
		Vars.timer.start();
		RaiderCamera = new MyCamera();
		RaiderSystems = new SpecialSystems(Vars.digitalSidecarSlot,
				Vars.armMotorChannel, Vars.rampRate, Vars.solenoidSlot, 1, 2);
		AutoRaider = new AutoBot(Vars.frontLeftSensorChannel,
				Vars.frontRightSensorChannel, Vars.backLeftSensorChannel,
				Vars.backRightSensorChannel);
		RaiderSwerve = new SwerveDrive(Vars.digitalSidecarSlot,
				Vars.mainMotorChannel, Vars.frontSwivelMotorChannel,
				Vars.backSwivelMotorChannel, Vars.rampRate, Vars.analogInSlot,
				Vars.frontEncoderChannel, Vars.backEncoderChannel,
				Vars.kPSwerve, Vars.kISwerve, Vars.kDSwerve);
		mainJoy = new Joystick(Vars.mainJoyPort);
		secondJoy = new Joystick(Vars.secondJoyPort);
//		RaiderDrive = new ArcadeDrive(4, 4, 5, Vars.rampRate);
		System.out.println("Robot Initialized");
	}

	public void disabled(){
		System.out.println("Robot Disabled");
	}

	public void autonomous(){
		Vars.timer.reset();
		getWatchdog().setEnabled(true);
		getWatchdog().setExpiration(1);
		while (isAutonomous()){
			getWatchdog().feed();
			AutoRaider.autoDrive(RaiderSwerve, RaiderSystems, DriverStation);
		}
	}

	public void operatorControl(){
		Vars.timer.reset();
		getWatchdog().setEnabled(true);
		getWatchdog().setExpiration(1);
		while (isOperatorControl()){
			getWatchdog().feed();
			RaiderSystems.oldArm(mainJoy);
			RaiderSwerve.drive(mainJoy, RaiderCamera);
//			System.out.println("X: " + RaiderCamera.getLightData());
//			RaiderDrive.drive(mainJoy);
		}
	}
}