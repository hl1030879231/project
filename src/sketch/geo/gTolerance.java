package sketch.geo;

public class gTolerance {
	
	private gTolerance() {}

	public static final double dTol = 1e-4;
	public static final double d2Tol = dTol * Math.sqrt(2);
	
	public static final double aTol = Math.PI * 1.0 / 180.0;
	
	public static final double pTol = 1e-8;
	
	public static final double nearTol = 4;
	
	public static final double pareTol = aTol * 10;
	
	public static final double disSegment = 30;
	
	public static boolean isNear(gPoint p1, gPoint p2) {
		if (p1.distance(p2) < nearTol) 
			return true;
		return false;
	}
	
	public static boolean isNear(gPoint p, gLine l) {
		return Math.abs(l.distanceTo(p)) < nearTol;
	}
	
	public static void main(String args[]) {
		System.out.println(new gLine(new gPoint(0, 1), new gPoint(1, 0.5)).distanceTo(new gPoint(0, 0)));
	}
}
