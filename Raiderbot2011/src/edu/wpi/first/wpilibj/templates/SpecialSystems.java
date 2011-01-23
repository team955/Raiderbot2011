package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class SpecialSystems {

	RampingJaguar armMotor;
	RampingJaguar topMotor;
	RampingJaguar bottomMotor;
	AnalogChannel armEncoder;
	MyPIDController pidController;
	MyPneumatic pneumatic;
	boolean isManual = false, manualSwitchButtonLS = false;
	boolean armUpLS = false, armDownLS = false;
	int currentHeight = 0;
	double armMotorSpeed, topMotorSpeed, bottomMotorSpeed;

	public SpecialSystems(int motorSlot, int armMotorChannel,
			int topMotorChannel, int bottomMotorChannel, int rampRate,
			int switchSlot, int topSwitchChannel, int bottomSwitchChannel,
			int encoderSlot, int armEncoderChannel) {
		armMotor = new RampingJaguar(motorSlot, armMotorChannel, rampRate);
		topMotor = new RampingJaguar(motorSlot, topMotorChannel, rampRate);
		bottomMotor = new RampingJaguar(motorSlot, bottomMotorChannel,
				rampRate);
		armEncoder = new AnalogChannel(encoderSlot, armEncoderChannel);
		pidController = new MyPIDController(Vars.kPArm, Vars.kIArm, Vars.kDArm);
	}

	public void manual(Joystick sticky) {
		if (sticky.getRawButton(Vars.upButton)) {
			armMotorSpeed = Vars.armMotorSpeed;
			if (armEncoder.getValue() >= Vars.armMax - Vars.safeThreshold) {
				armMotorSpeed *= (Vars.armMax - armEncoder.getValue())
						/ Vars.safeThreshold;
			}
		} else if (sticky.getRawButton(Vars.downButton)) {
			armMotorSpeed = -Vars.armMotorSpeed;
			if (armEncoder.getValue() <= Vars.armMin + Vars.safeThreshold) {
				armMotorSpeed *= (armEncoder.getValue() - Vars.armMin)
						/ Vars.safeThreshold;
			}
		} else {
			armMotorSpeed = 0;
		}
	}

	private void getPosition(Joystick sticky, DriverStation driverStation) {
		if (Vars.debounce(sticky.getRawButton(Vars.upButton), armUpLS)) {
			currentHeight++;
		}
		if (Vars.debounce(sticky.getRawButton(Vars.downButton), armDownLS)) {
			currentHeight--;
		}
		currentHeight = (int) Vars.limit((double) currentHeight, 0, 7);
		setArmPosition(currentHeight, driverStation);
	}

	private void runClaw(Joystick sticky) {
		if (sticky.getRawButton(Vars.rollInButton)) {
			topMotorSpeed = Vars.clawMotorSpeed;
			bottomMotorSpeed = -Vars.clawMotorSpeed;

		} else if (sticky.getRawButton(Vars.rollOutButton)) {
			topMotorSpeed = -Vars.clawMotorSpeed;
			bottomMotorSpeed = Vars.clawMotorSpeed;

		} else if (sticky.getRawButton(Vars.rollUpButton)) {
			topMotorSpeed = Vars.clawMotorSpeed;
			bottomMotorSpeed = Vars.clawMotorSpeed;

		} else if (sticky.getRawButton(Vars.rollDownButton)) {
			topMotorSpeed = -Vars.clawMotorSpeed;
			bottomMotorSpeed = -Vars.clawMotorSpeed;
		} else {
			topMotorSpeed = 0;
			bottomMotorSpeed = 0;
		}
	}

	private void deploy(Joystick sticky, DriverStation driverStation){
		if((Vars.timer.get() > 110 || driverStation.getDigitalIn(8))
				&& sticky.getRawButton(Vars.deployButton)){
			pneumatic.set(true);
		}
	}

	public void runArm(Joystick sticky, DriverStation driverStation) {
		if (Vars.debounce(sticky.getRawButton(Vars.manualSwitchButton),
				manualSwitchButtonLS)) {
			manual(sticky);
		} else {
			getPosition(sticky, driverStation);
		}
		runClaw(sticky);
		deploy(sticky, driverStation);
		armMotor.set(armMotorSpeed);
		topMotor.set(topMotorSpeed);
		bottomMotor.set(bottomMotorSpeed);
	}

	public void setArmPosition(int position, DriverStation station) {
		currentHeight = position;
		armMotorSpeed = pidController.dOut(Vars.armLvlArray[position],
				armEncoder.getValue());
		for (int i = 1; i <= 8; i++) {
			station.setDigitalOut(i, i == (8 - position) ? true : false);
		}
	}

	public void autoPosition(int position, DriverStation driverStation) {
		setArmPosition(position, driverStation);
		armMotor.set(armMotorSpeed);
	}

	public void clawOut(boolean trueorfalse) {
		if (trueorfalse) {
			topMotorSpeed = -Vars.clawMotorSpeed;
			bottomMotorSpeed = Vars.clawMotorSpeed;
		} else {
			topMotorSpeed = 0;
			bottomMotorSpeed = 0;
		}
		topMotor.set(topMotorSpeed);
		bottomMotor.set(bottomMotorSpeed);
	}




	/**************************************************************************/
	boolean pneumaticButtonLS;

	public SpecialSystems(int digitalSidecarSlot, int armMotorChannel,
			double rampRate, int solenoidSlot, int solinoid1Channel,
			int solinoid2Channel) {
		armMotor = new RampingJaguar(digitalSidecarSlot, armMotorChannel,
				rampRate);
		pneumatic = new MyPneumatic(solenoidSlot, solinoid1Channel,
				solinoid2Channel);
	}

	public void oldArm(Joystick mainJoy) {
		if (mainJoy.getRawButton(Vars.upButton)) {
			armMotor.set(Vars.armMotorSpeed);
		} else {
			armMotor.set(0);
		}
		if (Vars.debounce(mainJoy.getRawButton(Vars.downButton),
				pneumaticButtonLS)) {
			pneumatic.flip();
		}
		pneumaticButtonLS = mainJoy.getRawButton(Vars.downButton);
	}
}
