package test;

import java.text.DecimalFormat;

import main.arithmetic.SimUtils;
import main.matter.Line;
import main.matter.LineSegment;
import main.matter.Point;

public class TempTest {

	public static void main(String[] args) {
		double x=1;
		double y=1;
		Point p0 = new Point(0,0);
		Point p1 = new Point(1,0);
		Point p2 = new Point(0,1);
		Point p3 = new Point(-1,0);
		Point p4 = new Point(0,-1);
		Point p5 = new Point(1,1);
		LineSegment line05 = new LineSegment(p0,p5);
		System.out.println("line05---" + line05.toString());
		line05.move(SimUtils.LEFT, 1);
		System.out.println("line05---" + line05.toString());
		
		LineSegment line34 = new LineSegment(p3,p4);
		
		LineSegment line32 = new LineSegment(p3,p2);
		System.out.println("line32---" + line32.toString());
		line32.move(SimUtils.RIGHT, 1);
		System.out.println("line32---" + line32.toString());
		line32.move(SimUtils.RIGHT, 1);
		System.out.println("line32---" + line32.toString());
		
		LineSegment line12 = new LineSegment(p1,p2);
		System.out.println("line12---" + line12.toString());
		line12.move(SimUtils.RIGHT, 1);
		System.out.println("line12---" + line12.toString());
		line12.move(SimUtils.RIGHT, 1);
		System.out.println("line12---" + line12.toString());
		
		System.out.println(p1.distanceToPoint(p2));
		System.out.println(p3.distanceToLine(line32));
		System.out.println(line05.distanceToLine(line32));
		System.out.println("Ïß¶Î×î¶Ì¾àÀë£º" + line05.minDistanceToLineSegment(line32).length);
		Line line01 = new Line(p0,p1);
		System.out.println(line01.directionAngle/Math.PI);
		System.out.println(line05.angleToLine(line32));
		System.out.println(line05.intersectionLineAndLine(line34).toString());
		System.out.println(line05.getFootOfPerpendicular(p3).toString());
		System.out.println(p4.leftOrRightToLine(line05));	
		System.out.println(Math.tan(Math.PI/2));	
	}

}
