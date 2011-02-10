package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import com.sun.squawk.util.MathUtils;

public class SpecialSystems {

	RampingJaguar armMotor, topMotor, bottomMotor;
	MyEncoder armEncoder;
	MyPIDController pidController;
	MyPneumatic pneumatic;
	Servo camera;
	DigitalInput encoderCalibrator;
	boolean isManual = true;
	boolean servoPosition = false;
	boolean armUpLS = false, armDownLS = false;
	double armMotorSpeed, topMotorSpeed, bottomMotorSpeed;
		double armEncoderValue;
	double joyY;

	public SpecialSystems(int digitalSidecarSlot, int armMotorChannel,
			int topMotorChannel, int bottomMotorChannel, double rampRate,
			int analogInSlot, int armEncoderChannel, int servoChannel) {
		armMotor = new RampingJaguar(digitalSidecarSlot, armMotorChannel,
				rampRate);
		topMotor = new RampingJaguar(digitalSidecarSlot, topMotorChannel,
				rampRate);
		bottomMotor = new RampingJaguar(digitalSidecarSlot, bottomMotorChannel,
				rampRate);
		armEncoder = new MyEncoder(analogInSlot, armEncoderChannel);
		camera = new Servo(digitalSidecarSlot, servoChannel);
		pidController = new MyPIDController(Global.kPArm, Global.kIArm, Global.kDArm);
		encoderCalibrator = new DigitalInput(Global.digitalSidecarSlot,
				Global.encoderCalibratorChannel);
	}

	public void runArm(MyJoystick sticky, DriverStation driverStation) {
		armEncoderValue = armEncoder.get();
		if(encoderCalibrator.get()){
			Global.armMiddle =
					armEncoderValue + Global.armMiddle - Global.armMin;
		}
		joyY = -sticky.getY() * Math.abs(sticky.getY());
		if (sticky.debounce(Global.manualSwitchButton)) {
			isManual = !isManual;
			armMotorSpeed = 0;
			topMotorSpeed = 0;
			bottomMotorSpeed = 0;
		}
		if (isManual) {
			manualArm();
			if (sticky.debounce(Global.servoButton)) {
				servoPosition = !servoPosition;
			}
		} else {
			setArmPosition((int) ((-sticky.getZ() + 1) / 2 * 7), driverStation);
			if (Global.timer.get() > 105) {
				servoPosition = true;
			}
		}
		runClaw(sticky);
		deployMinibot(sticky, driverStation);
		armMotorSpeed = Global.limit(armMotorSpeed + idleSpeed(), 0, .7); // Gravity will be enough to bring the arm down
		System.out.println(isManual + " " + armEncoderValue + " " + armMotorSpeed);
		armMotor.set(-armMotorSpeed);
		topMotor.set(topMotorSpeed);
		bottomMotor.set(bottomMotorSpeed);
		camera.set(servoPosition ? Global.servoBack : Global.servoForward);

	}

	public void manualArm() {
		if (joyY < 0 && armEncoderValue < 520) { // Can't run into ground
			joyY = 0;
		}
		armMotorSpeed = (joyY * Global.maxArmSpeed);
		if (armEncoderValue >= Global.armMax - Global.safeThreshold // Ramp down joystick when close to top
				&& joyY > 0) {
			armMotorSpeed *= MathUtils.pow((Global.armMax - armEncoderValue)
					/ Global.safeThreshold, .5) / 1.5; /// TODO: TUNE
		}
	}

	public void setArmPosition(int position, DriverStation driverStation) {
		for (int i = 1; i <= 8; i++) {
			driverStation.setDigitalOut(i, i == (8 - position) ? true : false);
		}
		armMotorSpeed = pidController.out(Global.armLvlArray[position],
				armEncoderValue);
	}

	private double idleSpeed() {
		double idleSpeed = Global.armMaxIdleSpeed
				* Math.cos((armEncoderValue - Global.armMiddle) * Math.PI / 512);
		if (armEncoderValue < Global.armMin + 10){
			idleSpeed = 0;
		}
		return idleSpeed;
	}

	private void runClaw(Joystick sticky) {
		topMotorSpeed = Global.clawMotorSpeed
				* ((sticky.getRawButton(Global.rollUpButton) ? 1 : 0)
				+ (sticky.getRawButton(Global.rollDownButton) ? -1 : 0))
				+ (sticky.getRawButton(Global.rollInButton) ? 1 : 0)
				+ (sticky.getRawButton(Global.rollOutButton) ? -1 : 0);
		bottomMotorSpeed = Global.clawMotorSpeed
				* ((sticky.getRawButton(Global.rollUpButton) ? 1 : 0)
				+ (sticky.getRawButton(Global.rollDownButton) ? -1 : 0))
				+ (sticky.getRawButton(Global.rollInButton) ? -1 : 0)
				+ (sticky.getRawButton(Global.rollOutButton) ? 1 : 0);
	}

	private void deployMinibot(Joystick sticky, DriverStation driverStation) {
		if ((Global.timer.get() > 110 || driverStation.getDigitalIn(8))
				&& sticky.getRawButton(Global.deployButton)) {
			pneumatic.flip();
		}
	}

	public void autoPosition(int position, DriverStation driverStation) {
		setArmPosition(position, driverStation);
		armMotor.set(armMotorSpeed);
	}

	public void clawOut(boolean run) {
		topMotor.set(run ? -Global.clawMotorSpeed : 0);
		bottomMotor.set(run ? Global.clawMotorSpeed : 0);
	}
}
