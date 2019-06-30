package test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import main.data.SimUtils;
import main.entity.geometry.Line;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

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
		List<Point> points = new ArrayList<Point>();
		points.add(p0);
		points.add(p1);
		points.add(p2);
		points.add(p3);
		for (Point point:points) {
			System.out.println(point);
		}
		p1=null;
		System.gc();
		System.out.println("*******************");
		for (Point point:points) {
			System.out.println(point);
		}
		LineSegment line05 = new LineSegment(p0,p5);
		LineSegment line34 = new LineSegment(p3,p4);
		LineSegment line32 = new LineSegment(p3,p2);
		LineSegment line12 = new LineSegment(p1,p2);
		LineSegment line57 = new LineSegment(p5,p7);
		LineSegment line68 = new LineSegment(p6,p8);
		LineSegment ls1= new LineSegment(new Point(0.42,0.73),new Point(0.42,0.35));
		LineSegment ls2= new LineSegment(new Point(0.42,0.50),new Point(0.42,0.40));
		//ls1.intersectionLineSegmentOfTwoLineSegments(ls2).print();
		System.out.println("OOOOOOOOO");
		//ls2.intersectionLineSegmentOfTwoLineSegments(ls1).print();
		Point pp1 = new Point(0,1);
		Point pp2 = new Point(0,2);
		Point pp3 = new Point(0,3);
		Point pp4 = new Point(0,4);
		Line ll1= new Line(pp1,pp2);
		Line ll2= new Line(pp4,pp3);
		System.out.println(ll1.equals(ll2));
		
	}

}
