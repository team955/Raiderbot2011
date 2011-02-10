package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;

public class MyEncoder extends AnalogChannel {

	double value[] = {0, 0, 0, 0, 0};
	double dValue[] = {0, 0, 0, 0, 0};
	double dValueAverage;
	double encoderValueLS = 0;
	boolean usingExtrapolation = false;
	int loopCount;
	int count = 0, countLS = 4;

	public MyEncoder(int slot, int channel) {
		super(slot, channel);
		value[0] = getValue();
		encoderValueLS = getValue();
	}

	private double average(double array[]) {
		double sum = 0;
		for (int i = 0; i < 5; i++) {
			sum += array[i];
		}
		return sum / 5.0;
	}

	private void run() {
		value[count] = (double) getValue();
		if (Math.abs(value[count] - encoderValueLS) > 200 && !usingExtrapolation) {
			usingExtrapolation = true;
			loopCount = 0;
		}
		if (usingExtrapolation) {
			value[count] = value[countLS] + average(dValue);
			if (loopCount >= 5) {
				usingExtrapolation = false;
			}
			loopCount++;
		}
		dValue[count] = value[count] - value[countLS];
		dValueAverage = average(dValue);
		count++;
		if (count >= 5) {
			count = 0;
		}
		countLS++;
		if (countLS >= 5) {
			countLS = 0;
		}
		encoderValueLS = getValue();
	}

	public double get() {
		run();
		return Global.mod(average(value), 1023);
	}
}
