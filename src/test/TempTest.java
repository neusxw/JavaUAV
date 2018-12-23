package test;

import main.matter.Line;
import main.matter.LineSegment;
import main.matter.Point;

public class TempTest {

	public static void main(String[] args) {
		double x=1;
		double y=1;
		System.out.println(Math.atan2(-y, x));
		Point p1 = new Point(-1,0);
		Point p2 = new Point(0,1);
		Point p3 = new Point(0,-1);
		Point p4 = new Point(1,0);
		LineSegment line1 = new LineSegment(p1,p3);
		LineSegment line2 = new LineSegment(p2,p4);
		Line line3 = new Line(0,0,2);
		System.out.println(p1.distanceToPoint(p2));
		System.out.println(p3.distanceToStraightLine(line2));
		System.out.println(line1.distanceToStraightLine(line2));
		System.out.println("œﬂ∂Œæ‡¿Î£∫" + line1.distanceToLinesegment(line2).length);
		System.out.println(Math.atan2(1, 0));
	}

}
