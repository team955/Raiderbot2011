package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.*;

public class Vars {

	/******************************TO BE TESTED********************************/
	// Autonomous
	static double straightSpeed = 0.5;
	static double forkSpeed = 0.5;
	static double forkTime = 5;
	static double steeringCorrection = Math.PI / 6;
	static double forkAngle = Math.PI / 6;
	static double forkDeployTime = 5,
			straightDeployTime = 2;
	static double autoSpeed = 1;
	// Swivel
	static double kUSwerve = 0.3;
	static double kPSwerve = 0.6 * kUSwerve,	// Classic: .6, SO: .33, NO: .2
			kISwerve = 2 * kUSwerve,
			kDSwerve = kUSwerve / 8;			// Classic: 8, SO/NO: 3
	static double swivelSpeed = 0.5;
	// Camera
	static double minArea = 10;
	static double errorRange = .3;
	static int brightness = 4;
	static int color = 50;
	static int compression = 0;
	static int minibotSteeringRatio = -1;
	static AxisCamera.ResolutionT resolution = AxisCamera.ResolutionT.k160x120;
	// Special Systems
	static double armMotorSpeed = 1;
	static int pickUpTime = 2;
	static int armEncoderThreshold = 5;
	static int armLvlArray[] = {0, 20, 25, 30, 40, 45, 60, 65};
	static double clawMotorSpeed = 1;
	static double kUArm = 0.3;
	static double armMax = 1000, armMin = 100, safeThreshold = 50;
	static double kPArm = 0.6 * kUArm,	// Classic: .6, SO: .33, NO: .2
			kIArm = 2 * kUArm,
			kDArm = kUArm / 8;			// Classic: 8, SO/NO: 3
	/***********************TESTED (But may be adjusted)***********************/
	// Motor ramping
	static double rampRate = 2;
	/**************************************************************************/
	static double swivelThreshold = Math.PI / 100;
	static double deadzone = 0.05;
	// OI Ports
	static int mainJoyPort = 1;
	static int secondJoyPort = 2;
	// OI Digial Input Channels
	static int goLeftChannel = 1;
	static int goRightChannel = 2;
	static int highLvlButton = 3;
	// Joystick Buttons
	static int rollInButton = 1;
	static int rollOutButton = 2;
	static int rollUpButton = 3;
	static int rollDownButton = 4;
	static int upButton = 5;
	static int downButton = 6;
	static int pickUpZeroButton = 7;
	static int pickUpFeedButton = 8;
	static int manualSwitchButton = 9;
	static int lvlOne = 11;
	static int lvlTwo = 12;
	static int lvlThree = 13;
	static int trackButton = 11;
	static int deployButton = 20;
	// cRio Slots
	static int analogInSlot = 2;
	static int digitalSidecarSlot = 4;
	static int solenoidSlot = 8;
	// PWM Channels
	static int mainMotorChannel = 1;
	static int frontSwivelMotorChannel = 2;
	static int backSwivelMotorChannel = 3;
	static int armMotorChannel = 4;
	static int topMotorChannel = 5;
	static int bottomMotorChannel = 6;
	// Analog IO Channels
	static int frontEncoderChannel = 1;
	static int backEncoderChannel = 2;
	static int armEncoderChannel = 3;
	// cRIO Digial Input Channels
	static int frontLeftSensorChannel = 1;
	static int frontRightSensorChannel = 2;
	static int backLeftSensorChannel = 3;
	static int backRightSensorChannel = 4;
	// Timer
	static Timer timer = new Timer();

	// Functions
	static double limit(double input, double lower, double upper) {
		if (input > upper) {
			return upper;
		}
		if (input < lower) {
			return lower;
		} else {
			return input;
		}
	}

	static boolean debounce(boolean currentState, boolean lastState) {
		if (currentState != lastState) {
			return currentState;
		} else {
			return false;
		}
	}
}
