package sketch.geo;

public class gVector implements Geometry {
	
	public static final gVector zeroVector = new gVector(0, 0);
	public static final gVector xUnitVector = new gVector(1, 0);
	public static final gVector yUnitVector = new gVector(0, 1);

	private final double x, y;
	
	private gVector unitized = null;
	
	public gVector(double xx, double yy) {
		x = xx;
		y = yy;
	}

	public double x() {
		return x;
	}
	
	public double y() {
		return y;
	}
	
	public gVector reverse() {
		return new gVector(-x(), -y());
	}
	
	public gVector vertical() {
		return new gVector(-y(), x());
	}
	
	public double dotProduct(gVector mate) {
		return x() * mate.x() + y() * mate.y();
	}
	
	public double zOfCrossProduct(gVector mate) {
		return x() * mate.y() - y() * mate.x();
	}

	public gVector add(gVector mate) {
		return new gVector(x() + mate.x(), y() + mate.y());
	}

	public gVector subtract(gVector mate) {
		return new gVector(x() - mate.x(), y() - mate.y());
	}

	public gVector multiply(double scale) {
		return new gVector(x() * scale, y() * scale);
	}

	public gVector divide(double scale) {
		return new gVector(x() / scale, y() / scale);
	}
	
	public gVector rotate(double arc) {
		// angle = arc * Math.PI
		double cos = Math.cos(arc * Math.PI);
		double sin = Math.sin(arc * Math.PI);
		double x = x() * cos + y() * sin;
		double y = x() * (-sin) + y() * cos;
		return new gVector(x, y);
	}
	
	public gVector unitized() {
		if (unitized != null)
			return unitized;

		double leng = length();
		if (!gUtil.isDividable(Math.max(x(), y()), leng)) {
//			throw new JgclZeroLength();
			return (unitized = zeroVector);
		}

		return (unitized = new gVector(x()/leng, y()/leng));
	}
	
	public double norm() {
		double xv = x();
		double yv = y();

		return xv * xv + yv * yv;
	}
	
	public double length() {
		return Math.sqrt(norm());
	}
	
	public gPoint toPoint2D() {
		return new gPoint(x(), y());
	}
	
	public boolean identical(gVector mate) {
		double dTol2 = gTolerance.d2Tol;
		double xv, yv;

		xv = x() - mate.x();
		yv = y() - mate.y();

		return xv * xv + yv * yv < dTol2;
	}
	
	private boolean identicalDirection(gVector mate, boolean allowReversed) {
		double aTol = gTolerance.aTol;
		double dTol2 = gTolerance.d2Tol;
		double dotProd, zOfCrossProd;
		boolean result; // return value

		if (this.norm() < dTol2 || mate.norm() < dTol2) {
			result = true;
		} else {
			dotProd = dotProduct(mate); // cos(theta)*|this|*|mate|
			if (allowReversed)
				dotProd = Math.abs(dotProd);
			zOfCrossProd = zOfCrossProduct(mate); // sin(theta)*|this|*|mate|
			result = Math.abs(Math.atan2(zOfCrossProd, dotProd)) < aTol;
		}

		return result;
	}
	
	public boolean parallelDirection(gVector mate) {
		return identicalDirection(mate, true);
	}
	
	public double angleWith(gVector mate) {
		gVector thisUnitVec;
		gVector mateUnitVec;

		thisUnitVec = this.unitized();
		mateUnitVec = mate.unitized();
		//TODO: zero handling
		/*
		 * dot product --> radian
		 */
		double dotProduct = thisUnitVec.dotProduct(mateUnitVec);
		if (dotProduct > 1.0)
			dotProduct = 1.0;
		if (dotProduct < (-1.0))
			dotProduct = (-1.0);

		double theta = Math.acos(dotProduct);

		if (thisUnitVec.zOfCrossProduct(mateUnitVec) < 0.0)
			theta = (Math.PI * 2) - theta;

		return theta;
	}
	
	public String toString() {
		return "geo.vector:" + x + "," + y;
	}
	
	public static void main(String[] args) {
		gVector v1 = new gVector(1, 1);
		gVector v2 = new gVector(1, 0);
		
		System.out.println(v1.rotate(-.5));
		
	}
}
