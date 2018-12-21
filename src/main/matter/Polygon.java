package main.matter;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
	public List<Point> points= new ArrayList<Point>();

	public void addPoint(Point p) {
		points.add(p);
	}

	public void createPolygonFromArray(double[] x, double[] y) {
		if(x.length!=y.length) {
			System.out.println("横纵坐标长度不相等！");
			return;
		}
		for(int i=0; i< x.length; i++) {
			points.add(new Point(x[i],y[i]));
		}
		//形成环路
		points.add(new Point(x[0],y[0]));
	}
}
