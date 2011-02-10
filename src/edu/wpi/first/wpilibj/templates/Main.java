package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class Main extends SimpleRobot{
	MyCamera RaiderCamera;
	SwerveDrive RaiderSwerve;
	SpecialSystems RaiderSystems;
	AutoBot AutoRaider;
	MyJoystick MainJoy, SecondJoy;
	DriverStation DriverStation;
	double timerZero;

	public void robotInit(){
		Global.timer.start();
		RaiderCamera = new MyCamera();
		RaiderSystems = new SpecialSystems(Global.digitalSidecarSlot, Global.armMotorChannel,
			Global.topMotorChannel, Global.bottomMotorChannel, Global.rampRate,
			Global.analogInSlot, Global.armEncoderChannel, Global.servoChannel);
		AutoRaider = new AutoBot(Global.leftSensorChannel,
				Global.middleSensorChannel, Global.rightSensorChannel);
		RaiderSwerve = new SwerveDrive(Global.digitalSidecarSlot,
				Global.mainMotorChannel, Global.frontSwivelMotorChannel,
				Global.backSwivelMotorChannel, Global.rampRate, Global.analogInSlot,
				Global.frontEncoderChannel, Global.backEncoderChannel,
				Global.kPSwerve, Global.kISwerve, Global.kDSwerve);
		MainJoy = new MyJoystick(Global.mainJoyPort);
		SecondJoy = new MyJoystick(Global.secondJoyPort);
		DriverStation = DriverStation.getInstance();
		System.out.println("Robot Initialized");
	}

	public void disabled(){
		System.out.println("Robot Disabled");
	}

	public void autonomous(){
		Global.timer.reset();
		getWatchdog().setEnabled(true);
		getWatchdog().setExpiration(1);
		System.out.println("Autonomous");
		while (isAutonomous()){
			getWatchdog().feed();
			AutoRaider.autoRun(RaiderSwerve, RaiderSystems, DriverStation);
		}
	}

	public void operatorControl(){
		Global.timer.reset();
		getWatchdog().setEnabled(true);
		getWatchdog().setExpiration(1);
		System.out.println("Operator Control");
		while (isOperatorControl()){
			getWatchdog().feed();
//			System.out.println(Vars.timer.get() - timerZero);
//			timerZero = Vars.timer.get();
			RaiderSystems.runArm(SecondJoy, DriverStation);
			RaiderSwerve.drive(MainJoy, RaiderCamera);
		}
	}
}