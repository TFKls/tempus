package dev.tfkls.tempus.util;

public class MathUtil {
	public static int roundUp(float f) {
		return (int) (Math.signum(f) * Math.ceil(Math.abs(f)));
	}

	public static int roundDown(float f) {
		return (int) (Math.signum(f) * Math.floor(Math.abs(f)));
	}

	public static boolean shiftedUniformRandom(int low, int high, int val) {
		return val > (low + (Math.random() * (high - low)));
	}
}
