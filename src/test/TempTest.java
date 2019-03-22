package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.arithmetic.AllocationUAV;
import main.arithmetic.data.CoordTrans;
import main.arithmetic.data.SimUtils;
import main.entity.SimpleGrid;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

public class TempTest {

	public static void main(String[] args) {
		CoordTrans ct = new CoordTrans(1,2);
		//System.out.println(ct.getGeographyorigin()[0]);
		CoordTrans ct2 = new CoordTrans(10,2);
		//System.out.println(ct.getGeographyorigin()[0]);
		String str = new String("1.0 2.0 3.0 4.0");
		String[] strArray = str.split(";");
		System.out.println(strArray[0].split(" "));
		for(String s:strArray) {
			System.out.println(s);
		}
		System.out.println("abc".equals("abc"));
		
		AllocationUAV al = new AllocationUAV(8,3);
		//al.allocation();
		List<LineSegment> tempListAdd = new ArrayList<LineSegment>();
		System.out.println(tempListAdd.size());
		tempListAdd.add(null);
		System.out.println(tempListAdd.size());
		for(LineSegment lineSegment:tempListAdd) {
			//System.out.println(lineSegment.length);
		}
		System.out.println("-----------");
		Double x= new Double(-9);
		Double y= new Double(2);
		//System.out.println(x.compareTo(y));
		double[] d = {1.2,1.5,2.6,3.7};
		int sum=0;
		for(int i=0;i<d.length;i++) {
			sum+=d[i];
		}
		System.out.println(sum);
		List<Point> points = new ArrayList<Point>();
		points.add(new Point(196.03,119.35));
		points.add(new Point(279.02,147.41));
		points.add(new Point(169.10,392.51));
		points.add(new Point(95.23,344.08));
		Point p1 = new Point(-12.74,255.42); 
		Polygon polygon = new Polygon(points);
		System.out.println(p1.positionToPolygon(polygon));
		
		Point pp1 = new Point(163.57995940072223,406.8855062776166);
		Point pp2 = new Point(132.16103891756703,422.9736822458449);
		System.out.println(SimpleGrid.isConnected(pp1, pp2));
		
		Polygon polygon2 = new Polygon(new double[] {0,1,0.7}, new double[] {0,0.3,0.9}); 
		Polygon polygon3 = new Polygon(new double[] {1.0,0,-1.0,0}, new double[] {0,-1.0,0,1.0}); 
		System.out.println(polygon2.area());
		System.out.println(polygon3.area());
		
		LineSegment shortLine = new LineSegment(new Point(218,681.3),new Point(238,636.7));
		//shortLine.print();
		LineSegment neighborLine = new LineSegment(new Point(214.3,679.8),new Point(274.4,545.8));
		//neighborLine.print();
		LineSegment farLine = new LineSegment(new Point(144.4,649.9),new Point(205.8,512.9));
		//farLine.print();
		System.out.println(shortLine.distanceToParallelLineSegment(neighborLine));
		System.out.println(shortLine.distanceToParallelLineSegment(farLine));
		System.out.println(shortLine.minDistanceToLineSegment(neighborLine));
		System.out.println(shortLine.minDistanceToLineSegment(farLine));
		System.out.println(shortLine.getMidPoint().distanceToPoint(neighborLine.getMidPoint()));
		System.out.println(shortLine.getMidPoint().distanceToPoint(farLine.getMidPoint()));
	}

}
