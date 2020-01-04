package sketch.view;

import java.awt.*;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.operation.overlay.PolygonBuilder;
import sketch.geo.gPoint;
import sketch.geo.gPoints;
import aPredict.predictModel;
//mport colt.src.cern.colt.matrix.*;

public class SketchPanel extends JPanel {

	public static final boolean DEBUG = true;

	private static final long serialVersionUID = 1L;

	JFrame frame;


	private boolean drawable = true;

	public void setDrawable(boolean b) {
		drawable = b;
	}

	//Vector<gPoint> ink = new Vector<gPoint>();


	Vector<gPoint> ink2 = new Vector<gPoint>();
	Vector<Vector<gPoint>> inkHistory = new Vector<>();


	gPoint prev;

	private SketchPanel getThis() {
		return this;
	}

	predictModel p1 = new predictModel();

	public SketchPanel(JFrame owner) {
		super();
		initialize();

		frame = owner;

		timeCount();
		timeLisener();
	}

	private int i = 0;

	private void timeCount() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					// System.out.println(i);

					i++;

				} catch (Exception e) {
					System.out.println("exception" + e.getMessage());
				}
			}
		}, 1000, 500);

	}

	private void timeLisener() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					if (i >= 4) {
						i = 0;
						Vector<gPoint> curink = new Vector<gPoint>();
						for (Vector<gPoint> ink: inkHistory) {
							for (int i=0;i<ink.size();i++){
								gPoint cur = ink.get(i);
								curink.add(cur);							}
						}
						
						
						if (curink.size() != 0) {

							//predict
							ListIterator<gPoint> li = curink.listIterator();
							System.out.println("predict:");
							try {
								char a1 = p1.predictChinese(li);
								System.out.println(a1);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							clear();

						}
					}

				} catch (Exception e) {
					System.out.println("exception" + e.getMessage());
				}
			}
		}, 1000, 50);
	}


	public void clear() {
		repaint();
		inkHistory = new Vector<>();

	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 300);
		this.setBackground(java.awt.Color.white);
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

			public void mouseDragged(java.awt.event.MouseEvent e) {
				i = 0;
				if (!drawable)
					return;
				gPoint p = new gPoint(e.getX(), e.getY());
				//ink.add(p);
				ink2.add(p);
				prev = p;
				getThis().repaint();
			}
		});


		this.addMouseListener(new java.awt.event.MouseAdapter() {


			public void mouseReleased(java.awt.event.MouseEvent e) {
				i = 0;
				if (!drawable)
					return;
				//ink.add(new gPoint(e.getX(), e.getY()));
				ink2.add(new gPoint(e.getX(), e.getY()));
				
				//ÅÐ¶ÏÉ¾³ý±Ê»­
				if (ink2.size() != 0) {
					ListIterator<gPoint> li = ink2.listIterator();
					try {

						char a1 = p1.predictDel(li);
						if (a1 == '1') {
							System.out.println("delete");


							//clear();
							inkHistory.remove(ink2);
							getThis().deleteBy(ink2);
							repaint();
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
			}

			public void mousePressed(java.awt.event.MouseEvent e) {
				i = 0;
				if (!drawable)
					return;
				prev = new gPoint(e.getX(), e.getY());
				//ink.add(prev);
				
				//ÐÂµÄ±Ê»­
				ink2 = new Vector<gPoint>();
				ink2.add(prev);
				inkHistory.add(ink2);
				//inkHistory.add(ink2);
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		g.clearRect(0,0,getWidth(),getHeight());
		for (Vector<gPoint> ink: inkHistory) {
			for (int i=1;i<ink.size();i++){
				gPoint prev = ink.get(i-1);
				gPoint cur = ink.get(i);
				g.drawLine(prev.X(), prev.Y(), cur.X(), cur.Y());
			}
		}
	}

	private static Geometry inkToCoordinate(Vector<gPoint> ink) {
		Coordinate[] coordinates = new Coordinate[ink.size()+1];
		for (int i=0;i<=ink.size();i++) {
			if(i==ink.size())
				coordinates[ink.size()] = new Coordinate(ink.get(0).x, ink.get(0).y);//  ±ÕºÏÏß
			int index = i % ink.size();
			coordinates[index] = new Coordinate(ink.get(index).x, ink.get(index).y);
		}
		//if(coordinates!=null) System.out.println(ink.size()+"null------------------"+coordinates[ink.size()] );
		Geometry geo = new GeometryFactory().createPolygon(coordinates);
		return geo;
	}

	private void deleteBy(Vector<gPoint> ink) {
		Geometry geo = SketchPanel.inkToCoordinate(ink).convexHull();
		final ArrayList<Vector<gPoint>> toBeDeleted = new ArrayList<>();
		for (Vector<gPoint> v: inkHistory) {
			Geometry cur = SketchPanel.inkToCoordinate(v).convexHull();
			if (geo.intersection(cur).getArea() / cur.getArea() > 0.5) {
				toBeDeleted.add(v);
			}
		}
		for (Vector<gPoint> v: toBeDeleted) {
			inkHistory.remove(v);
		}
		getThis().repaint();
	}
}