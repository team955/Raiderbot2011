package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class Main extends SimpleRobot{
//////////////////////////// FIGURE OUT TIMER
	NormalDrive RaiderDrive;
	Timer timer;
//	SwerveDrive RaiderSwerve;
//	SpecialSystems RaiderSystems;
//	AutoBot AutoRaider;
	MyCamera RaiderCamera;
	Joystick mainJoy = new Joystick(Vars.mainJoyPort);

	public void robotInit(){
		timer = new Timer();
		timer.start();
		timer.reset();
		MySpeedController.pidInit(Vars.kPMotor, Vars.kIMotor, Vars.kDMotor);
		RaiderDrive = new NormalDrive(4, 4, 5);
//		RaiderSwerve = new SwerveDrive(Vars.digitalSidecarSlot,
//				Vars.mainMotorChannel, Vars.frontSwivelMotorChannel,
//				Vars.backSwivelMotorChannel, Vars.analogInSlot,
//				Vars.frontEncoderChannel, Vars.backEncoderChannel,
//				Vars.kPSwerve, Vars.kISwerve, Vars.kDSwerve);
//		RaiderSystems = new SpecialSystems(Vars.solenoidSlot,
//				Vars.compressorChannel, Vars.clampChannel1, Vars.clampChannel2,
//				Vars.lifterChannel1, Vars.lifterChannel2);
//		AutoRaider = new AutoBot(Vars.frontLeftSensorChannel,
//				Vars.frontRightSensorChannel, Vars.backLeftSensorChannel,
//				Vars.backRightSensorChannel);
		System.out.println("Robot Initialized");
	}

	public void disabled(){
		System.out.println("Robot Disabled");
	}

	public void autonomous(){
		getWatchdog().setEnabled(true);
		getWatchdog().setExpiration(1);
		while (isEnabled() && isAutonomous()){
//			getWatchdog().feed();
//			AutoRaider.autoDrive(RaiderSwerve, RaiderSystems, RaiderCamera);
		}
	}

	public void operatorControl(){
		getWatchdog().setEnabled(true);
		getWatchdog().setExpiration(1);
		while (isEnabled() && isOperatorControl()){
			getWatchdog().feed();
			System.out.println(timer.get());
//			RaiderCamera.feedImage();
			RaiderDrive.drive(mainJoy);
//			RaiderSwerve.drive(mainJoy);
//			RaiderSystems.run(mainJoy);
			Timer.delay(0.05); // Camera update time
		}
	}
}
/**********************************TODO, ETC.***********************************
 * TO BE TESTED
 * SwerveDrive
 * Vars values
 * AutoBot use of four light sensors for line tracking
 * DriverStation inputs (requires classmate)
 * Camera live image feed (requires classmate)
 * Dead reckoning speeds in AutoBot
 * Camera peg recognition
 *
 * TO BE ADDED
 * Use MyPIDController to determine speed of swerve modules
 * SpecialSystems arm control and mini-bot deployment
 * AutoBot special system functions
 * Camera integration into autonomous and teleop
 * Line tracking integration into teleop
 * Replacement for dead reckoning speeds in autonomous?
 * DashBoard + EnhancedIO
 * StatusTest.java - print stuff for the robot such as current sensor values to
 *	  a text file which can be downloaded after the match
 *
 * DONE
 * MyPIDController
 * MySpeedController
 * MyPneumatic
 * NormalDrive
 ******************************************************************************/
