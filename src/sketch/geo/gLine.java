package sketch.geo;

import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;

public class gLine extends Line2D.Double implements Geometry {

	private final gVector dir;
	
	public gLine(gPoint s, gPoint e) {
		super(s, e);
		dir = epn().subtract(spn()).toVector();
	}
	
	public gPoint linearInterpolate(double d) {
		return spn().linearInterpolate(epn(), d);
	}
	
	public gPoint spn() {
		return new gPoint(getX1(), getY1());
	}
	
	public gPoint epn() {
		return new gPoint(getX2(), getY2());
	}
	
	public gVector dir() {
		return dir;
	}
	
	public Line2D toLine2D() {
		return new Line2D.Double(spn().x(), spn().y(),
				epn().x(), epn().y());
	}
	
	public double distanceTo(gPoint p) {
		gVector vl = this.dir();
		gVector vp = p.subtract(this.spn()).toVector();
		
		return vl.zOfCrossProduct(vp) / vl.length();
	}
	
	/**
	 * @param line
	 * @return the cosine value of the 
	 */
	public double cosOfAngleTo(gLine line) {
	//	double a = this.spn().distance(this.epn());
	//	double b = line.spn().distance(mate);
		double a = this.length();
		double b = line.length();
		double c = this.epn().distance(line.epn());
		double cosine = (Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) /
								(2*a*b); // the theorem of cosine
		return cosine;
	}

	public double length() {
		return dir().length();
	}
	
	public boolean intersects(gLine mate) {
		return this.toLine2D().intersectsLine(mate.toLine2D());
	}
	
	public static void main(String[] args) {
		gPoint p1 = new gPoint(0,0);
		gPoint p2 = new gPoint(1,1);
		gPoint p3 = new gPoint(1,0);
		gPoint p4 = new gPoint(0,1);
		
		gLine l1 = new gLine(p1,p2);
		gLine l2 = new gLine(p1,p3);
		gLine l3 = new gLine(p1,p4);
		gLine l4 = new gLine(p2,p4);
		
		System.err.println(l1.intersects(l2));
		System.err.println(l1.intersects(l3));
		System.err.println(l2.intersects(l4));
	}
}
