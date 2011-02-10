package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.*;

public class Global {

	static double armMotorThreshold = 0.1;
	/******************************TO BE TESTED********************************/
	// Autonomous
	static double servoForward = 0;
	static double servoBack = 1;
	static double straightSpeed = 0.5;
	static double forkSpeed = 0.5;
	static double forkTime = 5;
	static double steeringCorrection = Math.PI / 6;
	static double forkAngle = 0.561265607;
	static double forkScoreTime = 5,
			straightScoreTime = 10;
	static double autoSpeed = 1;
	// Swivel
	static double swivelSpeed = 0.125;
	static double kPSwerve = 1. / Math.PI,
			kISwerve = 0.,
			kDSwerve = -0. / Math.PI;
	// Camera
	static double minArea = 10;
	static double errorRange = .3;
	static int brightness = 4;
	static int color = 50;
	static int compression = 0;
	static int minibotSteeringRatio = -1;
	static AxisCamera.ResolutionT resolution = AxisCamera.ResolutionT.k160x120;
	// Special Systems
	static int pickUpTime = 2;
	static int armEncoderThreshold = 5;
	static double clawMotorSpeed = .25;
	static double armMiddle = 675., armMaxIdleSpeed = .25;
	static double armMax = armMiddle + 160., armMin = armMiddle - 155.;
	static double safeThreshold = 200.;
	static int armLvlArray[] = {(int) armMin, (int) armMiddle - 130,
		(int) armMiddle - 50, (int) armMiddle, (int) armMiddle + 50,
		(int) armMiddle + 80, (int) armMiddle + 140, (int) armMax};
	static double kPArm = 2. / (armMax - armMin),
			kIArm = 0.,
			kDArm = -0. / (armMax - armMin);
	static double maxArmSpeed = .75;
	/***********************TESTED (But may be adjusted)***********************/
	// Motor ramping
	static double rampRate = 2;
	/**************************************************************************/
	// Raw Values
	static double swivelThreshold = Math.PI / 100;
	static double deadzone = 0.1;
	// Driver Station
	// Joystick Ports
	static int mainJoyPort = 1;
	static int secondJoyPort = 2;
	// Digial Input Channels
	static int goLeftChannel = 1;
	static int goRightChannel = 2;
	static int highLvlButton = 3;
	// Joystick Buttons
	// MainJoy
	static int trackButton = 11;
	// SecondJoy
	static int rollInButton = 4;
	static int rollOutButton = 5;
	static int rollUpButton = 3;
	static int rollDownButton = 2;
	static int upButton = 6;
	static int downButton = 7;
	static int manualSwitchButton = 9;
	static int deployButton = 10;
	static int servoButton = 11;
	// cRIO
	// Slots
	static int analogInSlot = 2;
	static int digitalSidecarSlot = 4;
	static int digitalOut = 8;
	// PWM Channels
	static int mainMotorChannel = 1;
	static int frontSwivelMotorChannel = 2;
	static int backSwivelMotorChannel = 3;
	static int armMotorChannel = 4;
	static int topMotorChannel = 5;
	static int bottomMotorChannel = 6;
	static int servoChannel = 7;
	// Analog Input Channels
	static int frontEncoderChannel = 1;
	static int backEncoderChannel = 2;
	static int armEncoderChannel = 3;
	// Digial Input Channels
	static int leftSensorChannel = 1;
	static int middleSensorChannel = 2;
	static int rightSensorChannel = 3;
	static int backRightSensorChannel = 4;
	static int encoderCalibratorChannel = 5;
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

	static double mod(double input, double modulus) {
		double output = input;
		while (output < 0) {
			output = output + modulus;
		}
		while (output >= modulus) {
			output = output - modulus;
		}
		return output;
	}

	static boolean debounce(boolean currentState, boolean lastState) {
		if (currentState != lastState) {
			return currentState;
		} else {
			return false;
		}
	}
}
