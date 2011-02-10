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
		camera.writeBrightness(Global.brightness);
//		camera.writeColorLevel(Vars.color);
		camera.writeCompression(Global.compression);
		camera.writeResolution(Global.resolution);
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
}
