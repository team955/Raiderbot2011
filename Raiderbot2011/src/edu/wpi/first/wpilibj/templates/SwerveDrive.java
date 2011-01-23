//	TO CONSIDER: Running the main motor backwards is also an option. If I need
//		the robot to move backwards from its starting position, I could either:
//			A. Turn the modules 180 degrees and run the main motor forwards OR
//			B. Run the main motor backwards
//		Currently our code would do A, which much simpler and less likely to
//			fail, however we should consider B as an option, as it would make
//			drive much more efficient.
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class SwerveDrive {

	RampingJaguar mainDriveMotor, frontSwivelMotor, backSwivelMotor;
	AnalogChannel frontEncoder, backEncoder;
	MyPIDController frontPID, backPID;
	double currentFrontAngle, currentBackAngle;
	double targetFrontAngle, targetBackAngle;
	double zFrontAngle, zBackAngle;
	double x, y, z, rXYZ;
	double joystickAngle;
	double lightX;

	public SwerveDrive(int digitalSidecarSlot, int mainMotorChannel,
			int frontSwivelMotorChannel, int backSwivelMotorChannel,
			double rampRate, int analogInSlot, int frontEncoderChannel,
			int backEncoderChannel, double kPSwerve, double kISwerve,
			double kDSwerve) {
		mainDriveMotor = new RampingJaguar(digitalSidecarSlot,
				mainMotorChannel, rampRate);
		frontSwivelMotor = new RampingJaguar(digitalSidecarSlot,
				frontSwivelMotorChannel, rampRate);
		backSwivelMotor = new RampingJaguar(digitalSidecarSlot,
				backSwivelMotorChannel, rampRate);
		frontEncoder = new AnalogChannel(analogInSlot, frontEncoderChannel);
		backEncoder = new AnalogChannel(analogInSlot, backEncoderChannel);
		frontPID = new MyPIDController(kPSwerve, kISwerve, kDSwerve);
		backPID = new MyPIDController(kPSwerve, kISwerve, kDSwerve);
	}

	private double mod(double input, double modulus) {
		double output = input;
		while (output < 0) {
			output = output + modulus;
		}
		while (output >= modulus) {
			output = output - modulus;
		}
		return output;
	}

	private double findAngle(double angle1, double angle2, double ratio) {
		double modifier = ((Math.abs(angle2 - angle1) <= Math.PI) ? 0
				: Math.PI);
		angle1 = mod(angle1 + modifier, 2 * Math.PI);
		angle2 = mod(angle2 + modifier, 2 * Math.PI);
		return mod((angle1 + (angle2 - angle1) * ratio) + modifier,
				2 * Math.PI);
	}

	private double getSpeed(double targetAngle, double currentAngle,
			MyPIDController pid) {
		double motorSpeed;
		targetAngle = mod(targetAngle, 2 * Math.PI);
		currentAngle = mod(currentAngle, 2 * Math.PI);
		if (Math.abs(targetAngle - currentAngle) >= Math.PI) {
			targetAngle += 2 * Math.PI * (currentAngle > targetAngle ? 1 : -1);
		}
		motorSpeed = Vars.limit(pid.dOut(targetAngle, currentAngle)
				* Vars.swivelSpeed, -1, 1);
//		/if (Math.abs(targetAngle - currentAngle) > Vars.swivelThreshold){
//		motorSpeed = MathUtils.log((mod(Math.abs
//		(targetAngle - currentAngle), 2 * Math.PI) + 1)
//		/ MathUtils.log(Math.PI + 1))
//		 * Vars.swivelMotorMaxSpeed;
//		if (Math.abs(targetAngle - currentAngle) <= Math.PI){
//		motorSpeed = -motorSpeed;
//		}
//		} else{
//		motorSpeed = 0;
//		}
		return motorSpeed;
	}

	public void drive(Joystick mainJoy, MyCamera RaiderCamera) {
		currentFrontAngle = frontEncoder.getValue() * Math.PI / 512;
		currentBackAngle = backEncoder.getValue() * Math.PI / 512;
		x = mainJoy.getX();
		y = -mainJoy.getY();
		z = mainJoy.getTwist();
		joystickAngle = mainJoy.getDirectionRadians();
		rXYZ = Math.sqrt(x * x + y * y + z * z);

		if (rXYZ > Vars.deadzone) {
			if (z > 0) {
				targetFrontAngle = findAngle(joystickAngle,
						(Math.PI / 2 + (x + y < 0
						? -(x + y) * Math.PI / 4 : 0)), z);
				targetBackAngle = findAngle(joystickAngle,
						(-Math.PI / 2 + (x + y > 0
						? (x + y) * Math.PI / 4 : 0)), z);
			} else {
				targetFrontAngle = findAngle(joystickAngle,
						(-Math.PI / 2 + (-x + y < 0
						? (-x + y) * Math.PI / 4 : 0)), -z);
				targetBackAngle = findAngle(joystickAngle,
						(Math.PI / 2 + (-x + y > 0
						? -(-x + y) * Math.PI / 4 : 0)), -z);
			}

			if (mainJoy.getRawButton(Vars.trackButton)) {
				lightX = RaiderCamera.getLightX();
				targetFrontAngle += lightX * Vars.minibotSteeringRatio
						* (y > 0 ? 1 : -1);
			}
			System.out.println("Front Ang: " + mod(targetFrontAngle
					* (180 / Math.PI), 2 * Math.PI));
			System.out.println("Back Ang: " + mod(targetBackAngle
					* (180 / Math.PI), 2 * Math.PI));
			
			frontSwivelMotor.set(getSpeed(targetFrontAngle, currentFrontAngle,
					frontPID));
			backSwivelMotor.set(getSpeed(targetBackAngle, currentBackAngle,
					backPID));
			mainDriveMotor.set(Vars.limit(rXYZ, -1, 1));
		} else {
			targetFrontAngle = 0;
			targetBackAngle = 0;
			mainDriveMotor.set(0);
			frontSwivelMotor.set(0);
			backSwivelMotor.set(0);
		}
	}

	public void setMainMotor(double speed) {
		mainDriveMotor.set(speed);
	}

	public void setFrontSwivelAngle(double angle) {
		frontSwivelMotor.set(getSpeed(angle, currentFrontAngle, frontPID));
	}

	public void setBackSwivelAngle(double angle) {
		backSwivelMotor.set(getSpeed(angle, currentBackAngle, backPID));
	}
}
