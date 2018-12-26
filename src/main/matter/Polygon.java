package main.matter;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
	public List<Point> vertexes= new ArrayList<Point>();
	public List<LineSegment> edges = new ArrayList<LineSegment>();

	public Polygon() {}
	
	public Polygon(double[] x, double[] y) {
		createPolygonFromArray(x, y);
	}
	
	public void createPolygonFromArray(double[] x, double[] y) {
		if(x.length!=y.length) {
			System.out.println("横纵坐标长度不相等！");
			return;
		}
		for(int i=0; i< x.length; i++) {
			vertexes.add(new Point(x[i],y[i]));
		}
		for(int i=0;i<x.length -1;i++) {
			edges.add(new LineSegment(vertexes.get(i),vertexes.get(i+1)));
		}
		edges.add(new LineSegment(vertexes.get(x.length-1),vertexes.get(0)));
	}
	
	
}
