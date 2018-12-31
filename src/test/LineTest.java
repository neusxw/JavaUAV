package test;

import java.text.DecimalFormat;

import main.arithmetic.SimUtils;
import main.entity.Line;
import main.entity.LineSegment;
import main.entity.Point;

public class LineTest {

	public static void main(String[] args) {
		double x=1;
		double y=1;
		Point p0 = new Point(0,0);
		Point p1 = new Point(1,0);
		Point p2 = new Point(0,1);
		Point p3 = new Point(-1,0);
		Point p4 = new Point(0,-1);
		Point p5 = new Point(1,1);
		Point p6 = new Point(0.5,0.5);
		Point p7 = new Point(-0.5,-0.5);
		Point p8 = new Point(-1,-1);
		LineSegment line05 = new LineSegment(p0,p5);
		LineSegment line34 = new LineSegment(p3,p4);
		LineSegment line32 = new LineSegment(p3,p2);
		LineSegment line12 = new LineSegment(p1,p2);
		LineSegment line57 = new LineSegment(p5,p7);
		LineSegment line68 = new LineSegment(p6,p8);
		line57.print();
		line68.print();
		line57.intersectionLineSegmentOfTwoLineSegments(line68).print();
	}

}
