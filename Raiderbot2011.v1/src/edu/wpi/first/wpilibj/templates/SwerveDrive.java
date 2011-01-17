//	TO CONSIDER: Running the main motor backwards is also an option. If I need
//		the robot to move backwards from its starting position, I could either:
//			A. Turn the modules 180 degrees and run the main motor forwards OR
//			B. Run the main motor backwards
//		Currently our code would do A, which much simpler and less likely to
//			fail, however we should consider B as an option, as it would make
//			drive much more efficient.
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
//import com.sun.squawk.util.MathUtils;

public class SwerveDrive {

	MySpeedController mainDriveMotor, frontSwivelMotor, backSwivelMotor;
	AnalogChannel frontEncoder, backEncoder;
	MyPIDController frontPID, backPID;
	double currentFrontAngle, currentBackAngle;
	double targetFrontAngle, targetBackAngle;
	double zFrontAngle, zBackAngle;
	double frontSwivelMotorSpeed, backSwivelMotorSpeed;
	double x, y, z, rXYZ;
	double joystickAngle;

	public SwerveDrive(int digitalSidecarSlot, int mainMotorChannel,
			int frontSwivelMotorChannel, int backSwivelMotorChannel,
			int analogInSlot, int frontEncoderChannel, int backEncoderChannel,
			double kPSwerve, double kISwerve, double kDSwerve) {
		mainDriveMotor = new MySpeedController(digitalSidecarSlot,
				mainMotorChannel);
		frontSwivelMotor = new MySpeedController(digitalSidecarSlot,
				frontSwivelMotorChannel);
		backSwivelMotor = new MySpeedController(digitalSidecarSlot,
				backSwivelMotorChannel);
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
//		angle1 = mod(angle1, 2 * Math.PI);
//		angle2 = mod(angle2, 2 * Math.PI);
//		if (Math.abs(angle2 - angle1) <= Math.PI) {
//			return angle1 + (angle2 - angle1) * ratio;
//		} else {
//			angle1 = mod(angle1 + Math.PI, 2 * Math.PI);
//			angle2 = mod(angle2 + Math.PI, 2 * Math.PI);
//			return mod((angle1 + (angle2 - angle1) * ratio) + Math.PI,
//					2 * Math.PI);
//		}
	}

	private double getSpeed(double currentAngle, double targetAngle,
			MyPIDController pid) {
		double motorSpeed;
		if (Math.abs(targetAngle - currentAngle) >= Math.PI) {
			targetAngle += (currentAngle > targetAngle ? 1 : -1)
					* 2 * Math.PI;
		}
		motorSpeed = Vars.limit(pid.dOut(targetAngle, currentAngle), -1, 1);
//		if (Math.abs(targetAngle - currentAngle) > Vars.swivelThreshold){
//			motorSpeed = MathUtils.log((normalize(Math.abs
//					(targetAngle - currentAngle), 2 * Math.PI) + 1)
//					/ MathUtils.log(Math.PI + 1))
//					* Vars.swivelMotorMaxSpeed;
//			if (Math.abs(targetAngle - currentAngle) <= Math.PI){
//				motorSpeed = -motorSpeed;
//			}
//		} else{
//			motorSpeed = 0;
//		}
		return motorSpeed;
	}

	public void drive(Joystick MainJoy) {
		// Current bearing of wheels (in radians); 10 bit encoders
		currentFrontAngle = frontEncoder.getValue() * Math.PI / 512;
		currentBackAngle = backEncoder.getValue() * Math.PI / 512;

		x = MainJoy.getX();
		y = -MainJoy.getY();
		z = MainJoy.getTwist();

		// Bearing of Joystick
		joystickAngle = MainJoy.getDirectionRadians();

		// Final motor velocity composed of three axes
		rXYZ = Math.sqrt(x * x + y * y + z * z);

		// In case of faulty calibration, set a joystick deadzone
		if (rXYZ > Vars.deadzone) {
			// Calculate target angles for front and back wheels
			if (z > 0) {
//				if (x + y < 0) {
//					zFrontAngle = -(x + y) * Math.PI / 4 + Math.PI / 2;
//					zBackAngle = -Math.PI / 2;
//				} else {
//					zFrontAngle = Math.PI / 2;
//					zBackAngle = (x + y) * Math.PI / 4 - Math.PI / 2;
//				}
				targetFrontAngle = findAngle(joystickAngle,
						(Math.PI / 2 + (x + y < 0
						? -(x + y) * Math.PI / 4 : 0)), z);
				targetBackAngle = findAngle(joystickAngle,
						(-Math.PI / 2 + (x + y > 0
						? (x + y) * Math.PI / 4 : 0)), z);
			} else {
//				if (-x + y < 0) {
//					zFrontAngle = (-x + y) * Math.PI / 4 - Math.PI / 2;
//					zBackAngle = Math.PI / 2;
//				} else {
//					zFrontAngle = -Math.PI / 2;
//					zBackAngle = -(-x + y) * Math.PI / 4 + Math.PI / 2;
//				}
				targetFrontAngle = findAngle(joystickAngle,
						(-Math.PI / 2 + (-x + y < 0
						? (-x + y) * Math.PI / 4 : 0)), -z);
				targetBackAngle = findAngle(joystickAngle,
						(Math.PI / 2 + (-x + y > 0
						? -(-x + y) * Math.PI / 4 : 0)), -z);
			}

//			targetFrontAngle = findAngle(joystickAngle, zFrontAngle, z);
//			targetBackAngle = findAngle(joystickAngle, zBackAngle, z);

			// Find swivel motor speeds
			frontSwivelMotorSpeed = getSpeed(currentFrontAngle,
					targetFrontAngle, frontPID);
			backSwivelMotorSpeed = getSpeed(currentBackAngle,
					targetBackAngle, backPID);
			//Set swivel motor speeds
			frontSwivelMotor.set(frontSwivelMotorSpeed);
			backSwivelMotor.set(backSwivelMotorSpeed);
			mainDriveMotor.set(Vars.limit(rXYZ, -1, 1));
		} // If joystick inside deadzone, set all motors to 0
		else {
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
		targetFrontAngle = angle;
		frontSwivelMotorSpeed = getSpeed(currentFrontAngle, targetFrontAngle,
				frontPID);
		frontSwivelMotor.set(frontSwivelMotorSpeed);
	}

	public void setBackSwivelAngle(double angle) {
		targetBackAngle = angle;
		backSwivelMotorSpeed = getSpeed(currentBackAngle, targetBackAngle,
				backPID);
		backSwivelMotor.set(backSwivelMotorSpeed);
	}

	public void setFrontSwivelSpeed(double speed) {
		frontSwivelMotor.set(speed);
	}

	public void setBackSwivelSpeed(double speed) {
		backSwivelMotor.set(speed);
	}
}
