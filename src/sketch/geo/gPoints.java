package sketch.geo;

import java.util.StringTokenizer;
import java.util.Vector;

public class gPoints implements Geometry {

	private final Vector<gPoint> points;
	
	public gPoints(Vector<gPoint> ps) {
		assert ps != null;
		points = ps;
	}
	
	public gPoints() {
		points = new Vector<gPoint>();
	}
	
	public gPoint spn() {
		assert points.size() > 0;
		return points.firstElement();
	}
	
	public gPoint epn() {
		assert points.size() > 0;
		return points.lastElement(); 
	}
	
	public gPoint get(int i) {
		return points.get(i);
	}
	
	public String toString() {
		String value = "";
		for (gPoint p : points) {
			value += p.toString();
		}
		return value;
	}
	
	public Vector<gPoint> getPoints() {
		return points;
	}
	
	public int size() {
		return points.size();
	}

	public static gPoints parseGPoints(String s) {
		Vector<gPoint> ps = new Vector<gPoint>();
		
		StringTokenizer stz = new StringTokenizer(s, ")(");
		while (stz.hasMoreTokens()) {
			ps.add(gPoint.parseGPoint(stz.nextToken()));
		}
		return new gPoints(ps);
	}

	public gPoints subPoints(int start, int end) {
		Vector<gPoint> ps = new Vector<gPoint>();
		for (int i = start; i <= end; i++) {
			ps.add(points.get(i));
		}
		return new gPoints(ps);
	}
	
	public gPoints subPoints(int start) {
		Vector<gPoint> ps = new Vector<gPoint>();
		for (int i = start; i < points.size(); i++) {
			ps.add(points.get(i));
		}
		return new gPoints(ps);
	}
	
	public void add(gPoint point) {
		points.add(point);
	}
	
	public void add(int index, gPoint point) {
		points.add(index, point);
	}
	
	public void remove(int index) {
		points.remove(index);
	}
	
	public int indexOf(gPoint point) {
		return points.indexOf(point);
	}
	
	public boolean contains(gPoint point) {
		return points.contains(point);
	}
	
	public double length() {
		// Get number of points
		int numOfPoints = points.size();
		// Set stroke length
		double length = 0;
		for (int i = 0; i < numOfPoints - 1; i++){
			// first point
			gPoint point1 = points.get(i);
			// second point
			gPoint point2 = points.get(i + 1);
			// set length
			length += point1.distance(point2);
		}
		return length;
	}
}
