package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.*;
import edu.wpi.first.wpilibj.image.*;

public class MyCamera {

	AxisCamera camera;
	ColorImage image = null;
	BinaryImage binaryImage;
	ParticleAnalysisReport[] report;
	ParticleAnalysisReport[] pegReport;
	ParticleAnalysisReport closestPeg;
	int pegs = 0;
	int nonEllipseParticles = 0;
	boolean pegsOnScreen;

	public void feedImage() {
		camera.getInstance().writeCompression(Vars.compression);
		camera.getInstance().writeResolution(Vars.resolution);
		camera.getInstance().writeBrightness(Vars.brightness);
		camera.getInstance();
		DriverStationLCD.getInstance().updateLCD();
	}

	private void getPegData() {
		try {
			image = camera.getImage();
//			 HSL - Hue, Saturation, Luminance. HSI, HSV, and RBG also options.
			binaryImage = image.thresholdHSL(68, 80, 0, 10, 220, 255);
			report = binaryImage.getOrderedParticleAnalysisReports();
			for (int i = 0; i < binaryImage.getNumberParticles(); i++) {
				double actualArea = report[i].particleArea;
				double ellipseArea = Math.PI / 4 * report[i].imageHeight
						* report[i].imageWidth;
				if ((actualArea > Vars.minArea)
						&& (actualArea < ellipseArea * (1 + Vars.errorRange))
						&& (actualArea > ellipseArea * (1 - Vars.errorRange))) {
					pegReport[i - nonEllipseParticles] = report[i];
					pegs++;
				} else {
					nonEllipseParticles++;
				}
			}
			if (pegReport[0] != null) {
				pegsOnScreen = true;
				closestPeg = pegReport[0];
				for (int i = 1; i < pegs; i++) {
					if (pegReport[i].particleArea
							> pegReport[i - 1].particleArea) {
						closestPeg = pegReport[i];
					}
				}
			} else {
				pegsOnScreen = false;
			}
		} catch (NIVisionException exception) {
			//System.out.println("NIVision Failure: " + exception);
			exception.printStackTrace();
		} catch (AxisCameraException exception) {
			//System.out.println("AxisCamrea Failure: " + exception);
			exception.printStackTrace();
		} finally {
			try {
				if (image != null) {
					image.free();
				}
			} catch (NIVisionException exception) {
			}
		}
	}

	public double getX() {
		getPegData();
		return pegsOnScreen ? closestPeg.center_mass_x_normalized : 0;
	}

	public double getY() {
		getPegData();
		return pegsOnScreen ? closestPeg.center_mass_y_normalized : 0;
	}

	public double getPercentCoverage() {
		getPegData();
		return pegsOnScreen ? closestPeg.particleToImagePercent : 0;
	}
}
