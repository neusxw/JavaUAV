package test;

import main.matter.Line;
import main.matter.LineSegment;
import main.matter.Point;

public class TempTest {

	public static void main(String[] args) {
		double x=1;
		double y=1;
		System.out.println(Math.atan2(-y, x));
		Point p0 = new Point(0,0);
		Point p1 = new Point(-1,-1);
		Point p2 = new Point(0,1);
		Point p3 = new Point(0,-1);
		Point p4 = new Point(1,0);
		Point p5 = new Point(1,1);
		LineSegment line0 = new LineSegment(p0,p5);
		LineSegment line1 = new LineSegment(p2,p4);
		System.out.println("line1:" + line1.A + "	"+line1.B+"	"+line1.C);
		LineSegment line2 = new LineSegment(p3,p2);
		System.out.println("line2:" + line2.A + "	"+line2.B+"	"+line2.C);
		System.out.println(p1.distanceToPoint(p2));
		System.out.println(p3.distanceToStraightLine(line2));
		System.out.println(line1.distanceToLine(line2));
		System.out.println("œﬂ∂Œæ‡¿Î£∫" + line1.minDistanceToLineSegment(line2).length);
		Line line3 = new Line(p0,p1);
		System.out.println(line3.direction/Math.PI);
		System.out.println(line1.angleToDirectionalLine(line2));
		System.out.println(line0.Intersection(line1).x +","+line0.Intersection(line1).y);
		System.out.println(line0.getFootOfPerpendicular(p3).x + "," + line0.getFootOfPerpendicular(p3).y);
		System.out.println(p4.leftOrRightToLine(line0));
		 
	}

}
