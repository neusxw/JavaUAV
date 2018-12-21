package test;

import main.matter.Point;

public class TempTest {

	public static void main(String[] args) {
		double x=1;
		double y=1;
		System.out.println(Math.atan2(-y, x));
		Point p1 = new Point(1,1);
		Point p2 = new Point(1,1);
		Point p3 = new Point(1,1);
		double d = Math.sqrt(Math.pow(2 - p1.getX(), 2)+Math.pow(3 - p1.getY(), 2));
		System.out.println(d);
		System.out.println(Math.sqrt(5));
	}

}
