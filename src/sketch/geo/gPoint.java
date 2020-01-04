package sketch.geo;

import java.awt.geom.Point2D;
import java.util.StringTokenizer;
import java.util.Vector;


public class gPoint extends Point2D.Double implements Geometry {
	
	private static final double nearThres = 5;
	private double time;
	
	public static final gPoint zeroPoint = new gPoint(0, 0);
	

	public gPoint(double xx, double yy) {
		super(xx, yy);
	}
	
//	public gPoint(double xx, double yy, double tt) {
//		super(xx, yy);
//		this.time = tt;
//	}kert2019.10.08
	
	public boolean isNear(gPoint p) {
		return (distance(p) < nearThres);
	}
	
	public int X() {
		return (int) x;
	}
	
	public int Y() {
		return (int) y;
	}
	
	public double x() {
		return x;
	}
	
	public double y() {
		return y;
	}
	public double getTime() {
		return time;
	}
	public gPoint add(gVector vector) {
		return new gPoint(x() + vector.x(), y() + vector.y());
	}
	
	public gPoint add(gPoint mate) {
		return new gPoint(x() + mate.x(), y() + mate.y());
	}
	
	public gPoint subtract(gVector vector) {
		return new gPoint(x() - vector.x(), y() - vector.y());
	}
	
	public gPoint subtract(gPoint mate) {
		return new gPoint(x() - mate.x(), y() - mate.y());
	}
	
	public gPoint multiply(double scale) {
		return new gPoint(x() * scale, y() * scale);
	}
	
	public gPoint divide(double scale) {
		return new gPoint(x() / scale, y() / scale);
	}
	
	public double distance2(gPoint mate) {
		double dx, dy;

		dx = x() - mate.x();
		dy = y() - mate.y();

		return (dx * dx + dy * dy);
	}
	
	public double distance(gPoint mate) {
		return Math.sqrt(distance2(mate));
	}
	
	public gPoint linearInterpolate(gPoint mate, double weightForThis) {
		double weightForMate = 1.0 - weightForThis;
		return new gPoint(this.x() * weightForThis + mate.x()
				* weightForMate, this.y() * weightForThis + mate.y()
				* weightForMate);
	}
	
	public gPoint midPoint(gPoint mate) {
		return linearInterpolate(mate, 0.5);
	}
	
	public boolean identical(gPoint mate) {
		return distance(mate) < gTolerance.d2Tol;
	}
	
	public gVector toVector() {
		return new gVector(x(), y());
	}
	
	public gPoint copy() {
		return new gPoint(x(), y());
	}
	
	public double distanceTo(gLine line) {
		gVector vp = this.subtract(line.spn()).toVector();
		gVector vv = line.dir();
		
		double a = vp.zOfCrossProduct(vv);
		double b = vv.length();
		
		if (gUtil.isDividable(a, b))
			return a/b;
		return 0;
	}
	
	public String toString() {
		return "(" + X() + "," + Y() + ")";
	}
	
	public static gPoint parseGPoint(String s) {
		s = s.replace("(", "").replace(")", "");
		String ss[] = s.split(",");
		
		return new gPoint(java.lang.Double.parseDouble(ss[0]), java.lang.Double.parseDouble(ss[1]));
	}
		
	public static Vector<gPoint> parseGPoints(String s) {
		Vector<gPoint> ps = new Vector<gPoint>();
		
		StringTokenizer stz = new StringTokenizer(s, ")(");
		while (stz.hasMoreTokens()) {
			ps.add(parseGPoint(stz.nextToken()));
		}
		return ps;
	}
	
//	public static void main(String[] args) {
//		gPoint p1 = new gPoint(0, 0);
//		gPoint p2 = new gPoint(1, 1);
//		gPoint p3 = new gPoint(2, 8);
//		Vector<gPoint> ps = new Vector<gPoint>();
//		ps.add(p1);
//		ps.add(p2);
//		ps.add(p3);
//		
//		System.out.println(parseGPoints(toString(ps)));
//	}
}
