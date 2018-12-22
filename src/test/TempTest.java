package test;

import main.matter.Line;
import main.matter.Point;

public class TempTest {

	public static void main(String[] args) {
		double x=1;
		double y=1;
		System.out.println(Math.atan2(-y, x));
		Point p1 = new Point(-1,0);
		Point p2 = new Point(0,1);
		Point p3 = new Point(0,0);
		Point p4 = new Point(0,1);
		Line line1 = new Line(p1,p2);
		Line line2 = new Line(p3,p4);
		
		System.out.println(line2.getA());
		System.out.println(line2.getB());
		System.out.println(line2.getC());
		
		System.out.println(p3.distanceToStraightLine(line2));
		System.out.println(line1.distanceToStraightLine(line2));
	}

}
