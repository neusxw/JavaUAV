package test;

import main.matter.Line;
import main.matter.LineSegment;
import main.matter.Point;
import main.matter.DirectionalLine;

public class TempTest {

	public static void main(String[] args) {
		double x=1;
		double y=1;
		System.out.println(Math.atan2(-y, x));
		Point p0 = new Point(0,0);
		Point p1 = new Point(-1,0);
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
		System.out.println(line1.distanceToStraightLine(line2));
		System.out.println("œﬂ∂Œæ‡¿Î£∫" + line1.distanceToLinesegment(line2).length);
		DirectionalLine line3 = new DirectionalLine(0,1,0);
		System.out.println(line3.direction/Math.PI);
		System.out.println(line1.angleToStraightLine(line2));
		System.out.println(line0.Intersection(line1).x +","+line0.Intersection(line1).y);
		
		  for(int i = 401;i<801;i++) { if(i<10) { System.out.println("ADYH1700"+i); }else
		  if(i<100) { System.out.println("ADYH170"+i); }else {
		  System.out.println("ADYH17"+i); } }
		 
	}

}
