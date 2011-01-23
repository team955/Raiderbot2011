package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.camera.*;

public class Vars {

	/******************************TO BE TESTED********************************/
	// Motor PIDController values
	static double kPMotor = .5, kIMotor = 15, kDMotor = 0;
	// Autonomous
	static double forkTime = 3;
	static double forkProfile[] = {.5, .5, .3, .5, .5, 0, 0, 0, 0};
	static double straightProfile[] = {.5, .5, .5, .5, 0, 0, 0, 0};
	static double steeringCorrection = Math.PI / 6;
	static double forkAngle = Math.PI / 6;
	// Swivel
	static double kPSwerve = .5, kISwerve = 0, kDSwerve = 0;
	static double swivelMotorMaxSpeed = .5;
	static double swivelAutoSpeed = 0.4;
	// Camera
	static double minArea = 10;
	static double errorRange = .05;
	/***********************TESTED (But may be adjusted)***********************/
	/**************************************************************************/
	static double swivelThreshold = Math.PI / 127;
	static double deadzone = 0.05;
	// OI Ports
	static int mainJoyPort = 1;
	// OI Digial Input Channels
	static int goLeftChannel = 1;
	static int goRightChannel = 2;
	static int armMotorChannel = 3;
    static int topMotorChannel = 4;
    static int bottomMotorChannel = 5;
    static int topSwitchChannel = 6;
    static int bottomSwitchChannel = 7;
    static int armEncoderChannel = 8;
	// Joystick Buttons
	static int clampButton = 1;
	static int liftButton = 2;
	static int compressorButton = 12;
	static int rollInButton = 1;
    static int rollOutButton = 2;
    static int rollUpButton = 3;
    static int rollDownButton = 4;
    static int armUpButton = 5;
    static int armDownButton = 6;
    static int pickUpZeroButton = 7;
    static int pickUpFeedButton = 8;
	// cRio Slots
	static int solenoidSlot = 8;
	static int digitalSidecarSlot = 4;
	static int analogInSlot = 2;
	static int armMotorSlot = 3;
    static int topMotorSlot = 1;
    static int bottomMotorSlot = 5;
    static int topSwitchSlot = 6;
    static int bottomSwitchSlot = 7;
	// Analog IO Channels
	static int mainMotorChannel = 1;
	static int frontSwivelMotorChannel = 2;
	static int backSwivelMotorChannel = 3;
	// Digital IO Channels
	static int frontEncoderChannel = 1;
	static int backEncoderChannel = 2;
	// Solenoid Channels
	static int clampChannel1 = 1;
	static int clampChannel2 = 2;
	static int lifterChannel1 = 3;
	static int lifterChannel2 = 4;
	static int compressorChannel = 5;
	// cRIO Digial Input Channels
	static int frontLeftSensorChannel = 1;
	static int frontRightSensorChannel = 2;
	static int backLeftSensorChannel = 3;
	static int backRightSensorChannel = 3;
	  // Times
    static int pickUpTime = 2;
	// Camera
	static AxisCamera.ResolutionT resolution = AxisCamera.ResolutionT.k160x120;
	static int compression = 30;
	static int brightness = 40;

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
