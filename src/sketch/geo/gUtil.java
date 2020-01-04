package sketch.geo;

public class gUtil {
	
	public static final double doubleEplison = determineDoubleValue();

	private gUtil() {}
	
	public static boolean isReciprocatable(double value) {
		if (Math.abs(value) < doubleEplison)
			return false;
		return true;
	}
	
	public static boolean isDividable(double a, double b) {
		double c;

		// Division by floating point does never throw any exception.
		c = b / a; // reverse

		if (Double.isNaN(c) || !isReciprocatable(c))
			// Math.abs(c) < JgclMachineEpsilon.DOUBLE)
			return false;
		return true;
	}
	
	private static double determineDoubleValue() {
		double half = 0.5;
		double one = 1.0;
		double two = 2.0;
		double meps = one;

		for (;; meps *= half) {
			double work = one + meps;
			if (work <= one)
				break;
		}

		return (meps * two);
	}
}
