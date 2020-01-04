package sketch.geo;

import java.awt.geom.Rectangle2D;
import sketch.geo.gPoint;

public class gRect extends Rectangle2D.Double implements Geometry {

	public gRect(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	
	public gRect(double x, double y, double w, double h) {
		super((int)x, (int)y, (int)w, (int)h);
	}
	
	// Get center point
	public gPoint getCenterPoint(){
		return new gPoint(getCenterX(), getCenterY());
	}
	
	// Get perimeter
	public double getPerimeter(){
		return getWidth() * 2 + getHeight() * 2;
	}	
	
	// Get area
	public double getArea(){
		return getWidth() * getHeight();
	}
	
	// Get longer side
	public double getLongerSide(){
		if (getWidth() > getHeight())
			return getWidth();
		else
			return getHeight();
	}
	
	// Get shorter side
	public double getShorterSide(){
		if (getWidth() < getHeight())
			return getWidth();
		else
			return getHeight();
	}
	
	// create union
	public gRect createUnion( gRect rect) {
		Rectangle2D r = super.createUnion( rect );
		return new gRect( r.getX(), r.getY(), r.getWidth(), r.getHeight() );
	}
}
