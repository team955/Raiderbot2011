package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.camera.*;
import edu.wpi.first.wpilibj.image.*;

public class MyCamera {

	AxisCamera camera;
	ColorImage image = null;
	BinaryImage binaryImage = null;
	ParticleAnalysisReport[] report;

	public MyCamera() {
		camera = camera.getInstance();
		camera.writeBrightness(Vars.brightness);
//		camera.writeColorLevel(Vars.color);
		camera.writeCompression(Vars.compression);
		camera.writeResolution(Vars.resolution);
		camera.getInstance();
	}

	private void getLightData() {
		image = null;
		report = null;
		try {
			if (camera.freshImage()) {
				image = camera.getImage();
				binaryImage = image.thresholdRGB(0, 127, 100, 255, 0, 127);
				if (binaryImage.getNumberParticles() > 0) {
					report = binaryImage.getOrderedParticleAnalysisReports(1);
				}
			}
		} catch (NIVisionException exception) {
			exception.printStackTrace();
		} catch (AxisCameraException exception) {
			exception.printStackTrace();
		} finally {
			try {
				if (image != null) {
					image.free();
				}
				if (binaryImage != null) {
					binaryImage.free();
				}
			} catch (NIVisionException exception) {
			}
		}
	}

	public double getLightX() {
		getLightData();
		return report[0].center_mass_x_normalized;
	}
//	ParticleAnalysisReport closestPeg;
//	int pegs = 0;
//	double actualArea, ellipseArea, ratio;
//	boolean closestPegFound;
//
//	public void getPegData() {
//		image = null;
//		report = null;
//		pegs = 0;
//		closestPegFound = false;
//		try {
//			if (camera.freshImage()) {
//				image = camera.getImage();
////					HSL - Hue, Saturation, Luminance. HSI, HSV, and RBG also options.
//				binaryImage = image.thresholdHSL(0, 255, 0, 255, 200, 255);
//				if(binaryImage.getNumberParticles() > 0) {
//					report = binaryImage.getOrderedParticleAnalysisReports(10);
//					for (int i = 0; i < binaryImage.getNumberParticles() && i
//							< 10; i++) {
//						actualArea = report[i].particleArea;
//						ellipseArea = (Math.PI / 4) *
//								report[i].boundingRectHeight
//								* report[i].boundingRectWidth;
//						ratio = actualArea / ellipseArea;
//						if (actualArea > Vars.minArea
//								&& ratio < (1 + Vars.errorRange)
//								&& ratio > (1 - Vars.errorRange)) {
//							pegs++;
//							if (!closestPegFound) {
//								closestPeg = report[i];
//								closestPegFound = true;
//							}
//						}
//					}
//				}
//			}
//		} catch (NIVisionException exception) {
//			exception.printStackTrace();
//		} catch (AxisCameraException exception) {
//			exception.printStackTrace();
//		} finally {
//			try {
//				if (image != null) {
//					image.free();
//				}
//				if (binaryImage != null) {
//					binaryImage.free();
//				}
//			} catch (NIVisionException exception) {
//			}
//		}
//	}
//
//	public double getPegX() {
//		return (pegs > 0) ? closestPeg.center_mass_x_normalized : 0;
//	}
//
//	public double getPegY() {
//		return (pegs > 0) ? closestPeg.center_mass_y_normalized : 0;
//	}
//
//	public double getPegPercentCoverage() {
//		return (pegs > 0) ? closestPeg.particleToImagePercent : 0;
//	}
}
